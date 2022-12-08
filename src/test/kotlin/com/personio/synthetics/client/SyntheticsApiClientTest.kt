package com.personio.synthetics.client

import com.datadog.api.client.auth.ApiKeyAuth
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class SyntheticsApiClientTest {
    @Test
    fun `server variables are set properly in the client`() {
        val expectedServerVariables = mapOf("site" to "datadoghq.eu")
        val credentialsProvider: CredentialsProvider = mock()
        whenever(credentialsProvider.getCredentials()).thenReturn(ApiCredentials("apikey", "appkey"))
        val apiClient = SyntheticsApiClient(credentialsProvider, "datadoghq.eu")
        assertEquals(expectedServerVariables, apiClient.apiClient.serverVariables)
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
