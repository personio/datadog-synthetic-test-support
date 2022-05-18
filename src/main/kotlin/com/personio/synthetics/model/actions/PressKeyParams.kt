package com.personio.synthetics.model.actions

internal data class PressKeyParams(
    val value: String = "Enter",
    val modifiers: List<String> = listOf()
)
