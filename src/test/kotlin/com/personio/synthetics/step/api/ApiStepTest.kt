package com.personio.synthetics.step.api

import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionType
import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParserType
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.datadog.api.client.v1.model.SyntheticsVariableParser
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.config.baseUrl
import com.personio.synthetics.config.fromVariable
import com.personio.synthetics.model.api.RequestParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.net.URL

internal class ApiStepTest {
    private val browserTest = BrowserTest("Test", mock(), mock())

    @Test
    fun `addApiStep adds the new step item to the browser test object`() {
        browserTest.apiStep("Step", "POST") { }

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `addApiStep adds step with type Run API and params of RequestParams`() {
        browserTest.apiStep("Step", "POST") { }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.RUN_API_TEST, step?.type)
        assertInstanceOf(RequestParams::class.java, step?.params)
    }

    @Test
    fun `addApiStep creates params with request sub type http and url that of browser test`() {
        browserTest.baseUrl(URL("https://synthetic-test.personio.de"))

        browserTest.apiStep("Step", "POST") { }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("http", params.request.subtype)
        assertEquals("https://synthetic-test.personio.de", params.request.config.request.url)
    }

    @Test
    fun `addAssertion without property should add a new assertion with property as null to the step`() {
        browserTest.apiStep("Step", "GET") {
            assertion {
                type = SyntheticsAssertionType.STATUS_CODE
                operator = SyntheticsAssertionOperator.IS
                target = 200
            }
        }
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

        browserTest.apiStep("Step", "POST") {
            assertion {
                type = assertionType
                this.property = property
                this.operator = operator
                target = expectedTarget
            }
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expected =
            SyntheticsAssertion(
                SyntheticsAssertionTarget().type(assertionType).property(property).operator(operator).target(expectedTarget),
            )
        assertEquals(expected, params.request.config.assertions[0])
    }

    @Test
    fun `requestBody method should add the body to the request`() {
        browserTest.apiStep("Step", "GET") {
            requestBody("testbody")
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("testbody", params.request.config.request.body)
    }

    @Test
    fun `requestHeaders method should add the headers to the request`() {
        browserTest.apiStep("Step", "POST") {
            requestHeaders(mutableMapOf("content-type" to "application/json"))
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertTrue(params.request.config.request.headers!!.containsKey("content-type"))
        assertEquals("application/json", params.request.config.request.headers!!["content-type"])
    }

    @Test
    fun `method should add the method to the request`() {
        browserTest.apiStep("Step", "POST") {}
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("POST", params.request.config.request.method)
    }

    @Test
    fun `part url passed to the url method should append to the base url into the request`() {
        browserTest.baseUrl(URL("https://synthetic-test.personio.de"))
        browserTest.apiStep("Step", "GET") {
            url("/login")
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("https://synthetic-test.personio.de/login", params.request.config.request.url)
    }

    @Test
    fun `full url passed to the url method should be added into the request`() {
        browserTest.baseUrl(URL("https://synthetic-test.personio.de"))
        browserTest.apiStep("Step", "GET") {
            url("https://newurl.personio.de")
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("https://newurl.personio.de", params.request.config.request.url)
    }

    @Test
    fun `the url could be passed as a datadog variable and it should be added to the request`() {
        val variable = "API_ENDPOINT"
        browserTest
            .apiStep("Step", "GET") {
                url(fromVariable(variable))
            }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals("{{ $variable }}", params.request.config.request.url)
    }

    @Test
    fun `extractHeaderValue adds the action to the step`() {
        browserTest.apiStep("Step", "GET") {
            extractHeaderValue("name", "field")
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals(1, params.request.options.extract_values!!.size)
    }

    @Test
    fun `extractHeaderValue adds the RAW parser to the step if no regex is passed`() {
        val parserType = SyntheticsGlobalVariableParserType.RAW

        browserTest.apiStep("Step", "POST") {
            extractHeaderValue("name", "field")
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expectedResult = SyntheticsVariableParser().type(parserType)
        assertEquals(expectedResult, params.request.options.extract_values!![0].parser)
    }

    @Test
    fun `extractHeaderValue adds the REGEX parser to the step if regex is passed`() {
        val parserType = SyntheticsGlobalVariableParserType.REGEX
        val regex = "regex"

        browserTest.apiStep("Step", "GET") {
            extractHeaderValue("name", "field", regex)
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expectedResult = SyntheticsVariableParser().type(parserType).value(regex)
        assertEquals(expectedResult, params.request.options.extract_values!![0].parser)
    }

    @Test
    fun `extractBodyValue adds the action to the step`() {
        browserTest.apiStep("Step", "POST") {
            extractBodyValue(
                name = "name",
                parserType = SyntheticsGlobalVariableParserType.RAW,
            )
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertEquals(1, params.request.options.extract_values!!.size)
    }

    @Test
    fun `extractBodyValue adds the passed parser to the step`() {
        val name = "name"
        val parserType = SyntheticsGlobalVariableParserType.JSON_PATH
        val parserValue = "value"

        browserTest.apiStep("Step", "POST") {
            extractBodyValue(name, parserType, parserValue)
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        val expectedResult = SyntheticsVariableParser().type(parserType).value(parserValue)
        assertEquals(expectedResult, params.request.options.extract_values!![0].parser)
    }

    @Test
    fun `extractBodyValue without parser value makes the value null`() {
        browserTest.apiStep("Step", "POST") {
            extractBodyValue(
                name = "name",
                parserType = SyntheticsGlobalVariableParserType.RAW,
            )
        }
        val params = browserTest.steps?.get(0)?.params as RequestParams

        assertNull(params.request.options.extract_values!![0].parser.value)
    }
}
