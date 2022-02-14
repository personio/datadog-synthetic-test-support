package com.personio.synthetics.model.element

import com.personio.synthetics.model.actions.MultiLocator

internal data class Element(
    var multiLocator: MultiLocator = MultiLocator(),
    var targetOuterHTML: String = "",
    var userLocator: UserLocator = UserLocator()
)
