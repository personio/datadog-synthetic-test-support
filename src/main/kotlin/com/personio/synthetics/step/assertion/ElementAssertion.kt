package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType

/**
 * Adds a new element present assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return ElementAssertionStep object with the element present assertion step added
 */
fun BrowserTest.elementPresentAssertion(stepName: String, f: ElementAssertionStep.() -> Unit): ElementAssertionStep =
    addStep(stepName, ElementAssertionStep()) {
        type = SyntheticsStepType.ASSERT_ELEMENT_PRESENT
        params = AssertionParams()
        f()
    }

/**
 * Adds a new element content assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return ElementAssertionStep object with the element content assertion step added
 */
fun BrowserTest.elementContentAssertion(stepName: String, f: ElementAssertionStep.() -> Unit): ElementAssertionStep =
    addStep(stepName, ElementAssertionStep()) {
        type = SyntheticsStepType.ASSERT_ELEMENT_CONTENT
        params = AssertionParams()
        f()
    }

/**
 * Adds a new element attribute assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return ElementAssertionStep object with the element attribute assertion step added
 */
fun BrowserTest.elementAttributeAssertion(stepName: String, f: ElementAssertionStep.() -> Unit): ElementAssertionStep =
    addStep(stepName, ElementAssertionStep()) {
        type = SyntheticsStepType.ASSERT_ELEMENT_ATTRIBUTE
        params = AssertionParams()
        f()
    }

/**
 * Configure the Element assertion step for the synthetic browser test
 */
class ElementAssertionStep : AssertionStep() {
    /**
     * Sets the attribute to be checked for the element attribute assertion step
     * @param attributeName Name of the attribute to be checked for the element attribute assertion step
     * @return ElementAssertionStep object with attribute param set
     */
    fun attribute(attributeName: String) = apply {
        params = withParamType<AssertionParams> {
            copy(attribute = attributeName)
        }
    }
}
