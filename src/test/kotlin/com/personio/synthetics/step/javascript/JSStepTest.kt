package com.personio.synthetics.step.javascript

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.javascript.JSParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class JSStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `addExtractFromJavascriptStep adds new step to the browser test`() {
        browserTest.extractFromJavascriptStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `addExtractFromJavascriptStep creates step with type Extract From Javascript and params of type JSParams`() {
        browserTest.extractFromJavascriptStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT, step?.type)
        assertInstanceOf(JSParams::class.java, step?.params)
    }

    @Test
    fun `code adds the passed javascript code to the JSParams object`() {
        browserTest
            .extractFromJavascriptStep("Step") {
                code("return 1")
            }
        val params = browserTest.steps?.get(0)?.params as JSParams

        assertEquals("return 1", params.code)
    }

    @Test
    fun `variable adds the variable to the JSParams object`() {
        browserTest
            .extractFromJavascriptStep("Step") {
                variable("VAR1")
            }
        val params = browserTest.steps?.get(0)?.params as JSParams

        assertEquals("VAR1", params.variable.name)
    }
}
