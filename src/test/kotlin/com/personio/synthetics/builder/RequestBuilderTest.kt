package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsTestRequestBodyType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RequestBuilderTest {
    @Test
    fun `headers appends given headers`() {
        val sut = RequestBuilder()
        sut.headers(
            mapOf("any_header" to "any_value")
        )
        val result = sut.build()

        assertEquals(
            mapOf("any_header" to "any_value"),
            result.headers
        )
    }

    @Test
    fun `cookies add a Cookie header`() {
        val sut = RequestBuilder()
        sut.cookies("any_cookie")
        val result = sut.build()

        assertEquals(
            mapOf("Cookie" to "any_cookie"),
            result.headers
        )
    }

    @Test
    fun `url sets url`() {
        val sut = RequestBuilder()
        sut.url("any_url")
        val result = sut.build()

        assertEquals("any_url", result.url)
    }

    @Test
    fun `method sets http method`() {
        val sut = RequestBuilder()
        sut.method(RequestMethod.POST)
        val result = sut.build()

        assertEquals("POST", result.method)
    }

    @Test
    fun `body sets body`() {
        val sut = RequestBuilder()
        sut.body("any_body")
        val result = sut.build()

        assertEquals("any_body", result.body)
    }

    @Test
    fun `bodyType sets bodyType`() {
        val sut = RequestBuilder()
        sut.bodyType(SyntheticsTestRequestBodyType.APPLICATION_JSON)
        val result = sut.build()

        assertEquals(
            SyntheticsTestRequestBodyType.APPLICATION_JSON,
            result.bodyType
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `followRedirects sets bodyType`(value: Boolean) {
        val sut = RequestBuilder()
        sut.followRedirects(value)
        val result = sut.build()

        assertEquals(
            value,
            result.followRedirects
        )
    }
}
