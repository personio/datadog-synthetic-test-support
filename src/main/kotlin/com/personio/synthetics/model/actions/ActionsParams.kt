package com.personio.synthetics.model.actions

import com.personio.synthetics.model.element.Element

internal data class ActionsParams(
    val value: String? = null,
    val element: Element? = null,
    val delay: Long? = null
)
