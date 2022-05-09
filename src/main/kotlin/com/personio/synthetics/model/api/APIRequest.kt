package com.personio.synthetics.model.api

import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig

internal data class APIRequest(
    val config: SyntheticsBrowserTestConfig = SyntheticsBrowserTestConfig(),
    val options: Options = Options(),
    val subtype: String = ""
)
