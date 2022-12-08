package com.personio.synthetics.client

import com.personio.synthetics.config.Credentials
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ConfigurationTest {

    @Test
    fun `Validate Datadog credentials are set`() {
        val credentials = Credentials(ddApiKey = null, ddAppKey = null, awsRegion = "awsRegion", datadogCredentialsAwsArn = "awsArn")
        assertTrue(
            credentialsProvider(credentials) is AwsSecretsManagerCredentialsProvider,
            "The returned credentials provider is not an instance of AwsSecretsManagerCredentialsProvider class"
        )
    }

    @Test
    fun `Validate api and app key are set`() {
        val credentials = Credentials(ddApiKey = "apiKey", ddAppKey = "appKey", awsRegion = "", datadogCredentialsAwsArn = "")
        assertTrue(
            credentialsProvider(credentials) is ConfigCredentialsProvider,
            "The returned credentials provider is not an instance of ConfigCredentialsProvider class"
        )
    }

    @Test
    fun `Validate exception is thrown when api key is not set`() {
        val credentials = Credentials(ddApiKey = "", ddAppKey = "appKey", null, datadogCredentialsAwsArn = "")
        val exception = assertThrows<Exception> { credentialsProvider(credentials) }
        assertEquals("Please set the required config values for credentials.", exception.message)
    }

    @Test
    fun `Validate exception is thrown when app key is not set`() {
        val credentials = Credentials(ddApiKey = "apiKey", ddAppKey = "", null, datadogCredentialsAwsArn = "")
        val exception = assertThrows<Exception> { credentialsProvider(credentials) }
        assertEquals("Please set the required config values for credentials.", exception.message)
    }
}
