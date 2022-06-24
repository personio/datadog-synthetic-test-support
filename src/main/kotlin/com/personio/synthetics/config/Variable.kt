package com.personio.synthetics.config

import com.datadog.api.v1.client.model.SyntheticsBrowserVariable
import com.datadog.api.v1.client.model.SyntheticsBrowserVariableType
import com.personio.synthetics.client.BrowserTest
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Creates a local variable with the numeric pattern
 * The value of the variable will be a generated random numeric string with n digits
 * @param name name of the variable. The name would be converted to upper case letters
 * @param characterLength length of the random numeric value that's generated
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.numericPatternVariable(name: String, characterLength: Int) = apply {
    addLocalVariable(name, "{{ numeric($characterLength) }}")
}

/**
 * Creates a local variable with the alphabetic pattern
 * The value of the variable will be a generated random alphabetic string with n characters
 * @param name name of the variable. The name would be converted to upper case letters
 * @param characterLength length of the random alphabetic value that's generated
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.alphabeticPatternVariable(name: String, characterLength: Int) = apply {
    addLocalVariable(name, "{{ alphabetic($characterLength) }}")
}

/**
 * Creates a local variable with the alphanumeric pattern
 * The value of the variable will be a generated random alphanumeric string with n characters
 * @param name name of the variable. The name would be converted to upper case letters
 * @param characterLength length of the random alphanumeric value that's generated
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.alphanumericPatternVariable(name: String, characterLength: Int) = apply {
    addLocalVariable(name, "{{ alphanumeric($characterLength) }}")
}

/**
 * Creates a local variable with the date pattern
 * The value of the variable will be a generated date in UTC in one of the accepted formats with a value corresponding to the date the test is initiated at +/- nunit.
 * @param name name of the variable. The name would be converted to upper case letters
 * @param duration The duration (+ or -) to be added to or subtracted from the time by which the test is run to generate the date
 * @param format Pass one of the accepted format in which the date needs to be generated
 * The accepted formats are according to https://date-fns.org/v1.29.0/docs/format
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.datePatternVariable(name: String, duration: Duration, format: String) = apply {
    val (scaledValue, unit) = checkNotNull(getScaledDate(duration)) {
        "The passed duration should be less than 10_000_000 days for the date pattern variable $name"
    }
    addLocalVariable(name, "{{ date($scaledValue$unit, $format) }}")
}

/**
 * Creates a local variable with the timestamp pattern
 * The value of the variable will be a generated timestamp in one of the accepted formats with a value corresponding to the timestamp the test is initiated at +/- n unit.
 * @param name name of the variable. The name would be converted to upper case letters
 * @param duration The duration (+ or -) to be added to or subtracted from the time by which the test is run to generate the date
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.timestampPatternVariable(name: String, duration: Duration) = apply {
    val (scaledValue, unit) = checkNotNull(getScaledTimestamp(duration)) {
        "The passed duration should be less than 1_000_000_000 seconds for the timestamp pattern variable $name"
    }
    addLocalVariable(name, "{{ timestamp($scaledValue, $unit) }}")
}

/**
 * Use the global variable in the test
 * @param name name of the existing global variable. The name would be converted to upper case letters
 * @return BrowserTest object with this added global variable
 */
fun BrowserTest.useGlobalVariable(name: String) = apply {
    val variableName = name.uppercase()
    val variableId = getGlobalVariableId(variableName)
    checkNotNull(variableId) { "The global variable $name to be used in the test doesn't exist in DataDog" }
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(variableName)
                .id(variableId)
                .type(SyntheticsBrowserVariableType.GLOBAL)
        )
}

private fun BrowserTest.addLocalVariable(name: String, pattern: String) {
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name.uppercase())
                .type(SyntheticsBrowserVariableType.TEXT)
                .pattern(pattern)
                .example("")
        )
}

private fun getScaledDate(value: Duration): Pair<Long, String>? =
    value.getScaledValue(
        sequenceOf(DurationUnit.MILLISECONDS, DurationUnit.SECONDS, DurationUnit.MINUTES, DurationUnit.HOURS, DurationUnit.DAYS),
        10_000_000
    )

private fun getScaledTimestamp(value: Duration): Pair<Long, String>? =
    value.getScaledValue(
        sequenceOf(DurationUnit.MILLISECONDS, DurationUnit.SECONDS),
        1_000_000_000
    )

private fun Duration.getScaledValue(sequence: Sequence<DurationUnit>, limit: Long): Pair<Long, String>? =
    sequence
        .map { unit -> this.toLong(unit) to unit.toDatadogDurationUnit() }
        .firstOrNull { (scaled, _) -> scaled.absoluteValue < limit }

private fun DurationUnit.toDatadogDurationUnit(): String {
    return when (this) {
        DurationUnit.MILLISECONDS -> "ms"
        DurationUnit.SECONDS -> "s"
        DurationUnit.MINUTES -> "m"
        DurationUnit.HOURS -> "h"
        DurationUnit.DAYS -> "d"
        else -> throw IllegalArgumentException("The given duration unit is not supported")
    }
}
