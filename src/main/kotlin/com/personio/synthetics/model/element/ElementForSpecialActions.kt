package com.personio.synthetics.model.element

internal data class ElementForSpecialActions(
    val html: String = "",
    val targetOuterHTML: String = "",
    val url: String = "",
    val userLocator: UserLocator = UserLocator()
)
