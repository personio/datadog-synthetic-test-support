package com.personio.synthetics.model.api

import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig

internal data class APIRequest(
    var config: SyntheticsBrowserTestConfig = SyntheticsBrowserTestConfig(),
    var options: Options = Options(),
    var subtype: String = ""
)
