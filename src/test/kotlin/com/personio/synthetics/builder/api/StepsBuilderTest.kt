package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPIStep
import com.personio.synthetics.TEST_STEP_NAME
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class StepsBuilderTest {
    @Test
    fun `step adds a step to the list`() {
        val stepsBuilder = StepsBuilder()
        stepsBuilder.step(TEST_STEP_NAME, makeStepBuilderMock(SyntheticsAPIStep())) {}
        stepsBuilder.step(TEST_STEP_NAME, makeStepBuilderMock(SyntheticsAPIStep())) {}
        val result = stepsBuilder.build()

        assertEquals(2, result.count())
    }

    private fun makeStepBuilderMock(stepToReturn: SyntheticsAPIStep): StepBuilder {
        val mock = Mockito.mock(StepBuilder::class.java)
        whenever(mock.build()).thenReturn(stepToReturn)
        return mock
    }
}
