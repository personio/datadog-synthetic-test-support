package com.personio.synthetics.config

import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsBrowserVariable
import com.datadog.api.client.v1.model.SyntheticsBrowserVariableType
import com.personio.synthetics.client.BrowserTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class VariableTest {
    private val browserTest = BrowserTest("Test", mock(), mock())

    @Test
    fun `useGlobalVariable adds a global variable to the test config`() {
        val variableName = "VARIABLE1"
        val variableType = SyntheticsBrowserVariableType.GLOBAL
        val variableId = "variable-id"
        val browserTest = Mockito.mock(BrowserTest::class.java)
        whenever(browserTest.getGlobalVariableId(variableName)).thenReturn(variableId)
        whenever(browserTest.config).thenReturn(SyntheticsBrowserTestConfig())

        browserTest.useGlobalVariable(variableName)

        val expectedResult = SyntheticsBrowserVariable()
            .name(variableName)
            .type(variableType)
            .id(variableId)
        assertEquals(expectedResult, browserTest.config!!.variables!![0])
    }

    @Test
    fun `useGlobalVariable throws error if the variable doesn't exist in Datadog`() {
        val variableName = "VARIABLE1"
        val browserTest = Mockito.mock(BrowserTest::class.java)
        whenever(browserTest.getGlobalVariableId(variableName)).thenReturn(null)
        whenever(browserTest.config).thenReturn(SyntheticsBrowserTestConfig())

        assertThrows<IllegalStateException> {
            browserTest.useGlobalVariable(variableName)
        }
    }

    @Test
    fun `useGlobalVariable changes the variable name casing to upper case`() {
        val variableName = "variable1"
        val variableType = SyntheticsBrowserVariableType.GLOBAL
        val variableId = "variable-id"

        val expectedVariableName = variableName.uppercase()
        val browserTest = Mockito.mock(BrowserTest::class.java)
        whenever(browserTest.getGlobalVariableId(expectedVariableName)).thenReturn(variableId)
        whenever(browserTest.config).thenReturn(SyntheticsBrowserTestConfig())

        browserTest.useGlobalVariable(variableName)

        val expectedResult = SyntheticsBrowserVariable()
            .name(expectedVariableName)
            .type(variableType)
            .id(variableId)
        assertEquals(expectedResult, browserTest.config!!.variables!![0])
    }

    @Test
    fun `textVariable adds a variable with text value`() {
        val variableName = "VARIABLE1"
        val value = "variableValue"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern(value)

        browserTest.textVariable(variableName, value)

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `numericPatternVariable changes the variable name casing to upper case`() {
        val variableName = "variable1"
        val characterLength = 5

        val expectedVariableName = variableName.uppercase()
        val expectedResult = syntheticBrowserVariable()
            .name(expectedVariableName)
            .pattern("{{ numeric($characterLength) }}")

        browserTest.numericPatternVariable(variableName, 5)

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `numericPatternVariable adds a variable of numeric pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ numeric($characterLength) }}")

        browserTest.numericPatternVariable(variableName, 5)

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `numericPatternVariable allows a value to be appended before the pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5
        val prefix = "prefix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("$prefix{{ numeric($characterLength) }}")

        browserTest.numericPatternVariable(
            name = variableName,
            characterLength = 5,
            prefix = prefix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `numericPatternVariable allows a value to be appended after the pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5
        val suffix = "suffix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ numeric($characterLength) }}$suffix")

        browserTest.numericPatternVariable(
            name = variableName,
            characterLength = 5,
            suffix = suffix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `alphabeticPatternVariable adds a variable of alphabetic pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ alphabetic($characterLength) }}")

        browserTest.alphabeticPatternVariable(variableName, 5)

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `alphabeticPatternVariable allows a value to be appended before the pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5
        val prefix = "prefix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("$prefix{{ alphabetic($characterLength) }}")

        browserTest.alphabeticPatternVariable(
            name = variableName,
            characterLength = 5,
            prefix = prefix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `alphabeticPatternVariable allows a value to be appended after the pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5
        val suffix = "suffix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ alphabetic($characterLength) }}$suffix")

        browserTest.alphabeticPatternVariable(
            name = variableName,
            characterLength = 5,
            suffix = suffix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `alphanumericPatternVariable adds a variable of alphabetic pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ alphanumeric($characterLength) }}")

        browserTest.alphanumericPatternVariable(variableName, 5)

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `alphanumericPatternVariable allows a value to be appended before the pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5
        val prefix = "prefix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("$prefix{{ alphanumeric($characterLength) }}")

        browserTest.alphanumericPatternVariable(
            name = variableName,
            characterLength = 5,
            prefix = prefix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `alphanumericPatternVariable allows a value to be appended after the pattern`() {
        val variableName = "VARIABLE1"
        val characterLength = 5
        val suffix = "suffix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ alphanumeric($characterLength) }}$suffix")

        browserTest.alphanumericPatternVariable(
            name = variableName,
            characterLength = 5,
            suffix = suffix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable adds a variable of date pattern`() {
        val variableName = "VARIABLE1"
        val dateValue = 5.minutes
        val dateFormat = "YYYY-MM-DD"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ date(${dateValue.inWholeMilliseconds}ms, $dateFormat) }}")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable allows a value to be appended before the pattern`() {
        val variableName = "VARIABLE1"
        val dateValue = 5.minutes
        val dateFormat = "YYYY-MM-DD"
        val prefix = "prefix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("$prefix{{ date(${dateValue.inWholeMilliseconds}ms, $dateFormat) }}")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat,
            prefix = prefix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable allows a value to be appended after the pattern`() {
        val variableName = "VARIABLE1"
        val dateValue = 5.minutes
        val dateFormat = "YYYY-MM-DD"
        val suffix = "suffix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ date(${dateValue.inWholeMilliseconds}ms, $dateFormat) }}$suffix")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat,
            suffix = suffix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable uses the next higher unit if the value is greater than or equal to 10_000_000`() {
        val variableName = "VARIABLE1"
        val dateValue = 10_000_000.minutes
        val dateFormat = "YYYY-MM-DD"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ date(${dateValue.inWholeHours}h, $dateFormat) }}")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable allows passing the duration less than 10_000_000 days`() {
        val variableName = "VARIABLE1"
        val dateValue = 9_999_999.days
        val dateFormat = "YYYY-MM-DD"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ date(${dateValue.inWholeDays}d, $dateFormat) }}")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable converts the duration supplied in microseconds to milliseconds`() {
        val variableName = "VARIABLE1"
        val dateValue = 1_000.microseconds
        val dateFormat = "YYYY-MM-DD"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ date(${dateValue.inWholeMilliseconds}ms, $dateFormat) }}")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable converts the duration supplied in nanoseconds to milliseconds`() {
        val variableName = "VARIABLE1"
        val dateValue = 1_000.nanoseconds
        val dateFormat = "YYYY-MM-DD"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ date(${dateValue.inWholeMilliseconds}ms, $dateFormat) }}")

        browserTest.datePatternVariable(
            name = variableName,
            duration = dateValue,
            format = dateFormat
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `datePatternVariable throws an exception if the duration is greater than or equal to 10_000_000 days`() {
        assertThrows<IllegalStateException> {
            browserTest.datePatternVariable(
                name = "VARIABLE1",
                duration = 10_000_000.days,
                format = "YYYY-MM-DD"
            )
        }
    }

    @Test
    fun `timestampPatternVariable adds a variable of timestamp pattern`() {
        val variableName = "VARIABLE1"
        val timestampValue = 5.seconds

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable allows a value to be appended before the pattern`() {
        val variableName = "VARIABLE1"
        val timestampValue = 5.seconds
        val prefix = "prefix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("$prefix{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue,
            prefix = prefix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable allows a value to be appended after the pattern`() {
        val variableName = "VARIABLE1"
        val timestampValue = 5.seconds
        val suffix = "suffix"

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}$suffix")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue,
            suffix = suffix
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable uses the next higher unit if the value is greater than or equal to 1_000_000_000`() {
        val variableName = "VARIABLE1"
        val timestampValue = 1_000_000_000.milliseconds

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeSeconds}, s) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable allows passing the duration less than 1_000_000_000 seconds`() {
        val variableName = "VARIABLE1"
        val timestampValue = 999_999_999.seconds

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeSeconds}, s) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable uses the unit ms or s if duration is passed in nanoseconds`() {
        val variableName = "VARIABLE1"
        val timestampValue = 1_000.nanoseconds

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable uses the unit ms or s if duration is passed in microseconds`() {
        val variableName = "VARIABLE1"
        val timestampValue = 1_000.microseconds

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable uses the unit ms or s if duration is passed in minutes`() {
        val variableName = "VARIABLE1"
        val timestampValue = 1.minutes

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable uses the unit ms or s if duration is passed in hours`() {
        val variableName = "VARIABLE1"
        val timestampValue = 1.hours

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable uses the unit ms or s if duration is passed in days`() {
        val variableName = "VARIABLE1"
        val timestampValue = 1.days

        val expectedResult = syntheticBrowserVariable()
            .name(variableName)
            .pattern("{{ timestamp(${timestampValue.inWholeMilliseconds}, ms) }}")

        browserTest.timestampPatternVariable(
            name = variableName,
            duration = timestampValue
        )

        assertEquals(expectedResult, browserTest.config?.variables?.get(0))
    }

    @Test
    fun `timestampPatternVariable throws exception if the duration is greater than or equal to 1_000_000_000 seconds`() {
        assertThrows<IllegalStateException> {
            browserTest.timestampPatternVariable(
                name = "VARIABLE1",
                duration = 1_000_000_000.seconds
            )
        }
    }

    @Test
    fun `fromVariable function returns the variable formatted according to datadog standards`() {
        val variableName = "TEST_VAR"
        val expectedVariableFormat = "{{ $variableName }}"

        val actualVariableFormat = fromVariable(variableName)

        assertEquals(expectedVariableFormat, actualVariableFormat)
    }

    @Test
    fun `fromVariable function returns the formatted variable after converting variable name to upper case`() {
        val variableName = "test_var"
        val expectedVariableFormat = "{{ ${variableName.uppercase()} }}"

        val actualVariableFormat = fromVariable(variableName)

        assertEquals(expectedVariableFormat, actualVariableFormat)
    }

    private fun syntheticBrowserVariable(): SyntheticsBrowserVariable =
        SyntheticsBrowserVariable()
            .type(SyntheticsBrowserVariableType.TEXT)
            .example("")
}
