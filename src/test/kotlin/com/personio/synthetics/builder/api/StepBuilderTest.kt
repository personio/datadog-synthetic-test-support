package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPITestStepSubtype
import com.datadog.api.client.v1.model.SyntheticsAPIWaitStep
import com.datadog.api.client.v1.model.SyntheticsAPIWaitStepSubtype
import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import com.datadog.api.client.v1.model.SyntheticsTestRequest
import com.personio.synthetics.TEST_STEP_NAME
import com.personio.synthetics.builder.AssertionsBuilder
import com.personio.synthetics.builder.RequestBuilder
import com.personio.synthetics.builder.parsing.ParsingOptionsBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.seconds

class StepBuilderTest {
    @Test
    fun `extract sets the extracted values properly`() {
        val stepBuilder =
            StepBuilder(
                TEST_STEP_NAME,
                makeRequestBuilderHappyPathMock(),
                makeAssertionBuilderMock(),
                makeParsingOptionsBuilderMock(
                    SyntheticsParsingOptions(),
                ),
            )
        stepBuilder.request { }
        stepBuilder.extract("any_variable_name") {}
        val result = stepBuilder.build()

        assertEquals(1, result.syntheticsAPITestStep.extractedValues.count())
    }

    @Test
    fun `extract sets the extracted values to null when no parsing options provided`() {
        val stepBuilder =
            StepBuilder(
                TEST_STEP_NAME,
                makeRequestBuilderHappyPathMock(),
                makeAssertionBuilderMock(),
                makeParsingOptionsBuilderMock(),
            )
        stepBuilder.request { }
        stepBuilder.extract("any_variable_name") {}
        val result = stepBuilder.build()

        assertNull(result.syntheticsAPITestStep.extractedValues)
    }

    @Test
    fun `request sets the request in SyntheticsAPIStep`() {
        val requestBuilderMock = makeRequestBuilderHappyPathMock()
        val stepBuilder = StepBuilder(TEST_STEP_NAME, requestBuilderMock)
        stepBuilder.request { }

        val result = stepBuilder.build()
        verify(requestBuilderMock, times(1)).build()
        assertNotNull(result.syntheticsAPITestStep.request)
    }

    @Test
    fun `build creates a wait step when wait duration is provided`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME)
        stepBuilder.wait(30)

        val result = stepBuilder.build()

        assertEquals(
            SyntheticsAPIWaitStep(TEST_STEP_NAME, SyntheticsAPIWaitStepSubtype.WAIT, 30),
            result.syntheticsAPIWaitStep,
        )
    }

    @Test
    fun `wait throws exception for invalid duration`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME)

        assertThrows<IllegalArgumentException> {
            stepBuilder.wait(300)
        }
    }

    @Test
    fun `assertions sets assertions properly`() {
        val assertionsMock =
            makeAssertionBuilderMock(
                listOf(SyntheticsAssertion(), SyntheticsAssertion()),
            )
        val stepBuilder = StepBuilder(TEST_STEP_NAME, RequestBuilder(), assertionsMock)
        stepBuilder.assertions { }
        stepBuilder.request { }
        val result = stepBuilder.build()

        verify(assertionsMock, times(1)).build()
        assertEquals(2, result.syntheticsAPITestStep.assertions.count())
    }

    @Test
    fun `build sets HTTP step subtype`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, RequestBuilder())
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(SyntheticsAPITestStepSubtype.HTTP, result.syntheticsAPITestStep.subtype)
    }

    @Test
    fun `build sets step name properly`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, RequestBuilder())
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(TEST_STEP_NAME, result.syntheticsAPITestStep.name)
    }

    @Test
    fun `build sets empty assertions by default`() {
        val assertionsMock = makeAssertionBuilderMock()
        val stepBuilder = StepBuilder(TEST_STEP_NAME, RequestBuilder(), assertionsMock)
        stepBuilder.assertions { }
        stepBuilder.request { }
        val result = stepBuilder.build()

        verify(assertionsMock, times(1)).build()
        assertTrue(result.syntheticsAPITestStep.assertions.isEmpty())
    }

    @Test
    fun `build throws IllegalStateException when both request & wait duration are null`() {
        val requestBuilderMock = Mockito.mock(RequestBuilder::class.java)
        whenever(requestBuilderMock.build())
            .thenReturn(null)
        val stepBuilder = StepBuilder(TEST_STEP_NAME, requestBuilderMock)

        val exception =
            assertThrows<IllegalStateException> {
                stepBuilder.build()
            }
        assertEquals(exception.message, "Provide either of Request or Wait duration.")
    }

    @Test
    fun `build throws IllegalStateException when both request & wait duration are provided`() {
        val requestBuilderMock = makeRequestBuilderHappyPathMock()
        val stepBuilder = StepBuilder(TEST_STEP_NAME, requestBuilderMock)
        stepBuilder.request { }
        stepBuilder.wait(30)

        val exception =
            assertThrows<IllegalStateException> {
                stepBuilder.build()
            }
        assertEquals(exception.message, "Only one of Request or Wait duration should be provided.")
    }

    @Test
    fun `build sets isCritical to null when allowFailure is false`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, makeRequestBuilderHappyPathMock())
        stepBuilder.allowFailure = false
        stepBuilder.isCritical = true
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertNull(result.syntheticsAPITestStep.isCritical)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `build sets isCritical to the provided value when allowFailure is true`(isCritical: Boolean) {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, makeRequestBuilderHappyPathMock())
        stepBuilder.allowFailure = true
        stepBuilder.isCritical = isCritical
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(isCritical, result.syntheticsAPITestStep.isCritical)
    }

    @Test
    fun `build uses default step retry configuration if retry method was not called`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, makeRequestBuilderHappyPathMock())
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(SyntheticsTestOptionsRetry(), result.syntheticsAPITestStep.retry)
    }

    @Test
    fun `build sets step retry count and interval if retry method was called`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, makeRequestBuilderHappyPathMock())
        stepBuilder.request { }
        stepBuilder.retry(3, 3.seconds)
        val result = stepBuilder.build()

        assertEquals(SyntheticsTestOptionsRetry().count(3).interval(3000.0), result.syntheticsAPITestStep.retry)
    }

    @Test
    fun `retry throws exception for retry count value bigger than 5`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, makeRequestBuilderHappyPathMock())
        assertThrows<IllegalArgumentException> {
            stepBuilder.retry(7, 3.seconds)
        }
    }

    @Test
    fun `retry throws exception for retry interval value bigger than 5 seconds`() {
        val stepBuilder = StepBuilder(TEST_STEP_NAME, makeRequestBuilderHappyPathMock())
        assertThrows<IllegalArgumentException> {
            stepBuilder.retry(3, 10.seconds)
        }
    }

    private fun makeRequestBuilderHappyPathMock(): RequestBuilder {
        val mock = Mockito.mock(RequestBuilder::class.java)
        whenever(mock.build()).thenReturn(SyntheticsTestRequest())
        return mock
    }

    private fun makeAssertionBuilderMock(assertionsToReturn: List<SyntheticsAssertion> = listOf()): AssertionsBuilder {
        val mock = Mockito.mock(AssertionsBuilder::class.java)
        whenever(mock.build()).thenReturn(assertionsToReturn)
        return mock
    }

    private fun makeParsingOptionsBuilderMock(parsingOptionsToReturn: SyntheticsParsingOptions? = null): ParsingOptionsBuilder {
        val mock = Mockito.mock(ParsingOptionsBuilder::class.java)
        whenever(mock.build()).thenReturn(parsingOptionsToReturn)
        return mock
    }
}
