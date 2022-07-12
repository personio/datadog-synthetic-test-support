package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.assertion.AssertionParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class SpecialAssertionStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `customJavascriptAssertion adds the new step item to the browser test object`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `customJavascriptAssertion adds the step item of type Test custom JavaScript assertion`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        )

        assertEquals(SyntheticsStepType.ASSERT_FROM_JAVASCRIPT, browserTest.steps!![0].type)
    }

    @Test
    fun `customJavascriptAssertion adds AssertionParams to the browser test object`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `customJavascriptAssertion adds the passed code to params object`() {
        val code = "return true;"
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = code
        )

        assertEquals(code, (browserTest.steps?.get(0)?.params as AssertionParams).code)
    }

    @Test
    fun `customJavascriptAssertion accepts additional configuration changes to the test step`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }
}
