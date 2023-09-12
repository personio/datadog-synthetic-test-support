package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsAPIStep
import com.datadog.api.client.v1.model.SyntheticsAPITest
import com.datadog.api.client.v1.model.SyntheticsAPITestConfig
import com.datadog.api.client.v1.model.SyntheticsAPITestType
import com.datadog.api.client.v1.model.SyntheticsConfigVariable
import com.datadog.api.client.v1.model.SyntheticsConfigVariableType
import com.datadog.api.client.v1.model.SyntheticsTestDetailsSubType
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.personio.synthetics.builder.api.StepsBuilder
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.Defaults

/**
 * A builder for creating multi-step SyntheticAPITest instances
 */
class SyntheticMultiStepApiTestBuilder(
    override val name: String,
    defaults: Defaults,
    apiClient: SyntheticsApiClient
) : SyntheticTestBuilder(name, defaults, apiClient) {
    private val config = SyntheticsAPITestConfig()

    /**
     * Builds a synthetic API test
     * @return SyntheticsAPITest object that contains an API test
     */
    fun build(): SyntheticsAPITest =
        SyntheticsAPITest(
            config,
            parameters.locations,
            parameters.message,
            name,
            options,
            SyntheticsAPITestType.API
        )
            .tags(parameters.tags)
            .status(SyntheticsTestPauseStatus.PAUSED)
            .subtype(SyntheticsTestDetailsSubType.MULTI)

    /**
     * Specifies API steps for a test using a DSL
     * @param stepsBuilder A builder to use for building synthetic API steps
     * @param init A function to apply to the specified builder instance
     */
    fun steps(stepsBuilder: StepsBuilder = StepsBuilder(), init: StepsBuilder.() -> Unit) {
        config.steps(stepsBuilder.apply(init).build())
    }

    /**
     * Specifies API steps for a test
     * @param steps A list of synthetic API steps
     */
    fun steps(steps: List<SyntheticsAPIStep>) {
        config.steps(steps)
    }

    override fun addLocalVariable(name: String, pattern: String) {
        config.addConfigVariablesItem(
            SyntheticsConfigVariable()
                .name(name.uppercase())
                .type(SyntheticsConfigVariableType.TEXT)
                .pattern(pattern)
                .example("")
        )
    }

    override fun useGlobalVariable(name: String) {
        val variableName = name.uppercase()
        val variableId = getGlobalVariableId(variableName)
        checkNotNull(variableId) { "The global variable $name to be used in the test doesn't exist in DataDog." }
        config.addConfigVariablesItem(
            SyntheticsConfigVariable()
                .name(variableName)
                .id(variableId)
                .type(SyntheticsConfigVariableType.GLOBAL)
        )
    }
}
