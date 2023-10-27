package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.personio.synthetics.TEST_STEP_NAME
import com.personio.synthetics.builder.browser.StepsBuilder
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.getConfigFromFile
import com.personio.synthetics.model.config.Location
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.net.URL
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

class SyntheticBrowserTestBuilderTest {
    private lateinit var testBuilder: SyntheticBrowserTestBuilder

    @BeforeEach
    fun prepareSut() {
        val apiClientMock = Mockito.mock(SyntheticsApiClient::class.java)
        testBuilder =
            SyntheticBrowserTestBuilder(
                TEST_STEP_NAME,
                getConfigFromFile("config-unit-test.yaml").defaults,
                apiClientMock,
            )
    }

    @Test
    fun `baseUrl sets base URL`() {
        testBuilder.baseUrl(URL("https://synthetic-test.personio.de"))
        val result = testBuilder.build()

        assertEquals(
            "https://synthetic-test.personio.de",
            result.config.request.url,
        )
    }

    @Test
    fun `publicLocations sets locations`() {
        testBuilder.publicLocations(Location.TOKYO_AWS, Location.LONDON_AWS)
        val result = testBuilder.build()

        assertEquals(
            listOf(Location.TOKYO_AWS.value, Location.LONDON_AWS.value),
            result.locations,
        )
    }

    @Test
    fun `publicLocation sets locations`() {
        testBuilder.publicLocation(Location.TOKYO_AWS, Location.LONDON_AWS)
        val result = testBuilder.build()

        assertEquals(
            listOf(Location.TOKYO_AWS.value, Location.LONDON_AWS.value),
            result.locations,
        )
    }

    @Test
    fun `testFrequency throws IllegalArgumentException if frequency is less than 5 minutes`() {
        assertThrows<IllegalArgumentException> {
            testBuilder.testFrequency(4.minutes)
        }
    }

    @Test
    fun `testFrequency throws IllegalArgumentException if frequency is more than 7 days`() {
        assertThrows<IllegalArgumentException> {
            testBuilder.testFrequency(8.days)
        }
    }

    @Test
    fun `testFrequency sets frequency in whole seconds`() {
        testBuilder.testFrequency(7.days)
        val result = testBuilder.build()

        assertEquals(
            7.days.inWholeSeconds,
            result.options.tickEvery,
        )
    }

    @Test
    fun `browserAndDevice sets deviceIds`() {
        testBuilder.browserAndDevice(SyntheticsDeviceID.CHROME_TABLET, SyntheticsDeviceID.FIREFOX_TABLET)
        val result = testBuilder.build()

        assertEquals(
            listOf(SyntheticsDeviceID.CHROME_TABLET, SyntheticsDeviceID.FIREFOX_TABLET),
            result.options.deviceIds,
        )
    }

    @Test
    fun `browsersAndDevices sets deviceIds`() {
        testBuilder.browsersAndDevices(SyntheticsDeviceID.CHROME_TABLET, SyntheticsDeviceID.FIREFOX_TABLET)
        val result = testBuilder.build()

        assertEquals(
            listOf(SyntheticsDeviceID.CHROME_TABLET, SyntheticsDeviceID.FIREFOX_TABLET),
            result.options.deviceIds,
        )
    }

    @Test
    fun `build sets no steps by default`() {
        val result = testBuilder.build()

        assertEquals(0, result.steps.count())
    }

    @Test
    fun `steps evaluates provided lambda and sets steps`() {
        val stepsBuilderMock = Mockito.mock(StepsBuilder::class.java)
        whenever(stepsBuilderMock.build())
            .thenReturn(listOf(SyntheticsStep(), SyntheticsStep()))
        testBuilder.steps(stepsBuilderMock) {}
        val result = testBuilder.build()

        assertEquals(2, result.steps.count())
    }

    @Test
    fun `status sets status of a test`() {
        testBuilder.status(SyntheticsTestPauseStatus.LIVE)
        val result = testBuilder.build()

        assertEquals(0, result.steps.count())
    }
}
