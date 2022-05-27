package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.assertion.AssertionParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class GenericAssertionStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `expectedValue adds the passed string value to the AssertionParams object`() {
        browserTest
            .pageContainsTextAssertion()
            .expectedValue("expected value")
        val params = browserTest.steps?.get(0)?.params as AssertionParams

        assertEquals("expected value", params.value)
    }

    @Test
    fun `check adds the passed check type to the AssertionParams object`() {
        browserTest
            .currentUrlAssertion()
            .check(SyntheticsCheckType.EQUALS)
        val params = browserTest.steps?.get(0)?.params as AssertionParams

        assertEquals(SyntheticsCheckType.EQUALS, params.check)
    }
}
