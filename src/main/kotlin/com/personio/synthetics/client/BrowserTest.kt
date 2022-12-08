package com.personio.synthetics.client

import com.datadog.api.client.v1.model.SyntheticsBrowserTest
import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsTestOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsMonitorOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import com.personio.synthetics.config.Configuration
import com.personio.synthetics.config.ConfigurationLoader
import com.personio.synthetics.config.Credentials
import com.personio.synthetics.config.Defaults

/**
 * Creates a synthetic browser test with the added configurations and steps in Datadog
 * @param name Name of the test
 * @param steps Calls the added steps and configuration functions of the test
 */
inline fun syntheticBrowserTest(name: String, steps: BrowserTest.() -> Unit) {
    check(name.isNotBlank()) {
        "The test's name must not be empty"
    }
    val projectConfig = ConfigurationLoader().loadConfiguration(
        Configuration::class.java
    )
    val browserTest = BrowserTest(name, SyntheticsApiClient(credentialsProvider(projectConfig.credentials), projectConfig.testSettings.datadogApiHost), projectConfig.testSettings.defaults)
    browserTest.steps()
    browserTest.createBrowserTest()
}

fun credentialsProvider(credentials: Credentials): CredentialsProvider {
    return if (!credentials.datadogCredentialsAwsArn.isNullOrEmpty()) {
        AwsSecretsManagerCredentialsProvider(credentials)
    } else if (!credentials.ddApiKey.isNullOrEmpty() && !credentials.ddAppKey.isNullOrEmpty()) {
        ConfigCredentialsProvider(credentials)
    } else {
        throw IllegalStateException("Please set the required config values for credentials.")
    }
}

/**
 * Synthetic browser test client for the API calls with the Datadog
 */
class BrowserTest(testName: String, private val syntheticsApiClient: SyntheticsApiClient, private val defaultsSettings: Defaults) : SyntheticsBrowserTest() {
    init {
        name = testName
        locations = defaultsSettings.runLocations
        config = SyntheticsBrowserTestConfig()
        options = defaultSyntheticsTestOptions()
        message = ""
    }

    /**
     * Creates / Updates the synthetic browser test. If the test already exists,
     * it will update the test, else it will create a new test
     * @return SyntheticsBrowserTest object
     */
    @PublishedApi internal fun createBrowserTest(): SyntheticsBrowserTest {
        val testId = getTestId()
        return if (testId != null) {
            syntheticsApiClient.updateBrowserTest(testId, this)
        } else {
            syntheticsApiClient.createSyntheticsBrowserTest(this)
        }
    }

    internal fun getGlobalVariableId(variableName: String) =
        syntheticsApiClient.listGlobalVariables()
            .variables
            .orEmpty()
            .filterNotNull()
            .find { it.name.equals(variableName) }
            ?.id

    private fun getTestId() =
        syntheticsApiClient.listTests()
            .tests
            .orEmpty()
            .filterNotNull()
            .find { it.name.equals(name) }
            ?.publicId

    private fun defaultSyntheticsTestOptions(): SyntheticsTestOptions =
        SyntheticsTestOptions()
            .addDeviceIdsItem(SyntheticsDeviceID.CHROME_LAPTOP_LARGE)
            .tickEvery(defaultsSettings.testFrequencySec)
            .minFailureDuration(defaultsSettings.minFailureDurationSec)
            .minLocationFailed(defaultsSettings.minLocationFailed)
            .retry(SyntheticsTestOptionsRetry().count(defaultsSettings.retryCount).interval(defaultsSettings.retryIntervalMillisec))
            .monitorOptions(
                SyntheticsTestOptionsMonitorOptions()
                    .renotifyInterval(defaultsSettings.renotifyIntervalMinutes)
            )
}
