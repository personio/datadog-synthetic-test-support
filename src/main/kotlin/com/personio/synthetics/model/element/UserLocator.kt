package com.personio.synthetics.model.element

internal data class UserLocator(
    var failTestOnCannotLocate: Boolean? = true,
    var values: List<Value>? = listOf()
)
