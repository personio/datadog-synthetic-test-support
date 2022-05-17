package com.personio.synthetics.client

import com.datadog.api.v1.client.Configuration
import com.datadog.api.v1.client.api.SyntheticsApi
import com.personio.synthetics.config.TestConfig

class SyntheticsApiClient(apiKey: String = System.getenv("DD_API_KEY"), appKey: String = System.getenv("DD_APP_KEY")) : SyntheticsApi() {
    init {
        apiClient = Configuration.getDefaultApiClient().apply {
            serverVariables = mapOf("site" to TestConfig.DATADOG_API_HOST)
            configureApiKeys(mapOf("apiKeyAuth" to apiKey, "appKeyAuth" to appKey))
        }
    }
}
