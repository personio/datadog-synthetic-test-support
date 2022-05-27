package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType

/**
 * Adds a new element present assertion step to the synthetic browser test
 * @return SyntheticsStep object with the element present assertion step added
 */
fun BrowserTest.elementPresentAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_ELEMENT_PRESENT
        params = AssertionParams()
    }

/**
 * Adds a new element content assertion step to the synthetic browser test
 * @return SyntheticsStep object with the element content assertion step added
 */
fun BrowserTest.elementContentAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_ELEMENT_CONTENT
        params = AssertionParams()
    }

/**
 * Adds a new element attribute assertion step to the synthetic browser test
 * @return SyntheticsStep object with the element attribute assertion step added
 */
fun BrowserTest.elementAttributeAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_ELEMENT_ATTRIBUTE
        params = AssertionParams()
    }

/**
 * Sets the attribute to be checked for the element attribute assertion step
 * @param attributeName Name of the attribute to be checked for the element attribute assertion step
 * @return SyntheticsStep object with attribute param set
 */
fun SyntheticsStep.attribute(attributeName: String) = apply {
    params = withParamType<AssertionParams> {
        copy(attribute = attributeName)
    }
}
