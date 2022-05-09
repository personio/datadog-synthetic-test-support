package com.personio.synthetics.model.assertion

import com.personio.synthetics.model.element.Element

internal data class AssertionParams(
    val attribute: String? = null,
    val check: String? = null,
    val element: Element? = null
)
