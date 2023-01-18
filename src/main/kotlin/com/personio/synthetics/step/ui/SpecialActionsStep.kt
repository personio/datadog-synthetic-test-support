package com.personio.synthetics.step.ui

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.ui.model.TargetElement
import com.personio.synthetics.step.withParamType
import kotlin.time.Duration

/**
 * Adds a new wait step to the synthetic browser test with a default value as 1 second
 * @param stepName Name of the step
 * @param waitingTime The wait time to be passed for the step (1 to 300 seconds)
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with waitStep added
 */
fun BrowserTest.waitStep(
    stepName: String,
    waitingTime: Duration
) = addStep(stepName) {
    type = SyntheticsStepType.WAIT
    params = WaitParams(value = waitingTime.inWholeSeconds.toInt())
}

/**
 * Adds a new scroll step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add one of the following parameters required for this test step
 * - target element -> The web element where the scroll is to be performed to
 * - vertical and horizontal scroll -> Supply the pixels for performing the scroll
 * @return SpecialActionsStep object with scrollStep added
 */
fun BrowserTest.scrollStep(stepName: String, f: SpecialActionsStep.() -> Unit): SpecialActionsStep =
    addStep(stepName, SpecialActionsStep()) {
        type = SyntheticsStepType.SCROLL
        params = SpecialActionsParams()
        f()
        with(params as SpecialActionsParams) {
            check(
                (x in 0..9999 && y in 0..9999) || (element != null)
            ) { "Either set x,y coordinates within(0,9999) pixels or target element for step:'$stepName'." }
        }
    }

/**
 * Adds a new hover step to the synthetic browser test
 * @param stepName Name of the step
 * @param targetElement The web element to which the hover has to be performed
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with hoverStep added
 */
fun BrowserTest.hoverStep(
    stepName: String,
    targetElement: TargetElement,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.HOVER
    params = SpecialActionsParams(element = targetElement.getSpecialActionsElementObject())
    if (f != null) f()
}

/**
 * Adds a new press key step to the synthetic browser test
 * @param stepName Name of the step
 * @param key The key to be pressed
 * @param f Additional configurations that need to be added to the step such as modifiers, timeout and so on
 * @return SpecialActionsStep object with pressKeyStep added
 */
fun BrowserTest.pressKeyStep(
    stepName: String,
    key: Key,
    f: (SpecialActionsStep.() -> Unit)? = null
): SpecialActionsStep =
    addStep(stepName, SpecialActionsStep()) {
        type = SyntheticsStepType.PRESS_KEY
        params = PressKeyParams(value = key.value)
        if (f != null) f()
    }

/**
 * Configures the Special actions steps for the synthetic browser test
 */
class SpecialActionsStep : SyntheticsStep() {
    /**
     * Sets the horizontal scroll value to be sent for the scroll step
     * @param x The number of pixels to scroll horizontally
     * @return SpecialActionsStep object with horizontal scroll value set
     */
    fun horizontalScroll(x: Int) = apply {
        params = withParamType<SpecialActionsParams> {
            copy(x = x)
        }
    }

    /**
     * Sets the vertical scroll value to be sent for the scroll step
     * @param y The number of pixels to scroll vertically
     * @return SpecialActionsStep object with vertical scroll value set
     */
    fun verticalScroll(y: Int) = apply {
        params = withParamType<SpecialActionsParams> {
            copy(y = y)
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

    /**
     * Sets the target element for the scroll step
     * @param locator The locator of the web element
     * @param locatorType The locator type for identifying the element
     * By default the locator type is set as CSS
     */
    fun targetElement(locator: String, locatorType: LocatorType = LocatorType.CSS) {
        params = withParamType<SpecialActionsParams> {
            copy(element = TargetElement(locator, locatorType).getSpecialActionsElementObject())
        }
    }
}
