package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.assertion.AssertionParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class PageAssertionStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `currentUrlAssertion adds the new step item to the browser test object`() {
        browserTest.currentUrlAssertion()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `currentUrlAssertion adds the step item of type Test the content of the URL of the active page`() {
        browserTest.currentUrlAssertion()

        assertEquals(SyntheticsStepType.ASSERT_CURRENT_URL, browserTest.steps!![0].type)
    }

    @Test
    fun `currentUrlAssertion adds AssertionParams to the browser test object`() {
        browserTest.currentUrlAssertion()

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `pageContainsTextAssertion adds the new step item to the browser test object`() {
        browserTest.pageContainsTextAssertion()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `pageContainsTextAssertion adds the step item of type Test that some text is present on the active page`() {
        browserTest.pageContainsTextAssertion()

        assertEquals(SyntheticsStepType.ASSERT_PAGE_CONTAINS, browserTest.steps!![0].type)
    }

    @Test
    fun `pageContainsTextAssertion adds AssertionParams to the browser test object`() {
        browserTest.pageContainsTextAssertion()

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `pageNotContainsTextAssertion adds the new step item to the browser test object`() {
        browserTest.pageNotContainsTextAssertion()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `pageNotContainsTextAssertion adds the step item of type Test that some text is not present on the active page`() {
        browserTest.pageNotContainsTextAssertion()

        assertEquals(SyntheticsStepType.ASSERT_PAGE_LACKS, browserTest.steps!![0].type)
    }

    @Test
    fun `pageNotContainsTextAssertion adds AssertionParams to the browser test object`() {
        browserTest.pageNotContainsTextAssertion()

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }
}
