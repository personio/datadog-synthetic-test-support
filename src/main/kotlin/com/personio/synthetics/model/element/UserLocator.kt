package com.personio.synthetics.model.element

internal data class UserLocator(
    val failTestOnCannotLocate: Boolean? = true,
    val values: List<Value>? = listOf(),
)
