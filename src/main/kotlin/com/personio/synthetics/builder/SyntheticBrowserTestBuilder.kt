package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsBrowserTest
import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsBrowserTestType
import com.datadog.api.client.v1.model.SyntheticsBrowserVariable
import com.datadog.api.client.v1.model.SyntheticsBrowserVariableType
import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsTestRequest
import com.personio.synthetics.builder.browser.StepsBuilder
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.Defaults
import java.net.URL
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * A builder for creating SyntheticsBrowserTest instances
 * The builder is still in progress, don't use it for the browser test creation
 * Follow [E2ETest][https://github.com/personio/datadog-synthetic-test-support/blob/master/src/test/kotlin/com/personio/synthetics/e2e/E2ETest.kt] example to create a browser synthetic test
 */
class SyntheticBrowserTestBuilder(
    override val name: String,
    defaults: Defaults,
    apiClient: SyntheticsApiClient,
) : SyntheticTestBuilder(name, defaults, apiClient) {
    private val config = SyntheticsBrowserTestConfig()
    private var steps: MutableList<SyntheticsStep> = mutableListOf()

    /**
     * Builds a synthetic browser test
     * @return SyntheticsBrowserTest object that contains a browser test
     */
    fun build(): SyntheticsBrowserTest {
        val test =
            SyntheticsBrowserTest(
                config,
                parameters.locations,
                parameters.message,
                name,
                options,
                SyntheticsBrowserTestType.BROWSER,
            )
                .steps(steps)
                .tags(parameters.tags)

        status?.let {
            test.status(it)
        }

        return test
    }

    /**
     * Sets the base url for the synthetic browser test
     * @param url The base url for the test
     */
    fun baseUrl(url: URL) {
        config.request(
            SyntheticsTestRequest()
                .method("GET")
                .url(url.toString()),
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

    /**
     * Sets the browsers and devices for the synthetic browser test
     * @param deviceIds Pass comma separated browsers and devices to the test config derived from SyntheticsDeviceID class
     */
    fun browsersAndDevices(vararg deviceIds: SyntheticsDeviceID) {
        options.deviceIds = deviceIds.map { it }
    }

    /**
     * Sets the browsers and devices for the synthetic browser test
     * @param deviceIds Pass comma separated browsers and devices to the test config derived from SyntheticsDeviceID class
     */
    @Deprecated(
        message = "The function is deprecated. Please use `browsersAndDevices` instead.",
        replaceWith = ReplaceWith("browsersAndDevices(*deviceIds)"),
    )
    fun browserAndDevice(vararg deviceIds: SyntheticsDeviceID) {
        options.deviceIds = deviceIds.map { it }
    }

    fun steps(
        stepsBuilder: StepsBuilder = StepsBuilder(),
        init: StepsBuilder.() -> Unit,
    ) {
        steps = stepsBuilder.apply(init).build().toMutableList()
    }

    override fun addLocalVariable(
        name: String,
        pattern: String,
    ) {
        config.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name.uppercase())
                .type(SyntheticsBrowserVariableType.TEXT)
                .pattern(pattern)
                .example(""),
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
                .type(SyntheticsBrowserVariableType.GLOBAL),
        )
    }
}
