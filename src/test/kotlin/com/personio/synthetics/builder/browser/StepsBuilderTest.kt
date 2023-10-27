package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class StepsBuilderTest {
    @Test
    fun `step adds a step to the list`() {
        val stepsBuilder = StepsBuilder()
        stepsBuilder.step("any_name", makeStepBuilderMock(SyntheticsStep())) {}
        stepsBuilder.step("any_name", makeStepBuilderMock(SyntheticsStep())) {}
        val result = stepsBuilder.build()

        assertEquals(2, result.count())
    }

    private fun makeStepBuilderMock(stepToReturn: SyntheticsStep): StepBuilder {
        val mock = Mockito.mock(StepBuilder::class.java)
        whenever(mock.build()).thenReturn(stepToReturn)
        return mock
    }
}
