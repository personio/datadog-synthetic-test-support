package com.personio.synthetics.model.api

import com.datadog.api.v1.client.model.SyntheticsGlobalVariableParseTestOptions

internal data class ExtractValue(val name: String) : SyntheticsGlobalVariableParseTestOptions()
