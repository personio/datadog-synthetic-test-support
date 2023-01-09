package com.personio.synthetics.client

import com.datadog.api.client.auth.ApiKeyAuth
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class SyntheticsApiClientTest {
    @Test
    fun `server variables are set properly in the client`() {
        val credentialsProvider: CredentialsProvider = mock()
        whenever(credentialsProvider.getCredentials()).thenReturn(ApiCredentials("apikey", "appkey"))

        val expectedHost = "datadoghq.eu"

        val apiClient = SyntheticsApiClient(credentialsProvider, expectedHost)
        assertEquals(expectedHost, apiClient.apiClient.serverVariables["site"])
    }

    @Test
    fun `the api key and the app key passed are set in the client`() {
        val apiKeyValue = "apiKeyValue"
        val appKeyValue = "appKeyValue"
        val credentialsProvider: CredentialsProvider = mock()
        whenever(credentialsProvider.getCredentials()).thenReturn(ApiCredentials(apiKeyValue, appKeyValue))

        val apiClient = SyntheticsApiClient(credentialsProvider, "datadoghq.eu")

        assertEquals(apiKeyValue, (apiClient.apiClient.authentications["apiKeyAuth"] as ApiKeyAuth).apiKey)
        assertEquals(appKeyValue, (apiClient.apiClient.authentications["appKeyAuth"] as ApiKeyAuth).apiKey)
    }
}
