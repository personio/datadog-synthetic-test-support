package com.personio.synthetics.model.assertion

import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.personio.synthetics.model.Params
import com.personio.synthetics.model.element.Element

internal data class AssertionParams(
    val attribute: String? = null,
    val value: String? = null,
    val check: SyntheticsCheckType? = null,
    val element: Element? = null,
    val code: String? = null
) : Params()
