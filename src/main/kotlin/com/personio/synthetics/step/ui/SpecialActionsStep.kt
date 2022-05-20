package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType
import kotlin.time.Duration

/**
 * Adds a wait step to the synthetic browser test with a default value as 1 second
 * @return SyntheticsStep object with the wait step added
 */
fun BrowserTest.waitStep(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.WAIT
        params = WaitParams()
    }

/**
 * Sets the waiting time to be sent for the wait step
 * @param value to be sent for the wait step
 * @return SyntheticsStep object with waiting time value set
 */
fun SyntheticsStep.waitingTime(value: Duration) = apply {
    params = withParamType<WaitParams> {
        copy(value = value.inWholeSeconds.toInt())
    }
}

/**
 * Adds a scroll step to the synthetic browser test
 * @return SyntheticsStep object with the scroll step added
 */
fun BrowserTest.scrollStep(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.SCROLL
        params = SpecialActionsParams()
    }

/**
 * Sets the horizontal scroll value to be sent for the scroll step
 * @param x to be sent for the scroll step
 * @return SyntheticsStep object with horizontal scroll value set
 */
fun SyntheticsStep.horizontalScroll(x: Int) = apply {
    params = withParamType<SpecialActionsParams> {
        copy(x = x)
    }
}

/**
 * Sets the vertical scroll value to be sent for the scroll step
 * @param y to be sent for the scroll step
 * @return SyntheticsStep object with vertical scroll value set
 */
fun SyntheticsStep.verticalScroll(y: Int) = apply {
    params = withParamType<SpecialActionsParams> {
        copy(y = y)
    }
}

/**
 * Adds a hover step to the synthetic browser test
 * @return SyntheticsStep object with the hover step added
 */
fun BrowserTest.hoverStep(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.HOVER
        params = SpecialActionsParams()
    }

/**
 * Adds a press key step to the synthetic browser test
 * @return SyntheticsStep object with the press key step added
 */
fun BrowserTest.pressKeyStep(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.PRESS_KEY
        params = PressKeyParams()
    }

/**
 * Sets the value to be sent for the press key step
 * @param value to be sent for the press key step
 * @return SyntheticsStep object with press key value set
 */
fun SyntheticsStep.key(value: String) = apply {
    params = withParamType<PressKeyParams> {
        copy(value = value)
    }
}

/**
 * Sets the modifier to be sent for the press key step
 * @param modifier to be sent for the press key step
 * @return SyntheticsStep object with press key value set
 */
fun SyntheticsStep.addModifier(modifier: Modifier) = apply {
    params = withParamType<PressKeyParams> {
        copy(modifiers = modifiers.plus(modifier.value))
    }
}
