package com.personio.synthetics.step.api

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsAssertion
import com.datadog.api.v1.client.model.SyntheticsAssertionOperator
import com.datadog.api.v1.client.model.SyntheticsAssertionTarget
import com.datadog.api.v1.client.model.SyntheticsAssertionType
import com.datadog.api.v1.client.model.SyntheticsGlobalVariableParserType
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.datadog.api.v1.client.model.SyntheticsVariableParser
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.setUrl
import com.personio.synthetics.model.api.RequestParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class ApiStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `addApiStep adds the new step item to the browser test object`() {
        browserTest.addApiStep()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `addApiStep adds step with type Run API and params of RequestParams`() {
        browserTest.addApiStep()
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.RUN_API_TEST, step?.type)
        assertInstanceOf(RequestParams::class.java, step?.params)
    }

    @Test
    fun `addApiStep creates params with request sub type http and url that of browser test`() {
        browserTest.setUrl("https://synthetic-test.personio.de")

        browserTest.addApiStep()
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("http", params.request.subtype)
        assertEquals("https://synthetic-test.personio.de", params.request.config.request.url)
    }

    @Test
    fun `addAssertion without property should add a new assertion with property as null to the step`() {
        browserTest
            .addApiStep()
            .addAssertion(
                assertionType = SyntheticsAssertionType.STATUS_CODE,
                operator = SyntheticsAssertionOperator.IS,
                expected = 200
            )
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals(1, params.request.config.assertions.size)
        assertNull(params.request.config.assertions[0].syntheticsAssertionTarget.property)
    }

    @Test
    fun `addAssertion with property should add a new assertion to the step`() {
        val assertionType = SyntheticsAssertionType.HEADER
        val property = "set-cookie"
        val operator = SyntheticsAssertionOperator.IS
        val expectedTarget = "cookie"

        browserTest
            .addApiStep()
            .addAssertion(
                assertionType = assertionType,
                property = property,
                operator = operator,
                expected = expectedTarget
            )
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expected = SyntheticsAssertion(
            SyntheticsAssertionTarget()
                .type(assertionType)
                .property(property)
                .operator(operator)
                .target(expectedTarget)
        )
        assertEquals(expected, params.request.config.assertions[0])
    }

    @Test
    fun `requestBody method should add the body to the request`() {
        browserTest
            .addApiStep()
            .requestBody("testbody")
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("testbody", params.request.config.request.body)
    }

    @Test
    fun `requestHeaders method should add the headers to the request`() {
        browserTest
            .addApiStep()
            .requestHeaders(mutableMapOf("content-type" to "application/json"))
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertTrue(params.request.config.request.headers!!.containsKey("content-type"))
        assertEquals("application/json", params.request.config.request.headers!!["content-type"])
    }

    @Test
    fun `method should add the method to the request`() {
        browserTest
            .addApiStep()
            .method(HTTPMethod.POST)
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals(HTTPMethod.POST, params.request.config.request.method)
    }

    @Test
    fun `part url passed to the url method should append to the base url into the request`() {
        browserTest.setUrl("https://synthetic-test.personio.de")
        browserTest
            .addApiStep()
            .url("login")
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("https://synthetic-test.personio.de/login", params.request.config.request.url)
    }

    @Test
    fun `full url passed to the url method should be added into the request`() {
        browserTest.setUrl("https://synthetic-test.personio.de")
        browserTest
            .addApiStep()
            .url("https://newurl.personio.de")
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("https://newurl.personio.de", params.request.config.request.url)
    }

    @Test
    fun `extractHeaderValue adds the action to the step`() {
        browserTest
            .addApiStep()
            .extractHeaderValue("name", "field")
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals(1, params.request.options.extract_values!!.size)
    }

    @Test
    fun `extractHeaderValue adds the RAW parser to the step if no regex is passed`() {
        val parserType = SyntheticsGlobalVariableParserType.RAW

        browserTest
            .addApiStep()
            .extractHeaderValue("name", "field")
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expectedResult = SyntheticsVariableParser()
            .type(parserType)
        assertEquals(expectedResult, params.request.options.extract_values!![0].parser)
    }

    @Test
    fun `extractHeaderValue adds the REGEX parser to the step if regex is passed`() {
        val parserType = SyntheticsGlobalVariableParserType.REGEX
        val regex = "regex"

        browserTest
            .addApiStep()
            .extractHeaderValue("name", "field", regex)
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expectedResult = SyntheticsVariableParser()
            .type(parserType)
            .value(regex)
        assertEquals(expectedResult, params.request.options.extract_values!![0].parser)
    }

    @Test
    fun `extractBodyValue adds the action to the step`() {
        browserTest
            .addApiStep()
            .extractBodyValue(
                name = "name",
                parserType = SyntheticsGlobalVariableParserType.RAW,
            )
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals(1, params.request.options.extract_values!!.size)
    }

    @Test
    fun `extractBodyValue adds the passed parser to the step`() {
        val name = "name"
        val parserType = SyntheticsGlobalVariableParserType.JSON_PATH
        val parserValue = "value"

        browserTest
            .addApiStep()
            .extractBodyValue(name, parserType, parserValue)
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expectedResult = SyntheticsVariableParser()
            .type(parserType)
            .value(parserValue)
        assertEquals(expectedResult, params.request.options.extract_values!![0].parser)
    }

    @Test
    fun `extractBodyValue without parser value makes the value null`() {
        browserTest
            .addApiStep()
            .extractBodyValue(
                name = "name",
                parserType = SyntheticsGlobalVariableParserType.RAW,
            )
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertNull(params.request.options.extract_values!![0].parser.value)
    }
}
