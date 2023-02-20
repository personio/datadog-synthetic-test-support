package com.personio.synthetics.client

import com.personio.synthetics.config.Credentials
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConfigCredentialsProviderTest {
    private val credentials = Credentials(ddApiKey = "apiKey", ddAppKey = "appKey", null, null)

    @Test
    fun `getCredentials returns expected API credentials taken from the configuration file`() {
        val credentialsProvider = ConfigCredentialsProvider(credentials)
        assertEquals(ApiCredentials("apiKey", "appKey"), credentialsProvider.getCredentials())
    }
}
