package com.personio.synthetics.step.assertion

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.ui.model.TargetElement

/**
 * Adds a new assertion step for testing that an element is present to the synthetic browser test
 * @param stepName Name of the step
 * @param targetElement The element where the assertion is to be performed
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with elementPresentAssertion added
 */
fun BrowserTest.elementPresentAssertion(
    stepName: String,
    targetElement: TargetElement,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.ASSERT_ELEMENT_PRESENT
    params = AssertionParams(
        element = targetElement.getElementObject()
    )
    if (f != null) f()
}

/**
 * Adds a new assertion step for testing element's content to the synthetic browser test
 * @param stepName Name of the step
 * @param targetElement The element where the assertion is to be performed
 * @param check The type of check to be done on the element content
 * @param expectedContent The expected content for the element (optional)
 * The expectedValue does not need to be passed when the check is IS_EMPTY or NOT_IS_EMPTY
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with elementContentAssertion added
 */
fun BrowserTest.elementContentAssertion(
    stepName: String,
    targetElement: TargetElement,
    check: SyntheticsCheckType,
    expectedContent: String = "",
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    if (check !in listOf(SyntheticsCheckType.IS_EMPTY, SyntheticsCheckType.NOT_IS_EMPTY)) {
        check(!expectedContent.isNullOrEmpty()) { "Expected content is a required parameter for the passed check type $check in the step: $stepName." }
    }
    type = SyntheticsStepType.ASSERT_ELEMENT_CONTENT
    params = AssertionParams(
        element = targetElement.getElementObject(),
        check = check,
        value = expectedContent
    )
    if (f != null) f()
}

/**
 * Adds a new assertion step for testing element's attribute to the synthetic browser test
 * @param stepName Name of the step
 * @param attribute The attribute of the element where the assertion needs to be performed
 * @param targetElement The web element where the assertion is to be performed
 * @param check The type of check to be done on the element attribute
 * @param expectedValue The expected attribute content for the element (optional)
 * The expectedValue does not need to be passed when the check is IS_EMPTY or NOT_IS_EMPTY
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with elementAttributeAssertion added
 */
fun BrowserTest.elementAttributeAssertion(
    stepName: String,
    attribute: String,
    targetElement: TargetElement,
    check: SyntheticsCheckType,
    expectedValue: String = "",
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    if (check !in listOf(SyntheticsCheckType.IS_EMPTY, SyntheticsCheckType.NOT_IS_EMPTY)) {
        check(!expectedValue.isNullOrEmpty()) { "Expected value is a required parameter for the passed check type $check in the step: $stepName." }
    }
    type = SyntheticsStepType.ASSERT_ELEMENT_ATTRIBUTE
    params = AssertionParams(
        attribute = attribute,
        check = check,
        value = expectedValue,
        element = targetElement.getElementObject()
    )
    if (f != null) f()
}
