package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry

/**
 * Builds the configuration for the step retry behavior
 */
class RetryOptionsBuilder {
    var count = 0L
    var intervalMs = 0.0

    fun build(): SyntheticsTestOptionsRetry {
        return SyntheticsTestOptionsRetry().count(count).interval(intervalMs)
    }
}
