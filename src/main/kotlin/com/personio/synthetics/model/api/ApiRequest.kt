package com.personio.synthetics.model.api

import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig

internal data class ApiRequest(
    val config: SyntheticsBrowserTestConfig = SyntheticsBrowserTestConfig(),
    val options: Options = Options(),
    val subtype: String = ""
)
