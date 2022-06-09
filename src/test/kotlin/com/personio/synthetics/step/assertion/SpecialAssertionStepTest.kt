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
        browserTest.customJavascriptAssertion("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `customJavascriptAssertion adds the step item of type Test custom JavaScript assertion`() {
        browserTest.customJavascriptAssertion("Step") {}

        assertEquals(SyntheticsStepType.ASSERT_FROM_JAVASCRIPT, browserTest.steps!![0].type)
    }

    @Test
    fun `customJavascriptAssertion adds AssertionParams to the browser test object`() {
        browserTest.customJavascriptAssertion("Step") {}

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `code adds the passed javascript code to the AssertionParams object`() {
        browserTest
            .customJavascriptAssertion("Step") {}
            .code("return true;")
        val params = browserTest.steps?.get(0)?.params as AssertionParams

        assertEquals("return true;", params.code)
    }
}
