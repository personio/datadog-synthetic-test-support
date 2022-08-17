package com.personio.synthetics.step.assertion

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.waitBeforeDeclaringStepAsFailed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import kotlin.time.Duration.Companion.seconds

internal class PageAssertionStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `currentUrlAssertion adds the new step item to the browser test object`() {
        browserTest.currentUrlAssertion(
            stepName = "Step",
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `currentUrlAssertion adds the step item of type Test the content of the URL of the active page`() {
        browserTest.currentUrlAssertion(
            stepName = "Step",
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertEquals(SyntheticsStepType.ASSERT_CURRENT_URL, browserTest.steps!![0].type)
    }

    @Test
    fun `currentUrlAssertion adds AssertionParams to the browser test object`() {
        browserTest.currentUrlAssertion(
            stepName = "Step",
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `currentUrlAssertion adds passed check type to the params object`() {
        val checkType = SyntheticsCheckType.EQUALS
        browserTest.currentUrlAssertion(
            stepName = "Step",
            check = checkType,
            expectedContent = "expectedContent"
        )

        assertEquals(checkType, (browserTest.steps!![0].params as AssertionParams).check)
    }

    @Test
    fun `currentUrlAssertion adds passed expected content to the params object`() {
        val expectedContent = "expectedContent"
        browserTest.currentUrlAssertion(
            stepName = "Step",
            check = SyntheticsCheckType.EQUALS,
            expectedContent = expectedContent
        )

        assertEquals(expectedContent, (browserTest.steps!![0].params as AssertionParams).value)
    }

    @Test
    fun `currentUrlAssertion doesn't throw exception if expected content is not passed and check type is IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.currentUrlAssertion(
                stepName = "Step",
                check = SyntheticsCheckType.IS_EMPTY,
            )
        }
    }

    @Test
    fun `currentUrlAssertion doesn't throw exception if expected content is not passed and check type is NOT_IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.currentUrlAssertion(
                stepName = "Step",
                check = SyntheticsCheckType.NOT_IS_EMPTY,
            )
        }
    }

    @Test
    fun `currentUrlAssertion throw exception if expected content is not passed and check type is neither "IS_EMPTY" nor "NOT_IS_EMPTY"`() {
        assertThrows<IllegalStateException> {
            browserTest.currentUrlAssertion(
                stepName = "Step",
                check = SyntheticsCheckType.EQUALS,
            )
        }
    }

    @Test
    fun `currentUrlAssertion accepts additional configuration changes to the test step`() {
        browserTest.currentUrlAssertion(
            stepName = "Step",
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `pageContainsTextAssertion adds the new step item to the browser test object`() {
        browserTest.pageContainsTextAssertion(
            stepName = "Step",
            expectedText = "expectedText"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `pageContainsTextAssertion adds the step item of type Test that some text is present on the active page`() {
        browserTest.pageContainsTextAssertion(
            stepName = "Step",
            expectedText = "expectedText"
        )

        assertEquals(SyntheticsStepType.ASSERT_PAGE_CONTAINS, browserTest.steps!![0].type)
    }

    @Test
    fun `pageContainsTextAssertion adds AssertionParams to the browser test object`() {
        browserTest.pageContainsTextAssertion(
            stepName = "Step",
            expectedText = "expectedText"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `pageContainsTextAssertion adds passed expected text to the params object`() {
        val expectedText = "expectedText"
        browserTest.pageContainsTextAssertion(
            stepName = "Step",
            expectedText = expectedText
        )

        assertEquals(expectedText, (browserTest.steps!![0].params as AssertionParams).value)
    }

    @Test
    fun `pageContainsTextAssertion accepts additional configuration changes to the test step`() {
        browserTest.pageContainsTextAssertion(
            stepName = "Step",
            expectedText = "expectedText"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `pageNotContainsTextAssertion adds the new step item to the browser test object`() {
        browserTest.pageNotContainsTextAssertion(
            stepName = "Step",
            text = "text"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `pageNotContainsTextAssertion adds the step item of type Test that some text is not present on the active page`() {
        browserTest.pageNotContainsTextAssertion(
            stepName = "Step",
            text = "text"
        )

        assertEquals(SyntheticsStepType.ASSERT_PAGE_LACKS, browserTest.steps!![0].type)
    }

    @Test
    fun `pageNotContainsTextAssertion adds AssertionParams to the browser test object`() {
        browserTest.pageNotContainsTextAssertion(
            stepName = "Step",
            text = "text"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `pageNotContainsTextAssertion adds passed text to the params object`() {
        val text = "text"
        browserTest.pageNotContainsTextAssertion(
            stepName = "Step",
            text = text
        )

        assertEquals(text, (browserTest.steps!![0].params as AssertionParams).value)
    }

    @Test
    fun `pageNotContainsTextAssertion accepts additional configuration changes to the test step`() {
        browserTest.pageNotContainsTextAssertion(
            stepName = "Step",
            text = "text"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }
}
