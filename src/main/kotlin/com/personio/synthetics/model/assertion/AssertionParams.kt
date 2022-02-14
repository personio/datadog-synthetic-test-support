package com.personio.synthetics.model.assertion

import com.personio.synthetics.model.ElementParams
import com.personio.synthetics.model.element.Element

internal data class AssertionParams(
    var attribute: String? = null,
    var check: String? = null,
    override var element: Element? = null
) : ElementParams
