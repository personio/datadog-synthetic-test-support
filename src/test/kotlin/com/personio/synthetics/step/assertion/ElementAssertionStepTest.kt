package com.personio.synthetics.step.assertion

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock

internal class ElementAssertionStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `elementPresentAssertion adds the new step item to the browser test object`() {
        browserTest.elementPresentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator")
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `elementPresentAssertion adds the step item of type Test that an element is present`() {
        browserTest.elementPresentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator")
        )

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_PRESENT, browserTest.steps!![0].type)
    }

    @Test
    fun `elementPresentAssertion adds AssertionParams to the browser test object`() {
        browserTest.elementPresentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator")
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `elementPresentAssertion adds the passed target element to the params object`() {
        val targetElement = TargetElement("#locator")
        browserTest.elementPresentAssertion(
            stepName = "Step",
            targetElement = targetElement
        )

        assertEquals(targetElement.getElementObject(), (browserTest.steps!![0].params as AssertionParams).element)
    }

    @Test
    fun `elementPresentAssertion accepts additional configuration changes to the test step`() {
        browserTest.elementPresentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator")
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `elementContentAssertion adds the new step item to the browser test object`() {
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `elementContentAssertion adds the step item of type Test an element's content`() {
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_CONTENT, browserTest.steps!![0].type)
    }

    @Test
    fun `elementContentAssertion adds AssertionParams to the browser test object`() {
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `elementContentAssertion adds passed target element to the params object`() {
        val targetElement = TargetElement("#locator")
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = targetElement,
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        )

        assertEquals(targetElement.getElementObject(), (browserTest.steps!![0].params as AssertionParams).element)
    }

    @Test
    fun `elementContentAssertion adds passed check type to the params object`() {
        val checkType = SyntheticsCheckType.EQUALS
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            check = checkType,
            expectedContent = "expectedContent"
        )

        assertEquals(checkType, (browserTest.steps!![0].params as AssertionParams).check)
    }

    @Test
    fun `elementContentAssertion adds passed expected content to the params object`() {
        val expectedContent = "content"
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            check = SyntheticsCheckType.EQUALS,
            expectedContent = expectedContent
        )

        assertEquals(expectedContent, (browserTest.steps!![0].params as AssertionParams).value)
    }

    @Test
    fun `elementContentAssertion doesn't throw exception if expected content is not passed and check type is IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.elementContentAssertion(
                stepName = "Step",
                targetElement = TargetElement("#locator"),
                check = SyntheticsCheckType.IS_EMPTY
            )
        }
    }

    @Test
    fun `elementContentAssertion doesn't throw exception if expected content is not passed and check type is NOT_IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.elementContentAssertion(
                stepName = "Step",
                targetElement = TargetElement("#locator"),
                check = SyntheticsCheckType.NOT_IS_EMPTY
            )
        }
    }

    @Test
    fun `elementContentAssertion throw exception if expected content is not passed and check type is not IS_EMPTY or NOT_IS_EMPTY`() {
        assertThrows<IllegalStateException> {
            browserTest.elementContentAssertion(
                stepName = "Step",
                targetElement = TargetElement("#locator"),
                check = SyntheticsCheckType.EQUALS
            )
        }
    }

    @Test
    fun `elementContentAssertion accepts additional configuration changes to the test step`() {
        browserTest.elementContentAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            check = SyntheticsCheckType.EQUALS,
            expectedContent = "expectedContent"
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `elementAttributeAssertion adds the new step item to the browser test object`() {
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = "attribute",
            check = SyntheticsCheckType.EQUALS,
            expectedValue = "expectedValue"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `elementAttributeAssertion adds the step item of type Test an element's attribute`() {
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = "attribute",
            check = SyntheticsCheckType.EQUALS,
            expectedValue = "expectedValue"
        )

        assertEquals(SyntheticsStepType.ASSERT_ELEMENT_ATTRIBUTE, browserTest.steps!![0].type)
    }

    @Test
    fun `elementAttributeAssertion adds AssertionParams to the browser test object`() {
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = "attribute",
            check = SyntheticsCheckType.EQUALS,
            expectedValue = "expectedValue"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `elementAttributeAssertion adds the passed target element to the params object`() {
        val targetElement = TargetElement("#locator")
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = targetElement,
            attribute = "attribute",
            check = SyntheticsCheckType.EQUALS,
            expectedValue = "expectedValue"
        )

        assertEquals(targetElement.getElementObject(), (browserTest.steps!![0].params as AssertionParams).element)
    }

    @Test
    fun `elementAttributeAssertion adds the passed attribute to the params object`() {
        val attribute = "attribute"
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = attribute,
            check = SyntheticsCheckType.EQUALS,
            expectedValue = "expectedValue"
        )

        assertEquals(attribute, (browserTest.steps!![0].params as AssertionParams).attribute)
    }

    @Test
    fun `elementAttributeAssertion adds the passed check to the params object`() {
        val checkType = SyntheticsCheckType.EQUALS
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = "attribute",
            check = checkType,
            expectedValue = "expectedValue"
        )

        assertEquals(checkType, (browserTest.steps!![0].params as AssertionParams).check)
    }

    @Test
    fun `elementAttributeAssertion adds the passed expected value to the params object`() {
        val expectedValue = "expectedValue"
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = "attribute",
            check = SyntheticsCheckType.EQUALS,
            expectedValue = expectedValue
        )

        assertEquals(expectedValue, (browserTest.steps!![0].params as AssertionParams).value)
    }

    @Test
    fun `elementAttributeAssertion doesn't throw exception if expected value is not passed and check type is IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.elementAttributeAssertion(
                stepName = "Step",
                targetElement = TargetElement("#locator"),
                attribute = "attribute",
                check = SyntheticsCheckType.IS_EMPTY
            )
        }
    }

    @Test
    fun `elementAttributeAssertion doesn't throw exception if expected value is not passed and check type is NOT_IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.elementAttributeAssertion(
                stepName = "Step",
                targetElement = TargetElement("#locator"),
                attribute = "attribute",
                check = SyntheticsCheckType.NOT_IS_EMPTY
            )
        }
    }

    @Test
    fun `elementAttributeAssertion throws exception if expected value is not passed and check type is neither "IS_EMPTY" nor "NOT_IS_EMPTY"`() {
        assertThrows<IllegalStateException> {
            browserTest.elementAttributeAssertion(
                stepName = "Step",
                targetElement = TargetElement("#locator"),
                attribute = "attribute",
                check = SyntheticsCheckType.EQUALS
            )
        }
    }

    @Test
    fun `elementAttributeAssertion accepts additional configuration changes to the test step`() {
        browserTest.elementAttributeAssertion(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            attribute = "attribute",
            check = SyntheticsCheckType.EQUALS,
            expectedValue = "expectedValue"
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }
}
