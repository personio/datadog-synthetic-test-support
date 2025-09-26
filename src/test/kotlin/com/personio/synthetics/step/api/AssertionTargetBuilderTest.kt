package com.personio.synthetics.step.api

import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionTargetValue
import com.datadog.api.client.v1.model.SyntheticsAssertionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class AssertionTargetBuilderTest {
    @Test
    fun `assertionBuilder should create SyntheticsAssertionTarget with String target`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.STATUS_CODE
                operator = SyntheticsAssertionOperator.IS
                target = "200"
            }

        val result = builder.assertionBuilder()

        assertEquals(SyntheticsAssertionType.STATUS_CODE, result.type)
        assertEquals(SyntheticsAssertionOperator.IS, result.operator)
        assertEquals(SyntheticsAssertionTargetValue("200"), result.target)
        assertNull(result.property)
    }

    @Test
    fun `assertionBuilder should create SyntheticsAssertionTarget with Int target`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.RESPONSE_TIME
                operator = SyntheticsAssertionOperator.LESS_THAN
                target = 5000
            }

        val result = builder.assertionBuilder()

        assertEquals(SyntheticsAssertionType.RESPONSE_TIME, result.type)
        assertEquals(SyntheticsAssertionOperator.LESS_THAN, result.operator)
        assertEquals(SyntheticsAssertionTargetValue(5000.0), result.target)
        assertNull(result.property)
    }

    @Test
    fun `assertionBuilder should create SyntheticsAssertionTarget with property`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.HEADER
                operator = SyntheticsAssertionOperator.CONTAINS
                target = "application/json"
                property = "content-type"
            }

        val result = builder.assertionBuilder()

        assertEquals(SyntheticsAssertionType.HEADER, result.type)
        assertEquals(SyntheticsAssertionOperator.CONTAINS, result.operator)
        assertEquals(SyntheticsAssertionTargetValue("application/json"), result.target)
        assertEquals("content-type", result.property)
    }

    @Test
    fun `assertionBuilder should throw IllegalArgumentException when type is null`() {
        val builder =
            AssertionTargetBuilder().apply {
                operator = SyntheticsAssertionOperator.IS
                target = "200"
            }

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                builder.assertionBuilder()
            }

        assertEquals("Type is required", exception.message)
    }

    @Test
    fun `assertionBuilder should throw IllegalArgumentException when operator is null`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.STATUS_CODE
                target = "200"
            }

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                builder.assertionBuilder()
            }

        assertEquals("Operator is required", exception.message)
    }

    @Test
    fun `assertionBuilder should throw IllegalArgumentException when target is null`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.STATUS_CODE
                operator = SyntheticsAssertionOperator.IS
            }

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                builder.assertionBuilder()
            }

        assertEquals("Target must be String or Int", exception.message)
    }

    @Test
    fun `assertionBuilder should throw IllegalArgumentException when target is not String or Int`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.STATUS_CODE
                operator = SyntheticsAssertionOperator.IS
                target = 123.45 // Double is not supported
            }

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                builder.assertionBuilder()
            }

        assertEquals("Target must be String or Int", exception.message)
    }

    @Test
    fun `assertionBuilder should handle empty String target`() {
        val builder =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.BODY
                operator = SyntheticsAssertionOperator.IS
                target = ""
            }

        val result = builder.assertionBuilder()

        assertEquals(SyntheticsAssertionTargetValue(""), result.target)
    }

    @Test
    fun `assertionBuilder should handle all SyntheticsAssertionType values`() {
        val assertionTypes =
            listOf(
                SyntheticsAssertionType.STATUS_CODE,
                SyntheticsAssertionType.RESPONSE_TIME,
                SyntheticsAssertionType.HEADER,
                SyntheticsAssertionType.BODY,
                SyntheticsAssertionType.CERTIFICATE,
            )

        assertionTypes.forEach { assertionType ->
            val builder =
                AssertionTargetBuilder().apply {
                    type = assertionType
                    operator = SyntheticsAssertionOperator.IS
                    target = "test"
                }

            val result = builder.assertionBuilder()
            assertEquals(assertionType, result.type)
        }
    }

    @Test
    fun `assertionBuilder should handle all SyntheticsAssertionOperator values`() {
        val operators =
            listOf(
                SyntheticsAssertionOperator.IS,
                SyntheticsAssertionOperator.IS_NOT,
                SyntheticsAssertionOperator.LESS_THAN,
                SyntheticsAssertionOperator.MORE_THAN,
                SyntheticsAssertionOperator.CONTAINS,
                SyntheticsAssertionOperator.DOES_NOT_CONTAIN,
            )

        operators.forEach { operator ->
            val builder =
                AssertionTargetBuilder().apply {
                    type = SyntheticsAssertionType.STATUS_CODE
                    this.operator = operator
                    target = 200
                }

            val result = builder.assertionBuilder()
            assertEquals(operator, result.operator)
        }
    }

    @Test
    fun `assertionBuilder should handle property correctly`() {
        // Test with property set
        val builderWithProperty =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.HEADER
                operator = SyntheticsAssertionOperator.CONTAINS
                target = "value"
                property = "custom-header"
            }

        val resultWithProperty = builderWithProperty.assertionBuilder()
        assertEquals("custom-header", resultWithProperty.property)

        // Test without property set
        val builderWithoutProperty =
            AssertionTargetBuilder().apply {
                type = SyntheticsAssertionType.STATUS_CODE
                operator = SyntheticsAssertionOperator.IS
                target = 200
            }

        val resultWithoutProperty = builderWithoutProperty.assertionBuilder()
        assertNull(resultWithoutProperty.property)
    }
}
