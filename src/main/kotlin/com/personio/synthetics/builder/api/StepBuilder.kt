package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPIStep
import com.datadog.api.client.v1.model.SyntheticsAPIStepSubtype
import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import com.datadog.api.client.v1.model.SyntheticsTestRequest
import com.personio.synthetics.builder.AssertionsBuilder
import com.personio.synthetics.builder.RequestBuilder
import com.personio.synthetics.builder.parsing.ParsingOptionsBuilder
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Builds a step for a multi-step API synthetic test
 * @param name Name of the step
 * @param requestBuilder Instance of a request builder to use to provide a request
 * @param assertionBuilder Instance of an assertions builder to use to provide assertions
 * @param parsingOptionsBuilder Instance of a parsing options builder to use to provide parsing options
 */
class StepBuilder(
    val name: String,
    private val requestBuilder: RequestBuilder = RequestBuilder(),
    private val assertionBuilder: AssertionsBuilder = AssertionsBuilder(),
    private val parsingOptionsBuilder: ParsingOptionsBuilder = ParsingOptionsBuilder(),
) {
    var allowFailure = false
    var isCritical = false
    private var retryOptions = SyntheticsTestOptionsRetry()

    private val step = SyntheticsAPIStep()
    private var request: SyntheticsTestRequest? = null
    private var assertions = listOf<SyntheticsAssertion>()
    private val parsingOptions = mutableListOf<SyntheticsParsingOptions>()

    fun build(): SyntheticsAPIStep {
        if (request == null) {
            throw IllegalStateException("Request must be provided.")
        }

        if (allowFailure) {
            step.isCritical(isCritical)
        }

        if (parsingOptions.isNotEmpty()) {
            step.extractedValues(parsingOptions)
        }

        return step
            .request(request)
            .allowFailure(allowFailure)
            .retry(retryOptions)
            .name(name)
            .assertions(assertions)
            .subtype(SyntheticsAPIStepSubtype.HTTP)
    }

    /**
     * Sets the HTTP request for the synthetic test
     * @param init Configuration to be applied on the RequestBuilder
     */
    fun request(init: RequestBuilder.() -> Unit) {
        request = requestBuilder.apply(init).build()
    }

    /**
     * Sets the assertions for the synthetic test
     * @param init Configuration to be applied on the AssertionsBuilder
     */
    fun assertions(init: AssertionsBuilder.() -> Unit) {
        assertions = assertionBuilder.apply(init).build()
    }

    /**
     * Specifies the variable extraction for the synthetic test
     * @param variableName Name of the variable
     * @param init Configuration to be applied on the ParsingOptionsBuilder
     */
    fun extract(
        variableName: String,
        init: ParsingOptionsBuilder.() -> Unit,
    ) {
        parsingOptionsBuilder.variable(variableName)

        val options =
            parsingOptionsBuilder
                .apply(init)
                .build()

        if (options != null) {
            parsingOptions.add(options)
        }
    }

    /**
     * Sets the retry count and interval for the step
     * @param retryCount The retry count for the step
     * Allowed retry count is between 0 and 5
     * @param retryInterval The retry interval for the step
     * Allowed retry interval is between 0 and 5 seconds
     */
    fun retry(
        retryCount: Long,
        retryInterval: Duration,
    ) {
        require(retryCount in 0..5) {
            "Step retry count should be between 0 and 5."
        }
        require(retryInterval in 0.seconds..5.seconds) {
            "Step retry interval should be between 0 and 5000 milliseconds."
        }
        retryOptions = SyntheticsTestOptionsRetry().count(retryCount).interval(retryInterval.inWholeMilliseconds.toDouble())
    }
}
