package com.personio.synthetics.dsl

import com.datadog.api.client.v1.model.SyntheticsAPITest
import com.datadog.api.client.v1.model.SyntheticsBrowserTest
import com.personio.synthetics.builder.SyntheticBrowserTestBuilder
import com.personio.synthetics.builder.SyntheticMultiStepApiTestBuilder
import com.personio.synthetics.client.AwsSecretsManagerCredentialsProvider
import com.personio.synthetics.client.ConfigCredentialsProvider
import com.personio.synthetics.client.CredentialsProvider
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.Credentials
import com.personio.synthetics.config.Defaults
import com.personio.synthetics.config.getConfigFromFile

private fun getSyntheticsApiClientAndDefaults(): Pair<SyntheticsApiClient, Defaults> {
    val configuration = getConfigFromFile("configuration.yaml")

    return SyntheticsApiClient(
        credentialsProvider = getCredentialsProvider(configuration.credentials),
        apiHost = configuration.datadogApiHost,
    ) to configuration.defaults
}

fun syntheticMultiStepApiTest(
    name: String,
    init: SyntheticMultiStepApiTestBuilder.() -> Unit,
): SyntheticsAPITest {
    check(name.isNotBlank()) {
        "The test's name must not be empty."
    }

    val (client, defaults) = getSyntheticsApiClientAndDefaults()

    val test =
        SyntheticMultiStepApiTestBuilder(name, defaults, client)
            .apply(init)
            .build()

    val testId = getTestId(client, name)

    return if (testId != null) {
        client.updateAPITest(testId, test)
    } else {
        client.createSyntheticsAPITest(test)
    }
}

fun syntheticBrowserTest(
    name: String,
    init: SyntheticBrowserTestBuilder.() -> Unit,
): SyntheticsBrowserTest {
    check(name.isNotBlank()) {
        "The test's name must not be empty."
    }

    val (client, defaults) = getSyntheticsApiClientAndDefaults()

    val test =
        SyntheticBrowserTestBuilder(name, defaults, client)
            .apply(init)
            .build()

    val testId = getTestId(client, name)

    return if (testId != null) {
        client.updateBrowserTest(testId, test)
    } else {
        client.createSyntheticsBrowserTest(test)
    }
}

private fun getTestId(syntheticsApiClient: SyntheticsApiClient, name: String) =
    syntheticsApiClient.listTests()
        .tests
        .orEmpty()
        .filterNotNull()
        .find { it.name == name }
        ?.publicId

private fun getCredentialsProvider(credentials: Credentials): CredentialsProvider {
    return credentials.let {
        when {
            !it.datadogCredentialsAwsArn.isNullOrEmpty() -> AwsSecretsManagerCredentialsProvider(it)
            !it.ddApiKey.isNullOrEmpty() && !it.ddAppKey.isNullOrEmpty() -> ConfigCredentialsProvider(it)
            else -> error("Please set the required config values for credentials in the \"configuration.yaml\" under resources.")
        }
    }
}
