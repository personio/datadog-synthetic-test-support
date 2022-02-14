package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.assertion.AssertionType

/**
 * Adds a new assertion step to the synthetic browser test
 * @param type Type of assertion for the step
 * @return SyntheticsStep with the assertion step added
 */
fun BrowserTest.assertionStep(type: AssertionType): SyntheticsStep {
    val assertionStep = SyntheticsStep()
        .type(SyntheticsStepType.fromValue(type.value))
        .params(AssertionParams())
    addStepsItem(assertionStep)
    return assertionStep
}
