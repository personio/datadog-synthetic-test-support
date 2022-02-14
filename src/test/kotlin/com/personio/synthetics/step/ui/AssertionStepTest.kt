package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.api.SyntheticsApi
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.assertion.AssertionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class AssertionStepTest {
    private val syntheticsApi = Mockito.mock(SyntheticsApi::class.java)
    private val browserTest = BrowserTest(syntheticsApi)

    @Test
    fun `assertionStep adds the new step item to the browser test object`() {
        browserTest.assertionStep(AssertionType.ELEMENT_PRESENT)

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `assertionStep adds AssertionParams to the browser test object`() {
        browserTest.assertionStep(AssertionType.ELEMENT_PRESENT)

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `assertionStep adds the step item of type passed as argument`() {
        browserTest.assertionStep(AssertionType.ELEMENT_PRESENT)

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_PRESENT, browserTest.steps!![0].type)
    }
}
