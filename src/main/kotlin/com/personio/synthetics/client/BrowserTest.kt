package com.personio.synthetics.client

import com.datadog.api.client.v1.model.SyntheticsBrowserTest
import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsTestOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsMonitorOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.datadog.api.client.v1.model.SyntheticsUpdateTestPauseStatusPayload
import com.personio.synthetics.config.Config
import com.personio.synthetics.config.Defaults
import com.personio.synthetics.config.loadConfiguration
import com.personio.synthetics.model.config.SyntheticsDeviceID

/**
 * Creates a synthetic browser test with the added configurations (if any) and steps in Datadog
 * The default configurations for the tests are loaded from the configuration file in resources.
 *
 * Deprecation notice: In future releases this method will be replaced by one that
 * uses SyntheticTestBuilder or similar.
 * @param name Name of the test
 * @param steps Calls the added steps and configuration functions of the test
 */
@Deprecated(message = "This function will have its package and signature changed in 3.x.x")
inline fun syntheticBrowserTest(
    name: String,
    steps: BrowserTest.() -> Unit,
) {
    check(name.isNotBlank()) {
        "The test's name must not be empty."
    }
    loadConfiguration("configuration.yaml")
    val browserTest = BrowserTest(name, SyntheticsApiClient(getCredentialsProvider()))
    browserTest.steps()
    browserTest.createBrowserTest()
}

fun getCredentialsProvider(): CredentialsProvider {
    return Config.testConfig.credentials.let {
        when {
            !it.datadogCredentialsAwsArn.isNullOrEmpty() -> AwsSecretsManagerCredentialsProvider(it)
            !it.ddApiKey.isNullOrEmpty() && !it.ddAppKey.isNullOrEmpty() -> ConfigCredentialsProvider(it)
            else -> throw IllegalStateException(
                "Please set the required config values for credentials in the \"configuration.yaml\" under resources.",
            )
        }
    }
}

/**
 * Synthetic browser test client for the API calls with the Datadog.
 *
 * Deprecation notice: In future releases this class will be replaced by
 * SyntheticTestBuilder (or similar).
 */
@Deprecated(message = "Planned for removal in 3.x.x")
class BrowserTest(
    testName: String,
    private val syntheticsApiClient: SyntheticsApiClient,
    private val defaultSettings: Defaults = Config.testConfig.defaults,
) : SyntheticsBrowserTest() {
    init {
        name = testName
        locations = defaultSettings.runLocations
        config = SyntheticsBrowserTestConfig()
        options = defaultSyntheticsTestOptions()
        message = ""
    }

    /**
     * Creates / Updates the synthetic browser test. If the test already exists,
     * it will update the test, else it will create a new test
     * @return SyntheticsBrowserTest object
     */
    @PublishedApi
    @Deprecated(message = "Planned for removal in 3.x.x")
    internal fun createBrowserTest(): SyntheticsBrowserTest {
        val testId = getTestId()
        return if (testId != null) {
            if (syntheticsApiClient.getTest(testId).status.equals(SyntheticsTestPauseStatus.LIVE)) {
                syntheticsApiClient.updateTestPauseStatus(
                    testId,
                    SyntheticsUpdateTestPauseStatusPayload().newStatus(
                        SyntheticsTestPauseStatus.PAUSED,
                    ),
                )
                syntheticsApiClient.updateBrowserTest(testId, this).also {
                    syntheticsApiClient.updateTestPauseStatus(
                        testId,
                        SyntheticsUpdateTestPauseStatusPayload().newStatus(
                            SyntheticsTestPauseStatus.LIVE,
                        ),
                    )
                }
            } else {
                syntheticsApiClient.updateBrowserTest(testId, this)
            }
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
            .addDeviceIdsItem(SyntheticsDeviceID.CHROME_LAPTOP_LARGE.value)
            .tickEvery(defaultSettings.testFrequency / 1000)
            .minFailureDuration(defaultSettings.minFailureDuration / 1000)
            .minLocationFailed(defaultSettings.minLocationFailed)
            .retry(SyntheticsTestOptionsRetry().count(defaultSettings.retryCount).interval(defaultSettings.retryInterval))
            .monitorOptions(
                SyntheticsTestOptionsMonitorOptions(),
            )
}
