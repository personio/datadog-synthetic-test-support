package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.element.Element
import com.personio.synthetics.model.element.UserLocator
import com.personio.synthetics.model.element.Value

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
        else -> throw IllegalArgumentException("Cannot use targetElement on params $p")
    }
}
