package com.personio.synthetics.client

import com.datadog.api.v1.client.model.SyntheticsBrowserTest
import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig
import com.datadog.api.v1.client.model.SyntheticsDeviceID
import com.datadog.api.v1.client.model.SyntheticsTestOptions
import com.datadog.api.v1.client.model.SyntheticsTestOptionsMonitorOptions
import com.datadog.api.v1.client.model.SyntheticsTestOptionsRetry
import com.personio.synthetics.config.TestConfig

/**
 * Creates a synthetic browser test with the added configurations and steps in Datadog
 * @param name Name of the test
 * @param steps Calls the added steps and configuration functions of the test
 */
inline fun syntheticBrowserTest(name: String, steps: BrowserTest.() -> Unit) {
    check(name.isNotBlank()) {
        "The test's name must not be empty"
    }
    val browserTest = BrowserTest(name, SyntheticsApiClient())
    browserTest.steps()
    browserTest.createBrowserTest()
}

class BrowserTest(testName: String, private val syntheticsApiClient: SyntheticsApiClient) : SyntheticsBrowserTest() {
    init {
        name = testName
        locations = TestConfig.RUN_LOCATIONS
        config = SyntheticsBrowserTestConfig()
        options = defaultSyntheticsTestOptions()
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

    private fun defaultSyntheticsTestOptions(): SyntheticsTestOptions {
        return SyntheticsTestOptions()
            .addDeviceIdsItem(SyntheticsDeviceID.CHROME_LAPTOP_LARGE)
            .tickEvery(TestConfig.TEST_FREQUENCY)
            .minFailureDuration(TestConfig.MIN_FAILURE_DURATION)
            .minLocationFailed(TestConfig.MIN_LOCATION_FAILED)
            .retry(SyntheticsTestOptionsRetry().count(TestConfig.RETRY_COUNT).interval(TestConfig.RETRY_INTERVAL))
            .monitorPriority(TestConfig.MONITOR_PRIORITY)
            .monitorOptions(
                SyntheticsTestOptionsMonitorOptions()
                    .renotifyInterval(TestConfig.RENOTIFY_INTERVAL)
            )
    }
}
