package com.personio.synthetics.step.extract

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.extract.ExtractParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class ExtractStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `extractFromJavascriptStep adds new step to the browser test`() {
        browserTest.extractFromJavascriptStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `extractFromJavascriptStep creates step with type Extract From Javascript and params of type ExtractParams`() {
        browserTest.extractFromJavascriptStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT, step?.type)
        assertInstanceOf(ExtractParams::class.java, step?.params)
    }

    @Test
    fun `code in extractFromJavascriptStep adds the passed javascript code to the ExtractParams object`() {
        browserTest
            .extractFromJavascriptStep("Step") {
                code("return 1")
            }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("return 1", params.code)
    }

    @Test
    fun `variable in extractFromJavascriptStep adds the variable to the ExtractParams object`() {
        browserTest
            .extractFromJavascriptStep("Step") {
                variable("VAR1")
            }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("VAR1", params.variable?.name)
    }

    @Test
    fun `extractTextFromElementStep adds new step to the browser test`() {
        browserTest.extractTextFromElementStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `extractTextFromElementStep creates step with type Extract From Javascript and params of type ExtractParams`() {
        browserTest.extractTextFromElementStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_VARIABLE, step?.type)
        assertInstanceOf(ExtractParams::class.java, step?.params)
    }

    @Test
    fun `variable in extractTextFromElementStep adds the variable to the ExtractParams object`() {
        browserTest
            .extractTextFromElementStep("Step") {
                variable("VAR1")
            }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("VAR1", params.variable?.name)
    }
}
