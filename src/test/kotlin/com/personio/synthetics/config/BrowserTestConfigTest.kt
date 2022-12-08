package com.personio.synthetics.config

import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.net.URL
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class BrowserTestConfigTest {
    private val defaults = Defaults(300, 300, 1, 1, 60.0, 10, listOf("awsregion"))
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi, defaults)

    @Test
    fun `baseUrl function sets the base url in the test config`() {
        val expectedUrl = "https://synthetic-test.personio.de"
        browserTest.baseUrl(URL(expectedUrl))

        assertEquals(expectedUrl, browserTest.config!!.request.url)
    }

    @Test
    fun `testFrequency function sets the test frequency in the test config`() {
        browserTest.testFrequency(300.seconds)

        assertEquals(300, browserTest.options.tickEvery)
    }

    @Test
    fun `testFrequency function throws exception for test frequency value less than 5 minutes`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.testFrequency(299.seconds)
        }
    }

    @Test
    fun `testFrequency function throws exception for test frequency value bigger than 7 days`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.testFrequency(604801.seconds)
        }
    }

    @Test
    fun `retry function sets the retry count and interval in the test config`() {
        browserTest.retry(1, 60.milliseconds)

        assertEquals(1, browserTest.options.retry!!.count)
        assertEquals(60.0, browserTest.options.retry!!.interval)
    }

    @Test
    fun `retry function throws exception for retry count value bigger than 3`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.retry(3, 60.milliseconds)
        }
    }

    @Test
    fun `retry function throws exception for retry interval value bigger than 15 minutes`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.retry(1, 900001.milliseconds)
        }
    }

    @Test
    fun `minFailureDuration function sets the min failure duration in the test config`() {
        browserTest.minFailureDuration(300.seconds)

        assertEquals(300, browserTest.options.minFailureDuration)
    }

    @Test
    fun `minFailureDuration function throws exception for minimum failure duration value bigger than 120 minutes`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.minFailureDuration(7201.seconds)
        }
    }

    @Test
    fun `minLocationFailed function sets the min location failed in the test config`() {
        browserTest.publicLocation(Location.FRANKFURT_AWS)
        browserTest.minLocationFailed(1)

        assertEquals(1, browserTest.options.minLocationFailed)
    }

    @Test
    fun `minLocationFailed function throws exception for minimum location failed value less than 1`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.minLocationFailed(0)
        }
    }

    @Test
    fun `minLocationFailed function throws exception for min location failed bigger than the number of locations`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.publicLocation(Location.FRANKFURT_AWS, Location.N_CALIFORNIA_AWS)
            browserTest.minLocationFailed(3)
        }
    }

    @Test
    fun `monitorName function sets the monitor name in the test config`() {
        browserTest.monitorName("Test name")

        assertEquals("Test name", browserTest.options.monitorName)
    }

    @Test
    fun `renotifyInterval function sets the renotify interval in the test config`() {
        browserTest.renotifyInterval(RenotifyInterval.MINUTES_10)

        assertEquals(10, browserTest.options.monitorOptions!!.renotifyInterval)
    }

    @Test
    fun `monitorPriority function sets the monitor priority in the test config`() {
        browserTest.monitorPriority(MonitorPriority.P1_CRITICAL)

        assertEquals(1, browserTest.options.monitorPriority)
    }

    @Test
    fun `publicLocation function sets the public location in the test config`() {
        browserTest.publicLocation(Location.FRANKFURT_AWS)

        assertEquals(listOf(Location.FRANKFURT_AWS.value), browserTest.locations)
    }

    @Test
    fun `publicLocation function sets multiple public locations in the test config`() {
        browserTest.publicLocation(Location.FRANKFURT_AWS, Location.N_CALIFORNIA_AWS)

        assertEquals(listOf(Location.FRANKFURT_AWS.value, Location.N_CALIFORNIA_AWS.value), browserTest.locations)
    }

    @Test
    fun `browserAndDevice function sets the browser and device to the test config`() {
        browserTest.browserAndDevice(SyntheticsDeviceID.CHROME_LAPTOP_LARGE)

        assertEquals(listOf(SyntheticsDeviceID.CHROME_LAPTOP_LARGE), browserTest.options.deviceIds)
    }

    @Test
    fun `browserAndDevice function sets multiple browsers and devices in the test config`() {
        browserTest.browserAndDevice(SyntheticsDeviceID.CHROME_LAPTOP_LARGE, SyntheticsDeviceID.FIREFOX_MOBILE_SMALL)

        assertEquals(listOf(SyntheticsDeviceID.CHROME_LAPTOP_LARGE, SyntheticsDeviceID.FIREFOX_MOBILE_SMALL), browserTest.options.deviceIds)
    }
}
