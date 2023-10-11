package com.personio.synthetics.builder

import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.getConfigFromFile
import com.personio.synthetics.model.config.Location
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.net.URL

class SyntheticBrowserTestBuilderTest {
    private lateinit var testBuilder: SyntheticBrowserTestBuilder

    @BeforeEach
    fun prepareSut() {
        val apiClientMock = Mockito.mock(SyntheticsApiClient::class.java)
        testBuilder = SyntheticBrowserTestBuilder(
            "any_name",
            getConfigFromFile("config-unit-test.yaml").defaults,
            apiClientMock
        )
    }

    @Test
    fun `baseUrl sets base URL`() {
        testBuilder.baseUrl(URL("https://synthetic-test.personio.de"))
        val result = testBuilder.build()

        Assertions.assertEquals(
            "https://synthetic-test.personio.de",
            result.config.request.url
        )
    }

    @Test
    fun `publicLocations sets locations`() {
        testBuilder.publicLocations(Location.TOKYO_AWS, Location.LONDON_AWS)
        val result = testBuilder.build()

        Assertions.assertEquals(
            listOf(Location.TOKYO_AWS.value, Location.LONDON_AWS.value),
            result.locations
        )
    }

    @Test
    fun `publicLocation sets locations`() {
        testBuilder.publicLocation(Location.TOKYO_AWS, Location.LONDON_AWS)
        val result = testBuilder.build()

        Assertions.assertEquals(
            listOf(Location.TOKYO_AWS.value, Location.LONDON_AWS.value),
            result.locations
        )
    }
}
