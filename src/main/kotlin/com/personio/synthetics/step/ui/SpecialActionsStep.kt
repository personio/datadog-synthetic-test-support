package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import kotlin.time.Duration

/**
 * Adds a wait step to the synthetic browser test with a default value as 1 second
 * @return SyntheticsStep object with the wait step added
 */
fun BrowserTest.waitStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.WAIT)
        .params(WaitParams())
    addStepsItem(step)
    return step
}

/**
 * Sets the waiting time to be sent for the wait step
 * @param value to be sent for the wait step
 * @return SyntheticsStep object with waiting time value set
 */
fun SyntheticsStep.waitingTime(value: Duration) = apply {
    params = with(params as? WaitParams ?: throw IllegalArgumentException("Cannot use waiting time on params $params")) {
        copy(value = value.inWholeSeconds.toInt())
    }
}

/**
 * Adds a scroll step to the synthetic browser test
 * @return SyntheticsStep object with the scroll step added
 */
fun BrowserTest.scrollStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.SCROLL)
        .params(SpecialActionsParams())
    addStepsItem(step)
    return step
}

/**
 * Sets the horizontal scroll value to be sent for the scroll step
 * @param x to be sent for the scroll step
 * @return SyntheticsStep object with horizontal scroll value set
 */
fun SyntheticsStep.horizontalScroll(x: Int) = apply {
    params = (params as? SpecialActionsParams ?: throw IllegalArgumentException("Cannot use horizontalScroll on params $params"))
        .copy(x = x)
}

/**
 * Sets the vertical scroll value to be sent for the scroll step
 * @param y to be sent for the scroll step
 * @return SyntheticsStep object with vertical scroll value set
 */
fun SyntheticsStep.verticalScroll(y: Int) = apply {
    params = (params as? SpecialActionsParams ?: throw IllegalArgumentException("Cannot use verticalScroll on params $params"))
        .copy(y = y)
}

/**
 * Adds a hover step to the synthetic browser test
 * @return SyntheticsStep object with the hover step added
 */
fun BrowserTest.hoverStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.HOVER)
        .params(SpecialActionsParams())
    addStepsItem(step)
    return step
}

/**
 * Adds a press key step to the synthetic browser test
 * @return SyntheticsStep object with the press key step added
 */
fun BrowserTest.pressKeyStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.PRESS_KEY)
        .params(PressKeyParams())
    addStepsItem(step)
    return step
}

/**
 * Sets the value to be sent for the press key step
 * @param value to be sent for the press key step
 * @return SyntheticsStep object with press key value set
 */
fun SyntheticsStep.key(value: String) = apply {
    params = (params as? PressKeyParams ?: throw IllegalArgumentException("Cannot use key on params $params"))
        .copy(value = value)
}

/**
 * Sets the modifier to be sent for the press key step
 * @param modifier to be sent for the press key step
 * @return SyntheticsStep object with press key value set
 */
fun SyntheticsStep.addModifier(modifier: Modifier) = apply {
    params = with(params as? PressKeyParams ?: throw IllegalArgumentException("Cannot use addModifier on params $params")) {
        copy(modifiers = modifiers.plus(modifier.value))
    }
}
