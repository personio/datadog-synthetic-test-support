package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsAssertionJSONPathOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionJSONPathTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionJSONPathTargetTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionType

class AssertionsBuilder {
    private val assertions = mutableListOf<SyntheticsAssertion>()

    fun build(): List<SyntheticsAssertion> {
        return assertions
    }

    /**
     * Asserts the response status code
     * @param code Status code
     */
    fun statusCode(code: Int) {
        target(
            SyntheticsAssertionType.STATUS_CODE,
            SyntheticsAssertionOperator.IS,
            code
        )
    }

    /**
     * Asserts that the response header contains the given value
     * @param name Header name
     * @param value Value to look for
     */
    fun headerContains(name: String, value: String) {
        assertions.add(
            SyntheticsAssertion(
                SyntheticsAssertionTarget()
                    .property(name)
                    .operator(SyntheticsAssertionOperator.CONTAINS)
                    .type(SyntheticsAssertionType.HEADER)
                    .target(value)
            )
        )
    }

    /**
     * Asserts that the value at the JSON path in the response body contains the given value
     * @param jsonPath JSON path
     * @param targetValue Value to look for
     */
    fun bodyContainsJsonPath(jsonPath: String, targetValue: Any) {
        assertions.add(
            SyntheticsAssertion(
                SyntheticsAssertionJSONPathTarget()
                    .operator(SyntheticsAssertionJSONPathOperator.VALIDATES_JSON_PATH)
                    .type(SyntheticsAssertionType.BODY)
                    .target(
                        SyntheticsAssertionJSONPathTargetTarget()
                            .jsonPath(jsonPath)
                            .operator("contains")
                            .targetValue(targetValue)
                    )
            )
        )
    }

    /**
     * Asserts that the response body contains the given value
     * @param value Value to look for
     */
    fun bodyContains(value: Any) {
        target(
            SyntheticsAssertionType.BODY,
            SyntheticsAssertionOperator.CONTAINS,
            value
        )
    }

    /**
     * Asserts that the response body does not contain the given value
     * @param value Value to look for
     */
    fun bodyDoesNotContain(value: Any) {
        target(
            SyntheticsAssertionType.BODY,
            SyntheticsAssertionOperator.DOES_NOT_CONTAIN,
            value
        )
    }

    /**
     * Adds an assertion for a given assertion type, operator and target
     * @param type Assertion type
     * @param operator Assertion operator
     * @param target Target
     */
    private fun target(type: SyntheticsAssertionType, operator: SyntheticsAssertionOperator, target: Any) {
        assertions.add(
            SyntheticsAssertion(
                SyntheticsAssertionTarget(
                    operator,
                    target,
                    type
                )
            )
        )
    }
}
