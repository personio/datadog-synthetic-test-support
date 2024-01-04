package com.personio.synthetics.step.ui.model

import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.element.Element
import com.personio.synthetics.model.element.ElementForSpecialActions
import com.personio.synthetics.model.element.UserLocator
import com.personio.synthetics.model.element.Value

data class TargetElement(
    val locator: String,
    val locatorType: LocatorType = LocatorType.CSS,
) {
    private fun userLocator(): UserLocator = UserLocator(values = listOf(Value(locatorType.value, locator)))

    @PublishedApi
    internal fun getElementObject(): Element {
        return Element(userLocator = userLocator())
    }

    @PublishedApi
    internal fun getSpecialActionsElementObject(): ElementForSpecialActions {
        return ElementForSpecialActions(userLocator = userLocator())
    }
}
