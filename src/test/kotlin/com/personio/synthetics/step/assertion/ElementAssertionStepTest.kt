package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.assertion.AssertionParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class ElementAssertionStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `elementPresentAssertion adds the new step item to the browser test object`() {
        browserTest.elementPresentAssertion()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `elementPresentAssertion adds the step item of type Test that an element is present`() {
        browserTest.elementPresentAssertion()

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_PRESENT, browserTest.steps!![0].type)
    }

    @Test
    fun `elementPresentAssertion adds AssertionParams to the browser test object`() {
        browserTest.elementPresentAssertion()

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `elementContentAssertion adds the new step item to the browser test object`() {
        browserTest.elementContentAssertion()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `elementContentAssertion adds the step item of type Test an element's content`() {
        browserTest.elementContentAssertion()

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_CONTENT, browserTest.steps!![0].type)
    }

    @Test
    fun `elementContentAssertion adds AssertionParams to the browser test object`() {
        browserTest.elementContentAssertion()

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `elementAttributeAssertion adds the new step item to the browser test object`() {
        browserTest.elementAttributeAssertion()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `elementAttributeAssertion adds the step item of type Test an element's attribute`() {
        browserTest.elementAttributeAssertion()

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_ATTRIBUTE, browserTest.steps!![0].type)
    }

    @Test
    fun `elementAttributeAssertion adds AssertionParams to the browser test object`() {
        browserTest.elementAttributeAssertion()

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `attribute adds the passed attribute name to the AssertionParams object`() {
        browserTest
            .elementAttributeAssertion()
            .attribute("href")
        val params = browserTest.steps?.get(0)?.params as AssertionParams

        assertEquals("href", params.attribute)
    }
}
