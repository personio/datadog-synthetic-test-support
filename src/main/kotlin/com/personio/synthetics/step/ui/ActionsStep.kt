package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.BrowserTest
import com.personio.synthetics.model.actions.ActionsParams

private const val DEFAULT_TEXT_DELAY: Long = 25 // in milliseconds

/**
 * Adds a new input text step to the synthetic browser test
 * @return SyntheticsStep object with the input text step added
 */
fun BrowserTest.inputTextStep(): SyntheticsStep {
    val textStep = SyntheticsStep()
        .type(SyntheticsStepType.TYPE_TEXT)
        .params(ActionsParams(delay = DEFAULT_TEXT_DELAY))
    addStepsItem(textStep)
    return textStep
}

/**
 * Adds a new click step to the synthetic browser test
 * @return SyntheticsStep object with the click step added
 */
fun BrowserTest.clickStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.CLICK)
        .params(ActionsParams())
    addStepsItem(step)
    return step
}

/**
 * Sets the text to be sent for the input text step
 * @param value Text to be sent for the input text step
 * @return SyntheticsStep object with text value set
 */
fun SyntheticsStep.text(value: String): SyntheticsStep {
    val textParams = params as ActionsParams
    textParams.value = value
    return this
}
