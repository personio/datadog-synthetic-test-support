package com.personio.synthetics.model.actions

import com.personio.synthetics.model.ElementParams
import com.personio.synthetics.model.element.Element

internal data class ActionsParams(
    var value: String? = null,
    override var element: Element? = null,
    var delay: Long? = null
) : ElementParams
