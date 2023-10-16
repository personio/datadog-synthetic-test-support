package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsBrowserTest
import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsBrowserTestType
import com.datadog.api.client.v1.model.SyntheticsBrowserVariable
import com.datadog.api.client.v1.model.SyntheticsBrowserVariableType
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.datadog.api.client.v1.model.SyntheticsTestRequest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.Defaults
import java.net.URL
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * A builder for creating SyntheticsBrowserTest instances
 */
class SyntheticBrowserTestBuilder(
    override val name: String,
    defaults: Defaults,
    apiClient: SyntheticsApiClient
) : SyntheticTestBuilder(name, defaults, apiClient) {
    private val config = SyntheticsBrowserTestConfig()

    /**
     * Builds a synthetic browser test
     * @return SyntheticsBrowserTest object that contains a browser test
     */
    fun build(): SyntheticsBrowserTest =
        SyntheticsBrowserTest(
            config,
            parameters.locations,
            parameters.message,
            name,
            options,
            SyntheticsBrowserTestType.BROWSER
        )
            .tags(parameters.tags)
            .status(SyntheticsTestPauseStatus.PAUSED)

    /**
     * Sets the base url for the synthetic browser test
     * @param url The base url for the test
     */
    fun baseUrl(url: URL) {
        config.request(
            SyntheticsTestRequest()
                .method("GET")
                .url(url.toString())
        )
    }

    /**
     * Sets the test execution frequency for the synthetic browser test
     * @param frequency The frequency of the test
     * Allowed test frequency is between 5 minutes and 7 days
     */
    override fun testFrequency(frequency: Duration) {
        require(frequency in 5.minutes..7.days) {
            "Frequency should be between 5 minutes and 7 days."
        }
        options.tickEvery = frequency.inWholeSeconds
    }

    override fun addLocalVariable(name: String, pattern: String) {
        config.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name.uppercase())
                .type(SyntheticsBrowserVariableType.TEXT)
                .pattern(pattern)
                .example("")
        )
    }

    override fun useGlobalVariable(name: String) {
        val variableName = name.uppercase()
        val variableId = getGlobalVariableId(variableName)
        checkNotNull(variableId) { "The global variable $name to be used in the test doesn't exist in DataDog." }
        config.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(variableName)
                .id(variableId)
                .type(SyntheticsBrowserVariableType.GLOBAL)
        )
    }
}
