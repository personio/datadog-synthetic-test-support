package com.personio.synthetics.model.extract

import com.personio.synthetics.model.Params
import com.personio.synthetics.model.element.Element

internal data class ExtractParams(
    val code: String? = null,
    val variable: Variable? = null,
    val element: Element? = null
) : Params()
