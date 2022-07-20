package com.personio.synthetics.client

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse

internal class AwsSecretsManagerCredentialsProviderTest {
    @Test
    fun `getCredentials returns expected API credentials given the secret has the expected structure`() {
        val secretsManagerClient: SecretsManagerClient = mock()
        val credentialsProvider = AwsSecretsManagerCredentialsProvider(secretsManagerClient)
        val getSecretValueResponse = GetSecretValueResponse.builder()
            .secretString("{\"apiKey\": \"apikey\",\"appKey\": \"appkey\"}")
            .build()
        whenever(secretsManagerClient.getSecretValue(any<GetSecretValueRequest>())).thenReturn(getSecretValueResponse)

        assertEquals(ApiCredentials("apikey", "appkey"), credentialsProvider.getCredentials())
    }

    @Test
    fun `getCredentials throws exception given the secret has a wrong structure`() {
        val secretsManagerClient: SecretsManagerClient = mock()
        val credentialsProvider = AwsSecretsManagerCredentialsProvider(secretsManagerClient)
        val getSecretValueResponse = GetSecretValueResponse.builder()
            .secretString("{\"foo\": \"bar\"}")
            .build()
        whenever(secretsManagerClient.getSecretValue(any<GetSecretValueRequest>())).thenReturn(getSecretValueResponse)

        assertThrows<Exception> { credentialsProvider.getCredentials() }
    }

    @Test
    fun `getCredentials throws exception given the secret has no secret string`() {
        val secretsManagerClient: SecretsManagerClient = mock()
        val credentialsProvider = AwsSecretsManagerCredentialsProvider(secretsManagerClient)
        val getSecretValueResponse = GetSecretValueResponse.builder().build()
        whenever(secretsManagerClient.getSecretValue(any<GetSecretValueRequest>())).thenReturn(getSecretValueResponse)

        assertThrows<Exception> { credentialsProvider.getCredentials() }
    }

    @Test
    fun `getCredentials throws exception given the secret fetching fails`() {
        val secretsManagerClient: SecretsManagerClient = mock()
        val credentialsProvider = AwsSecretsManagerCredentialsProvider(secretsManagerClient)
        whenever(secretsManagerClient.getSecretValue(any<GetSecretValueRequest>())).thenThrow()

        assertThrows<Exception> { credentialsProvider.getCredentials() }
    }
}
