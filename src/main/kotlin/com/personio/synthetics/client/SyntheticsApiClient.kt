package com.personio.synthetics.client

import com.datadog.api.client.ApiClient
import com.datadog.api.client.v1.api.SyntheticsApi
import com.personio.synthetics.config.Config

/**
 * API Client for the Datadog Synthetic test
 */
class SyntheticsApiClient(
    credentialsProvider: CredentialsProvider,
    apiHost: String = Config.testConfig.datadogApiHost,
) : SyntheticsApi() {
    init {
        val credentials = credentialsProvider.getCredentials()
        apiClient =
            ApiClient.getDefaultApiClient().apply {
                serverVariables = mapOf("site" to apiHost)
                configureApiKeys(mapOf("apiKeyAuth" to credentials.apiKey, "appKeyAuth" to credentials.appKey))
            }
    }
}
