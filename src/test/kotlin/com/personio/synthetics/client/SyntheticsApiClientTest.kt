package com.personio.synthetics.client

import com.datadog.api.v1.client.auth.ApiKeyAuth
import com.personio.synthetics.config.TestConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SyntheticsApiClientTest {
    @Test
    fun `server variables are set properly in the client`() {
        val expectedServerVariables = mapOf("site" to TestConfig.DATADOG_API_HOST)
        val apiClient = SyntheticsApiClient("apikey", "appkey")
        assertEquals(expectedServerVariables, apiClient.apiClient.serverVariables)
    }

    @Test
    fun `the api key and the app key passed are set in the client`() {
        val apiClient = SyntheticsApiClient("apikey", "appkey")
        assertEquals("apikey", (apiClient.apiClient.authentications["apiKeyAuth"] as ApiKeyAuth).apiKey)
        assertEquals("appkey", (apiClient.apiClient.authentications["appKeyAuth"] as ApiKeyAuth).apiKey)
    }
}
