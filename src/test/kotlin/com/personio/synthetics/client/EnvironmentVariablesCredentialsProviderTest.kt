package com.personio.synthetics.client

import io.kotest.extensions.system.OverrideMode
import io.kotest.extensions.system.withEnvironment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class EnvironmentVariablesCredentialsProviderTest {
    @Test
    fun `getCredentials returns expected API credentials given the environment variables are set`() {
        val apiKeyValue = "apiKeyValue"
        val appKeyValue = "appKeyValue"
        val credentialsProvider = EnvironmentVariablesCredentialsProvider()
        withEnvironment(
            mapOf(
                EnvironmentVariablesCredentialsProvider.DD_API_KEY to apiKeyValue,
                EnvironmentVariablesCredentialsProvider.DD_APP_KEY to appKeyValue,
            ),
            OverrideMode.SetOrOverride
        ) {
            assertEquals(ApiCredentials(apiKeyValue, appKeyValue), credentialsProvider.getCredentials())
        }
    }

    @Test
    fun `getCredentials throws exception given the DD_API_KEY environment variable is not set`() {
        val credentialsProvider = EnvironmentVariablesCredentialsProvider()
        withEnvironment(
            mapOf(EnvironmentVariablesCredentialsProvider.DD_APP_KEY to "appKeyValue"),
            OverrideMode.SetOrOverride
        ) {
            val exception = assertThrows<IllegalArgumentException> { credentialsProvider.getCredentials() }
            assertEquals("Environment variable ${EnvironmentVariablesCredentialsProvider.DD_API_KEY} not set", exception.message)
        }
    }

    @Test
    fun `getCredentials throws exception given the DD_APP_KEY environment variable is not set`() {
        val credentialsProvider = EnvironmentVariablesCredentialsProvider()
        withEnvironment(
            mapOf(EnvironmentVariablesCredentialsProvider.DD_API_KEY to "apiKeyValue"),
            OverrideMode.SetOrOverride
        ) {
            val exception = assertThrows<IllegalArgumentException> { credentialsProvider.getCredentials() }
            assertEquals("Environment variable ${EnvironmentVariablesCredentialsProvider.DD_APP_KEY} not set", exception.message)
        }
    }
}
