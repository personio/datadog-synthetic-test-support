package com.personio.synthetics.step.extract

import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.extract.ExtractParams
import com.personio.synthetics.step.ui.model.TargetElement
import com.personio.synthetics.step.waitBeforeDeclaringStepAsFailed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import kotlin.time.Duration.Companion.seconds

internal class ExtractStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `extractFromJavascriptStep adds new step to the browser test`() {
        browserTest.extractFromJavascriptStep(
            stepName = "Step",
            code = "return 'true'",
            variableName = "EXTRACT_JS_STEP_VAR"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `extractFromJavascriptStep creates step with type Extract From Javascript and params of type ExtractParams`() {
        browserTest.extractFromJavascriptStep(
            stepName = "Step",
            code = "return 'true'",
            variableName = "EXTRACT_JS_STEP_VAR"
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT, step?.type)
        assertInstanceOf(ExtractParams::class.java, step?.params)
    }

    @Test
    fun `extractFromJavascriptStep adds the passed javascript code to the ExtractParams object`() {
        val jsCode = "return true;"
        browserTest.extractFromJavascriptStep(
            stepName = "Step",
            code = jsCode,
            variableName = "EXTRACT_JS_STEP_VAR"
        )

        assertEquals(jsCode, (browserTest.steps?.get(0)?.params as ExtractParams).code)
    }

    @Test
    fun `extractFromJavascriptStep adds the passed variable to the ExtractParams object`() {
        val variableName = "EXTRACT_JS_STEP_VAR"
        browserTest.extractFromJavascriptStep(
            stepName = "Step",
            code = "return 'true'",
            variableName = variableName
        )

        assertEquals(variableName, (browserTest.steps?.get(0)?.params as ExtractParams).variable?.name)
    }

    @Test
    fun `extractFromJavascriptStep accepts additional configuration changes to the test step`() {
        browserTest.extractFromJavascriptStep(
            stepName = "Step",
            code = "return 'true'",
            variableName = "EXTRACT_JS_STEP_VAR"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `extractTextFromElementStep adds new step to the browser test`() {
        browserTest.extractTextFromElementStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId"),
            variableName = "EXTRACT_TEXT_VAR"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `extractTextFromElementStep creates step with type Extract From Javascript and params of type ExtractParams`() {
        browserTest.extractTextFromElementStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId"),
            variableName = "EXTRACT_TEXT_VAR"
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.EXTRACT_VARIABLE, step?.type)
        assertInstanceOf(ExtractParams::class.java, step?.params)
    }

    @Test
    fun `extractTextFromElementStep adds the passed variable name to the ExtractParams object`() {
        val variableName = "EXTRACT_TEXT_VAR"
        browserTest.extractTextFromElementStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId"),
            variableName = variableName
        )

        assertEquals(variableName, (browserTest.steps?.get(0)?.params as ExtractParams).variable?.name)
    }

    @Test
    fun `extractTextFromElementStep adds the passed target element to the ExtractParams object`() {
        val targetElement = TargetElement("#locator")
        browserTest.extractTextFromElementStep(
            stepName = "Step",
            targetElement = targetElement,
            variableName = "EXTRACT_TEXT_VAR"
        )

        assertEquals(targetElement.getElementObject(), (browserTest.steps?.get(0)?.params as ExtractParams).element)
    }

    @Test
    fun `extractTextFromElementStep accepts additional configuration changes to the test step`() {
        browserTest.extractTextFromElementStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId"),
            variableName = "EXTRACT_TEXT_VAR"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }
}
