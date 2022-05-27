package com.personio.synthetics.step

import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.element.Element
import com.personio.synthetics.model.element.ElementForSpecialActions
import com.personio.synthetics.model.element.UserLocator
import com.personio.synthetics.model.element.Value
import com.personio.synthetics.model.javascript.JSParams
import org.intellij.lang.annotations.Language

/**
 * Sets the target element for the UI components
 * @param locator The locator for identifying the element
 * @param locatorType The type of locator used for identifying the element (Optional parameter)
 * Default value is CSS
 * @return SyntheticsStep object with the target element set
 */
fun SyntheticsStep.targetElement(locator: String, locatorType: LocatorType = LocatorType.CSS) = apply {
    val userLocator = UserLocator(
        values = listOf(Value(locatorType.value, locator))
    )
    params = when (val p = params) {
        is ActionsParams -> p.copy(element = Element(userLocator = userLocator))
        is AssertionParams -> p.copy(element = Element(userLocator = userLocator))
        is SpecialActionsParams -> p.copy(element = ElementForSpecialActions(userLocator = userLocator))
        else -> throw IllegalArgumentException("Cannot use targetElement on params $p")
    }
}

/**
 * Pass the code to be executed in the extract from JS step or custom JS assertion step
 * @param code JS code to be executed. The code should return a value. `console.error` logs will appear in the test results.
 * @return SyntheticsStep object with the code to execute set
 */
fun SyntheticsStep.code(@Language("JS") code: String) = apply {
    params = when (val p = params) {
        is JSParams -> p.copy(code = code)
        is AssertionParams -> p.copy(code = code)
        else -> throw IllegalArgumentException("Cannot use code on params $p")
    }
}
