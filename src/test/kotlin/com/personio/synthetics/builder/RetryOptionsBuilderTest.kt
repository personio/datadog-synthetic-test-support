package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RetryOptionsBuilderTest {
    @Test
    fun `builder creates retry options object with correct configuration`() {
        val retryOptionsBuilder = RetryOptionsBuilder()
        retryOptionsBuilder.count = 2
        retryOptionsBuilder.intervalMs = 3500.0

        Assertions.assertEquals(SyntheticsTestOptionsRetry().count(2).interval(3500.0), retryOptionsBuilder.build())
    }
}
