package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPIStepSubtype
import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import com.datadog.api.client.v1.model.SyntheticsTestRequest
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
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StepBuilderTest {
    @Test
    fun `extract sets the extracted values properly`() {
        val stepBuilder =
            StepBuilder(
                "any_name",
                makeRequestBuilderHappyPathMock(),
                makeAssertionBuilderMock(),
                makeParsingOptionsBuilderMock(
                    SyntheticsParsingOptions(),
                ),
            )
        stepBuilder.request { }
        stepBuilder.extract("any_variable_name") {}
        val result = stepBuilder.build()

        assertEquals(1, result.extractedValues.count())
    }

    @Test
    fun `extract sets the extracted values to null when no parsing options provided`() {
        val stepBuilder =
            StepBuilder(
                "any_name",
                makeRequestBuilderHappyPathMock(),
                makeAssertionBuilderMock(),
                makeParsingOptionsBuilderMock(),
            )
        stepBuilder.request { }
        stepBuilder.extract("any_variable_name") {}
        val result = stepBuilder.build()

        assertNull(result.extractedValues)
    }

    @Test
    fun `request sets the request in SyntheticsAPIStep`() {
        val requestBuilderMock = makeRequestBuilderHappyPathMock()
        val stepBuilder = StepBuilder("any_name", requestBuilderMock)
        stepBuilder.request { }

        val result = stepBuilder.build()
        verify(requestBuilderMock, times(1)).build()
        assertNotNull(result.request)
    }

    @Test
    fun `assertions sets assertions properly`() {
        val assertionsMock =
            makeAssertionBuilderMock(
                listOf(SyntheticsAssertion(), SyntheticsAssertion()),
            )
        val stepBuilder = StepBuilder("any_name", RequestBuilder(), assertionsMock)
        stepBuilder.assertions { }
        stepBuilder.request { }
        val result = stepBuilder.build()

        verify(assertionsMock, times(1)).build()
        assertEquals(2, result.assertions.count())
    }

    @Test
    fun `build sets HTTP step subtype`() {
        val stepBuilder = StepBuilder("any_name", RequestBuilder())
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(SyntheticsAPIStepSubtype.HTTP, result.subtype)
    }

    @Test
    fun `build sets step name properly`() {
        val stepBuilder = StepBuilder("any_name", RequestBuilder())
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals("any_name", result.name)
    }

    @Test
    fun `build sets empty assertions by default`() {
        val assertionsMock = makeAssertionBuilderMock()
        val stepBuilder = StepBuilder("any_name", RequestBuilder(), assertionsMock)
        stepBuilder.assertions { }
        stepBuilder.request { }
        val result = stepBuilder.build()

        verify(assertionsMock, times(1)).build()
        assertTrue(result.assertions.isEmpty())
    }

    @Test
    fun `build throws IllegalStateException when request is null`() {
        val requestBuilderMock = Mockito.mock(RequestBuilder::class.java)
        whenever(requestBuilderMock.build())
            .thenReturn(null)
        val stepBuilder = StepBuilder("any_name", requestBuilderMock)

        assertThrows<IllegalStateException> {
            stepBuilder.build()
        }
    }

    @Test
    fun `build sets isCritical to null when allowFailure is false`() {
        val stepBuilder = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        stepBuilder.allowFailure = false
        stepBuilder.isCritical = true
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertNull(result.isCritical)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `build sets isCritical to the provided value when allowFailure is true`(isCritical: Boolean) {
        val stepBuilder = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        stepBuilder.allowFailure = true
        stepBuilder.isCritical = isCritical
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(isCritical, result.isCritical)
    }

    @Test
    fun `build sets step retry defaults if retry method was not called`() {
        val stepBuilder = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        stepBuilder.request { }
        val result = stepBuilder.build()

        assertEquals(SyntheticsTestOptionsRetry(), result.retry)
    }

    @Test
    fun `build sets step retry values if retry method was called`() {
        val stepBuilder = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        stepBuilder.request { }
        stepBuilder.retry(3, 3.seconds)
        val result = stepBuilder.build()

        assertEquals(SyntheticsTestOptionsRetry().count(3).interval(3000.0), result.retry)
    }

    @ParameterizedTest
    @MethodSource("generateRetryInvalidArgs")
    fun `retry throws error if invalid parameter is provided`(data: Pair<Long, Duration>) {
        val stepBuilder = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        assertThrows<IllegalArgumentException> {
            stepBuilder.retry(data.first, data.second)
        }
    }

    companion object {
        @JvmStatic
        fun generateRetryInvalidArgs() =
            listOf(
                7 to 3.seconds,
                3 to 10.seconds,
            )
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
