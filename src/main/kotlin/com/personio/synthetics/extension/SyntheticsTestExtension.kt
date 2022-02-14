package com.personio.synthetics.extension

import com.datadog.api.v1.client.Configuration
import com.datadog.api.v1.client.api.SyntheticsApi
import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig
import com.datadog.api.v1.client.model.SyntheticsBrowserTestType
import com.datadog.api.v1.client.model.SyntheticsDeviceID
import com.datadog.api.v1.client.model.SyntheticsTestOptions
import com.datadog.api.v1.client.model.SyntheticsTestOptionsMonitorOptions
import com.datadog.api.v1.client.model.SyntheticsTestOptionsRetry
import com.personio.synthetics.BrowserTest
import com.personio.synthetics.config.TestConfig
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

/**
 * Provides a parameter resolver which allows test class to have
 * BrowserTest as parameter which is required for creating synthetic tests
 */
class SyntheticsTestExtension : ParameterResolver {
    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return parameterContext?.parameter?.type == BrowserTest::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        val syntheticsApi = makeSyntheticsApiClient()
        return BrowserTest(syntheticsApi)
            .locations(TestConfig.RUN_LOCATIONS)
            .config(SyntheticsBrowserTestConfig())
            .options(defaultSyntheticsTestOptions())
            .type(SyntheticsBrowserTestType.BROWSER)
    }

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

    private fun makeSyntheticsApiClient(): SyntheticsApi {
        val apiKey = System.getenv("DD_API_KEY")
        val appKey = System.getenv("DD_APP_KEY")
        val serverVariables = mapOf("site" to TestConfig.DATADOG_API_HOST)
        val secrets = mapOf(
            "apiKeyAuth" to apiKey,
            "appKeyAuth" to appKey
        )
        val apiClient = Configuration.getDefaultApiClient()
        apiClient.serverVariables = serverVariables
        apiClient.configureApiKeys(secrets)
        return SyntheticsApi(apiClient)
    }
}
