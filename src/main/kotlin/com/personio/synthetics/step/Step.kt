package com.personio.synthetics.step

import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.model.Params
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.element.Element
import com.personio.synthetics.model.element.ElementForSpecialActions
import com.personio.synthetics.model.element.UserLocator
import com.personio.synthetics.model.javascript.JSParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.intellij.lang.annotations.Language

/**
 * The base class for Step
 * Contains the common functions used for the synthetic steps
 */
open class Step : SyntheticsStep() {
    /**
     * Sets the target element for the UI components
     * @param f Add the locator details for the element
     * By default, the locator type is CSS
     * @return Step object with the target element set
     */
    inline fun targetElement(f: TargetElement.() -> Unit) = apply {
        val targetElement = TargetElement()
        targetElement.f()
        params = getParams(targetElement.userLocator())
    }

    /**
     * Pass the code to be executed in the extract from JS step or custom JS assertion step
     * @param code JS code to be executed. The code should return a value. `console.error` logs will appear in the test results.
     * @return Step object with the code to execute set
     */
    fun code(@Language("JS") code: String) = apply {
        params = when (val p = params) {
            is JSParams -> p.copy(code = code)
            is AssertionParams -> p.copy(code = code)
            else -> throw IllegalArgumentException("Cannot use code on params $p")
        }
    }

    @PublishedApi
    internal fun getParams(userLocator: UserLocator): Params {
        return when (val p = params) {
            is ActionsParams -> p.copy(element = Element(userLocator = userLocator))
            is AssertionParams -> p.copy(element = Element(userLocator = userLocator))
            is SpecialActionsParams -> p.copy(element = ElementForSpecialActions(userLocator = userLocator))
            else -> throw IllegalArgumentException("Cannot use targetElement on params $p")
        }
    }
}
