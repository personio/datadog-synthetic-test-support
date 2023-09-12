package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPIStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class StepsBuilderTest {
    @Test
    fun `step adds a step to the list`() {
        val sut = StepsBuilder()
        sut.step("any_name", makeStepBuilderMock(SyntheticsAPIStep())) {}
        sut.step("any_name", makeStepBuilderMock(SyntheticsAPIStep())) {}
        val result = sut.build()

        assertEquals(2, result.count())
    }

    private fun makeStepBuilderMock(stepToReturn: SyntheticsAPIStep): StepBuilder {
        val mock = Mockito.mock(StepBuilder::class.java)
        whenever(mock.build()).thenReturn(stepToReturn)
        return mock
    }
}
