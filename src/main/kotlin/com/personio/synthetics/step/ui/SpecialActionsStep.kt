package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType
import kotlin.time.Duration

/**
 * Adds a wait step to the synthetic browser test with a default value as 1 second
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SpecialActionsStep object with the wait step added
 */
fun BrowserTest.waitStep(stepName: String, f: SpecialActionsStep.() -> Unit): SpecialActionsStep =
    addStep(stepName, SpecialActionsStep()) {
        type = SyntheticsStepType.WAIT
        params = WaitParams()
        f()
    }

/**
 * Adds a scroll step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SpecialActionsStep object with the scroll step added
 */
fun BrowserTest.scrollStep(stepName: String, f: SpecialActionsStep.() -> Unit): SpecialActionsStep =
    addStep(stepName, SpecialActionsStep()) {
        type = SyntheticsStepType.SCROLL
        params = SpecialActionsParams()
        f()
    }

/**
 * Adds a hover step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SpecialActionsStep object with the hover step added
 */
fun BrowserTest.hoverStep(stepName: String, f: SpecialActionsStep.() -> Unit): SpecialActionsStep =
    addStep(stepName, SpecialActionsStep()) {
        type = SyntheticsStepType.HOVER
        params = SpecialActionsParams()
        f()
    }

/**
 * Adds a press key step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SpecialActionsStep object with the press key step added
 */
fun BrowserTest.pressKeyStep(stepName: String, f: SpecialActionsStep.() -> Unit): SpecialActionsStep =
    addStep(stepName, SpecialActionsStep()) {
        type = SyntheticsStepType.PRESS_KEY
        params = PressKeyParams()
        f()
    }

/**
 * Configure the Special actions step for the synthetic browser test
 */
class SpecialActionsStep : Step() {
    /**
     * Sets the horizontal scroll value to be sent for the scroll step
     * @param x to be sent for the scroll step
     * @return SpecialActionsStep object with horizontal scroll value set
     */
    fun horizontalScroll(x: Int) = apply {
        params = withParamType<SpecialActionsParams> {
            copy(x = x)
        }
    }

    /**
     * Sets the vertical scroll value to be sent for the scroll step
     * @param y to be sent for the scroll step
     * @return SpecialActionsStep object with vertical scroll value set
     */
    fun verticalScroll(y: Int) = apply {
        params = withParamType<SpecialActionsParams> {
            copy(y = y)
        }
    }

    /**
     * Sets the waiting time to be sent for the wait step
     * @param value to be sent for the wait step (1 to 300 seconds)
     * @return SpecialActionsStep object with waiting time value set
     */
    fun waitingTime(value: Duration) = apply {
        params = withParamType<WaitParams> {
            copy(value = value.inWholeSeconds.toInt())
        }
    }

    /**
     * Sets the value to be sent for the press key step
     * @param key The key to be sent for the press key step
     * @return SpecialActionsStep object with press key value set
     */
    fun key(key: Key) = apply {
        params = withParamType<PressKeyParams> {
            copy(value = key.value)
        }
    }

    /**
     * Sets the modifier to be sent for the press key step
     * @param modifiers Pass comma separated modifiers to be sent for the press key step
     * @return SpecialActionsStep object with press key value set
     */
    fun modifiers(vararg modifiers: Modifier) = apply {
        params = withParamType<PressKeyParams> {
            val modifierValues = modifiers.map { it.value }.toList()
            copy(modifiers = modifierValues)
        }
    }
}
