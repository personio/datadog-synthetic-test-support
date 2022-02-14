package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.model.ElementParams
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
fun SyntheticsStep.targetElement(locator: String, locatorType: LocatorType = LocatorType.CSS): SyntheticsStep {
    val userLocator = UserLocator(
        values = listOf(Value(locatorType.value, locator))
    )
    var stepParams: ElementParams = if (params is ActionsParams) {
        params as ActionsParams
    } else {
        params as AssertionParams
    }
    stepParams.element = Element()
    stepParams.element?.userLocator = userLocator
    return this
}
