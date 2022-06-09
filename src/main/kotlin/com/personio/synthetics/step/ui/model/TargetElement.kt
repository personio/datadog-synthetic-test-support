package com.personio.synthetics.step.ui.model

import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.element.UserLocator
import com.personio.synthetics.model.element.Value

class TargetElement {
    lateinit var locator: String
    var locatorType: LocatorType = LocatorType.CSS

    @PublishedApi internal fun userLocator(): UserLocator =
        UserLocator(values = listOf(Value(locatorType.value, locator)))
}
