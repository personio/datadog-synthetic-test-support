package com.personio.synthetics.model.api

import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParseTestOptions

internal data class ExtractValue(
    val name: String,
) : SyntheticsGlobalVariableParseTestOptions()
