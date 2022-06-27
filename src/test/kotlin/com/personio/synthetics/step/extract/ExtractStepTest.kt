package com.personio.synthetics.step.extract

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.extract.ExtractParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock

internal class ExtractStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `extractFromJavascriptStep adds new step to the browser test`() {
        browserTest.extractFromJavascriptStep("Step") {
            code("return 'true'")
            variable("EXTRACT_JS_STEP_VAR")
        }

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `extractFromJavascriptStep creates step with type Extract From Javascript and params of type ExtractParams`() {
        browserTest.extractFromJavascriptStep("Step") {
            code("return 'true'")
            variable("EXTRACT_JS_STEP_VAR")
        }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT, step?.type)
        assertInstanceOf(ExtractParams::class.java, step?.params)
    }

    @Test
    fun `code in extractFromJavascriptStep adds the passed javascript code to the ExtractParams object`() {
        browserTest.extractFromJavascriptStep("Step") {
            code("return 1")
            variable("EXTRACT_JS_STEP_VAR")
        }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("return 1", params.code)
    }

    @Test
    fun `variable in extractFromJavascriptStep adds the variable to the ExtractParams object`() {
        browserTest.extractFromJavascriptStep("Step") {
            variable("VAR1")
            code("return 1")
        }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("VAR1", params.variable?.name)
    }

    @Test
    fun `extractFromJavascriptStep without code and variable throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.extractFromJavascriptStep("Step") {}
        }
    }

    @Test
    fun `extractFromJavascriptStep without code throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.extractFromJavascriptStep("Step") {
                variable("VAR1")
            }
        }
    }

    @Test
    fun `extractFromJavascriptStep without variable throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.extractFromJavascriptStep("Step") {
                code("return 1")
            }
        }
    }

    @Test
    fun `extractTextFromElementStep adds new step to the browser test`() {
        browserTest.extractTextFromElementStep("Step") {
            targetElement { locator = "#locatorId" }
            variable("EXTRACT_TEXT_VAR")
        }

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `extractTextFromElementStep creates step with type Extract From Javascript and params of type ExtractParams`() {
        browserTest.extractTextFromElementStep("Step") {
            targetElement { locator = "#locatorId" }
            variable("EXTRACT_TEXT_VAR")
        }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_VARIABLE, step?.type)
        assertInstanceOf(ExtractParams::class.java, step?.params)
    }

    @Test
    fun `variable in extractTextFromElementStep adds the variable to the ExtractParams object`() {
        browserTest.extractTextFromElementStep("Step") {
            variable("VAR1")
            targetElement { locator = "#locatorId" }
        }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("VAR1", params.variable?.name)
    }

    @Test
    fun `variable in extractTextFromElementStep adds the target element to the ExtractParams object`() {
        browserTest.extractTextFromElementStep("Step") {
            variable("VAR1")
            targetElement { locator = "#locatorId" }
        }
        val params = browserTest.steps?.get(0)?.params as ExtractParams

        assertEquals("#locatorId", params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `extractTextFromElementStep without target element and variable throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.extractTextFromElementStep("Step") {}
        }
    }

    @Test
    fun `extractTextFromElementStep without target element throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.extractTextFromElementStep("Step") {
                variable("VAR1")
            }
        }
    }

    @Test
    fun `extractTextFromElementStep without variable throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.extractTextFromElementStep("Step") {
                targetElement { locator = "#locatorId" }
            }
        }
    }
}
