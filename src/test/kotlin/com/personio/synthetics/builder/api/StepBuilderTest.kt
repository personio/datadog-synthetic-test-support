package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPIStepSubtype
import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
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
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class StepBuilderTest {
    @Test
    fun `extract sets the extracted values properly`() {
        val sut = StepBuilder(
            "any_name",
            makeRequestBuilderHappyPathMock(),
            makeAssertionBuilderMock(),
            makeParsingOptionsBuilderMock(
                SyntheticsParsingOptions()
            )
        )
        sut.request { }
        sut.extract("any_variable_name") {}
        val result = sut.build()

        assertEquals(1, result.extractedValues.count())
    }

    @Test
    fun `extract sets the extracted values to null when no parsing options provided`() {
        val sut = StepBuilder(
            "any_name",
            makeRequestBuilderHappyPathMock(),
            makeAssertionBuilderMock(),
            makeParsingOptionsBuilderMock()
        )
        sut.request { }
        sut.extract("any_variable_name") {}
        val result = sut.build()

        assertNull(result.extractedValues)
    }

    @Test
    fun `request sets the request in SyntheticsAPIStep`() {
        val requestBuilderMock = makeRequestBuilderHappyPathMock()
        val sut = StepBuilder("any_name", requestBuilderMock)
        sut.request { }

        val result = sut.build()
        verify(requestBuilderMock, times(1)).build()
        assertNotNull(result.request)
    }

    @Test
    fun `assertions sets assertions properly`() {
        val assertionsMock = makeAssertionBuilderMock(
            listOf(SyntheticsAssertion(), SyntheticsAssertion())
        )
        val sut = StepBuilder("any_name", RequestBuilder(), assertionsMock)
        sut.assertions { }
        sut.request { }
        val result = sut.build()

        verify(assertionsMock, times(1)).build()
        assertEquals(2, result.assertions.count())
    }

    @Test
    fun `build sets HTTP step subtype`() {
        val sut = StepBuilder("any_name", RequestBuilder())
        sut.request { }
        val result = sut.build()

        assertEquals(SyntheticsAPIStepSubtype.HTTP, result.subtype)
    }

    @Test
    fun `build sets step name properly`() {
        val sut = StepBuilder("any_name", RequestBuilder())
        sut.request { }
        val result = sut.build()

        assertEquals("any_name", result.name)
    }

    @Test
    fun `build sets empty assertions by default`() {
        val assertionsMock = makeAssertionBuilderMock()
        val sut = StepBuilder("any_name", RequestBuilder(), assertionsMock)
        sut.assertions { }
        sut.request { }
        val result = sut.build()

        verify(assertionsMock, times(1)).build()
        assertTrue(result.assertions.isEmpty())
    }

    @Test
    fun `build throws IllegalStateException when request is null`() {
        val requestBuilderMock = Mockito.mock(RequestBuilder::class.java)
        whenever(requestBuilderMock.build())
            .thenReturn(null)
        val sut = StepBuilder("any_name", requestBuilderMock)

        assertThrows<IllegalStateException> {
            sut.build()
        }
    }

    @Test
    fun `build sets isCritical to null when allowFailure is false`() {
        val sut = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        sut.allowFailure = false
        sut.isCritical = true
        sut.request { }
        val result = sut.build()

        assertNull(result.isCritical)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `build sets isCritical to the provided value when allowFailure is true`(isCritical: Boolean) {
        val sut = StepBuilder("any_name", makeRequestBuilderHappyPathMock())
        sut.allowFailure = true
        sut.isCritical = isCritical
        sut.request { }
        val result = sut.build()

        assertEquals(isCritical, result.isCritical)
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
