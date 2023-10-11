package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsTestRequestBodyType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RequestBuilderTest {
    private var requestBuilder = RequestBuilder()

    @Test
    fun `headers appends given headers`() {
        requestBuilder.headers(
            mapOf("any_header" to "any_value")
        )
        val result = requestBuilder.build()

        assertEquals(
            mapOf("any_header" to "any_value"),
            result.headers
        )
    }

    @Test
    fun `cookies add a Cookie header`() {
        requestBuilder.cookies("any_cookie")
        val result = requestBuilder.build()

        assertEquals(
            mapOf("Cookie" to "any_cookie"),
            result.headers
        )
    }

    @Test
    fun `url sets url`() {
        requestBuilder.url("any_url")
        val result = requestBuilder.build()

        assertEquals("any_url", result.url)
    }

    @Test
    fun `method sets http method`() {
        requestBuilder.method(RequestMethod.POST)
        val result = requestBuilder.build()

        assertEquals("POST", result.method)
    }

    @Test
    fun `body sets body`() {
        requestBuilder.body("any_body")
        val result = requestBuilder.build()

        assertEquals("any_body", result.body)
    }

    @Test
    fun `bodyType sets bodyType`() {
        requestBuilder.bodyType(SyntheticsTestRequestBodyType.APPLICATION_JSON)
        val result = requestBuilder.build()

        assertEquals(
            SyntheticsTestRequestBodyType.APPLICATION_JSON,
            result.bodyType
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `followRedirects sets followRedirects`(value: Boolean) {
        requestBuilder.followRedirects(value)
        val result = requestBuilder.build()

        assertEquals(
            value,
            result.followRedirects
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `ignoreServerCertificateError sets allowInsecure`(value: Boolean) {
        requestBuilder.ignoreServerCertificateError(value)
        val result = requestBuilder.build()

        assertEquals(
            value,
            result.allowInsecure
        )
    }
}
