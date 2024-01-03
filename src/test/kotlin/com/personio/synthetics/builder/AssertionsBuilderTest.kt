package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsAssertionJSONPathOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionJSONPathTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionJSONPathTargetTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionTarget
import com.datadog.api.client.v1.model.SyntheticsAssertionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AssertionsBuilderTest {
    private val assertionsBuilder = AssertionsBuilder()

    @Test
    fun `statusCode adds assertion that checks if the status code equals to the provided value`() {
        assertionsBuilder.statusCode(200)
        val result = assertionsBuilder.build()

        assertEquals(
            SyntheticsAssertion(
                SyntheticsAssertionTarget()
                    .operator(SyntheticsAssertionOperator.IS)
                    .target(200)
                    .type(SyntheticsAssertionType.STATUS_CODE),
            ),
            result.first(),
        )
    }

    @Test
    fun `headerContains adds assertion that checks if the header contains the value`() {
        assertionsBuilder.headerContains("any_header", "any_value")
        val result = assertionsBuilder.build()

        assertEquals(
            SyntheticsAssertion(
                SyntheticsAssertionTarget()
                    .property("any_header")
                    .operator(SyntheticsAssertionOperator.CONTAINS)
                    .target("any_value")
                    .type(SyntheticsAssertionType.HEADER),
            ),
            result.first(),
        )
    }

    @Test
    fun `bodyContainsJsonPath adds assertion that validates the value stored in the given json path against the value provided`() {
        assertionsBuilder.bodyContainsJsonPath("any_json_path", "any_value")
        val result = assertionsBuilder.build()

        assertEquals(
            SyntheticsAssertion(
                SyntheticsAssertionJSONPathTarget()
                    .operator(SyntheticsAssertionJSONPathOperator.VALIDATES_JSON_PATH)
                    .type(SyntheticsAssertionType.BODY)
                    .target(
                        SyntheticsAssertionJSONPathTargetTarget()
                            .jsonPath("any_json_path")
                            .operator("contains")
                            .targetValue("any_value"),
                    ),
            ),
            result.first(),
        )
    }

    @Test
    fun `bodyContains adds assertion that checks if the body contains the given raw value`() {
        assertionsBuilder.bodyContains("any_value")
        val result = assertionsBuilder.build()

        assertEquals(
            SyntheticsAssertion(
                SyntheticsAssertionTarget()
                    .operator(SyntheticsAssertionOperator.CONTAINS)
                    .target("any_value")
                    .type(SyntheticsAssertionType.BODY),
            ),
            result.first(),
        )
    }

    @Test
    fun `bodyDoesNotContains adds assertion that checks if the body does not contain the given raw value`() {
        assertionsBuilder.bodyDoesNotContain("any_value")
        val result = assertionsBuilder.build()

        assertEquals(
            SyntheticsAssertion(
                SyntheticsAssertionTarget()
                    .operator(SyntheticsAssertionOperator.DOES_NOT_CONTAIN)
                    .target("any_value")
                    .type(SyntheticsAssertionType.BODY),
            ),
            result.first(),
        )
    }

    @Test
    fun `build returns list of assertions`() {
        assertionsBuilder.statusCode(200)
        assertionsBuilder.headerContains("any_header", "any_value")
        assertionsBuilder.bodyContainsJsonPath("any_json_path", "any_value")
        assertionsBuilder.bodyContains("any_value")
        val result = assertionsBuilder.build()

        assertEquals(4, result.count())
    }
}
