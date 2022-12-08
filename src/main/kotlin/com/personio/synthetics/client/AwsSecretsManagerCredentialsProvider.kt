package com.personio.synthetics.client

import com.personio.synthetics.config.Credentials
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest

class AwsSecretsManagerCredentialsProvider(val credentials: Credentials, val client: SecretsManagerClient = SecretsManagerClient.builder().region(Region.of(credentials.awsRegion)).build()) : CredentialsProvider {

    override fun getCredentials(): ApiCredentials {
        val getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(credentials.datadogCredentialsAwsArn)
            .build()

        val secret = runCatching { client.getSecretValue(getSecretValueRequest) }
            .getOrElse {
                throw CredentialsProviderException("Failed to get Datadog credentials from AWS Secrets Manager").initCause(it)
            }.secretString() ?: throw CredentialsProviderException("Secret obtained from AWS Secrets Manager does not contain secret string")

        return runCatching { Json.decodeFromString<ApiCredentials>(secret) }
            .getOrElse {
                throw CredentialsProviderException("Failed to decode the secret obtained from AWS Secrets Manager to Datadog credentials")
                    .initCause(it)
            }
    }
}
