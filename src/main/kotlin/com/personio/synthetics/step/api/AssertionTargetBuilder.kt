package com.personio.synthetics.step.api

import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionTargetValue
import com.datadog.api.client.v1.model.SyntheticsAssertionType

/**
 * Builder for creating SyntheticsAssertionTarget object for API step assertion
 */
class AssertionTargetBuilder {
    var type: SyntheticsAssertionType? = null
    var operator: SyntheticsAssertionOperator? = null
    var property: String? = null
    var target: Any? = null

    /**
     * Builds and returns a SyntheticsAssertionTarget object with the configured parameters
     */
    fun assertionBuilder(): SyntheticsAssertionTarget {
        val type = type ?: throw IllegalArgumentException("Type is required")
        val operator = operator ?: throw IllegalArgumentException("Operator is required")
        val targetValue =
            when (val t = target) {
                is String -> SyntheticsAssertionTargetValue(t)
                is Int -> SyntheticsAssertionTargetValue(t.toDouble())
                else -> throw IllegalArgumentException("Target must be String or Int")
            }

        val assertionTarget = SyntheticsAssertionTarget(operator, targetValue, type)
        property?.let { assertionTarget.property = it }

        return assertionTarget
    }
}
