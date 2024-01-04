package com.personio.synthetics.model.actions

import com.personio.synthetics.model.Params
import com.personio.synthetics.model.element.ElementForSpecialActions

internal data class SpecialActionsParams(
    val element: ElementForSpecialActions? = null,
    val x: Int? = null,
    val y: Int? = null,
) : Params()
