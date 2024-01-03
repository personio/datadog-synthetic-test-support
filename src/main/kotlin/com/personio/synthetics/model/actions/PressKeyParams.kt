package com.personio.synthetics.model.actions

import com.personio.synthetics.model.Params

internal data class PressKeyParams(
    val value: String? = null,
    val modifiers: List<String> = listOf(),
) : Params()
