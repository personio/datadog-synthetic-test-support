package com.personio.synthetics.model.element

import com.personio.synthetics.model.actions.MultiLocator

internal data class Element(
    val multiLocator: MultiLocator = MultiLocator(),
    val targetOuterHTML: String = "",
    val userLocator: UserLocator = UserLocator(),
)
