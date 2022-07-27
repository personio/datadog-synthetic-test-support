package com.personio.synthetics.client

import com.datadog.api.client.ApiClient
import com.datadog.api.client.v1.api.SyntheticsApi
import com.personio.synthetics.config.TestConfig

/**
 * API Client for the Datadog Synthetic test
 */
class SyntheticsApiClient(credentialsProvider: CredentialsProvider) : SyntheticsApi() {
    init {
        val credentials = credentialsProvider.getCredentials()
        apiClient = ApiClient.getDefaultApiClient().apply {
            serverVariables = mapOf("site" to TestConfig.DATADOG_API_HOST)
            configureApiKeys(mapOf("apiKeyAuth" to credentials.apiKey, "appKeyAuth" to credentials.appKey))
        }
    }
}
