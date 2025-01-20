package com.personio.synthetics.config

import com.datadog.api.client.v1.model.SyntheticsBrowserVariable
import com.datadog.api.client.v1.model.SyntheticsBrowserVariableType
import com.personio.synthetics.client.BrowserTest
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Creates a local variable with the supplied string as a value
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param value Supply the text to be set for the variable
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.textVariable(
    name: String,
    value: String,
) = apply {
    addLocalVariable(name, value)
}

/**
 * Creates a local variable with the numeric pattern
 * The value of the variable will be a generated random numeric string with n digits
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param characterLength Length of the random numeric value that's generated
 * @param prefix String to be appended before the pattern
 * @param suffix String to be appended after the pattern
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.numericPatternVariable(
    name: String,
    characterLength: Int,
    prefix: String = "",
    suffix: String = "",
) = apply {
    addLocalVariable(name, "$prefix{{ numeric($characterLength) }}$suffix")
}

/**
 * Creates a local variable with the alphabetic pattern
 * The value of the variable will be a generated random alphabetic string with n characters
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param characterLength Length of the random alphabetic value that's generated
 * @param prefix String to be appended before the pattern
 * @param suffix String to be appended after the pattern
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.alphabeticPatternVariable(
    name: String,
    characterLength: Int,
    prefix: String = "",
    suffix: String = "",
) = apply {
    addLocalVariable(name, "$prefix{{ alphabetic($characterLength) }}$suffix")
}

/**
 * Creates a local variable with the alphanumeric pattern
 * The value of the variable will be a generated random alphanumeric string with n characters
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param characterLength Length of the random alphanumeric value that's generated
 * @param prefix String to be appended before the pattern
 * @param suffix String to be appended after the pattern
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.alphanumericPatternVariable(
    name: String,
    characterLength: Int,
    prefix: String = "",
    suffix: String = "",
) = apply {
    addLocalVariable(name, "$prefix{{ alphanumeric($characterLength) }}$suffix")
}

/**
 * Creates a local variable with the date pattern
 * The value of the variable will be a generated date in UTC in one of the accepted formats with a value corresponding to the date the test is initiated at +/- nunit
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param duration The duration (+ or -) to be added to or subtracted from the time by which the test is run to generate the date
 * @param format Pass one of the accepted format in which the date needs to be generated
 * @param prefix String to be appended before the pattern
 * @param suffix String to be appended after the pattern
 * The accepted formats are according to https://date-fns.org/v1.29.0/docs/format
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.datePatternVariable(
    name: String,
    duration: Duration,
    format: String,
    prefix: String = "",
    suffix: String = "",
) = apply {
    val (scaledValue, unit) =
        checkNotNull(getScaledDate(duration)) {
            "The passed duration should be between -1_000_000 and 1_000_000 days for the date pattern variable $name."
        }
    addLocalVariable(name, "$prefix{{ date($scaledValue$unit, $format) }}$suffix")
}

/**
 * Creates a local variable with the timestamp pattern
 * The value of the variable will be a generated timestamp in one of the accepted formats with a value corresponding to the timestamp the test is initiated at +/- n unit.
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param duration The duration (+ or -) to be added to or subtracted from the time by which the test is run to generate the date
 * @param prefix String to be appended before the pattern
 * @param suffix String to be appended after the pattern
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.timestampPatternVariable(
    name: String,
    duration: Duration,
    prefix: String = "",
    suffix: String = "",
) = apply {
    val (scaledValue, unit) =
        checkNotNull(getScaledTimestamp(duration)) {
            "The passed duration should be between -999_999_999 and 999_999_999 seconds for the timestamp pattern variable $name."
        }
    addLocalVariable(name, "$prefix{{ timestamp($scaledValue, $unit) }}$suffix")
}

/**
 * Create a local variable with an uuid
 * The value generated will be a version 4 universally unique identifier.
 * @param name Name of the variable. The name would be converted to upper case letters
 * @param prefix String to be appended before the pattern
 * @param suffix String to be appended after the pattern
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.uuidVariable(
    name: String,
    prefix: String = "",
    suffix: String = "",
) = apply {
    addLocalVariable(name, "$prefix{{ uuid }}$suffix")
}

/**
 * Uses the global variable in the test
 * @param name Name of the existing global variable. The name would be converted to upper case letters
 * @return BrowserTest object with this added global variable
 */
fun BrowserTest.useGlobalVariable(name: String) =
    apply {
        val variableName = name.uppercase()
        val variableId = getGlobalVariableId(variableName)
        checkNotNull(variableId) { "The global variable $name to be used in the test doesn't exist in DataDog." }
        config
            ?.addVariablesItem(
                SyntheticsBrowserVariable()
                    .name(variableName)
                    .id(variableId)
                    .type(SyntheticsBrowserVariableType.GLOBAL),
            )
    }

/**
 * Used for formatting the variable according to Datadog standards
 * @param variableName The name of the variable to be used
 * @return Formatted variable name according to the Datadog standards
 */
fun fromVariable(variableName: String): String = "{{ ${variableName.uppercase()} }}"

private fun BrowserTest.addLocalVariable(
    name: String,
    pattern: String,
) {
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name.uppercase())
                .type(SyntheticsBrowserVariableType.TEXT)
                .pattern(pattern)
                .example(""),
        )
}

private fun getScaledDate(value: Duration): Pair<Long, String>? =
    value.getScaledValue(
        sequenceOf(DurationUnit.MILLISECONDS, DurationUnit.SECONDS, DurationUnit.MINUTES, DurationUnit.HOURS, DurationUnit.DAYS),
        1_000_000,
    )

private fun getScaledTimestamp(value: Duration): Pair<Long, String>? =
    value.getScaledValue(
        sequenceOf(DurationUnit.MILLISECONDS, DurationUnit.SECONDS),
        999_999_999,
    )

private fun Duration.getScaledValue(
    sequence: Sequence<DurationUnit>,
    limit: Long,
): Pair<Long, String>? =
    sequence
        .map { unit -> this.toLong(unit) to unit.toDatadogDurationUnit() }
        .firstOrNull { (scaled, _) -> scaled.absoluteValue <= limit }

private fun DurationUnit.toDatadogDurationUnit(): String {
    return when (this) {
        DurationUnit.MILLISECONDS -> "ms"
        DurationUnit.SECONDS -> "s"
        DurationUnit.MINUTES -> "m"
        DurationUnit.HOURS -> "h"
        DurationUnit.DAYS -> "d"
        else -> throw IllegalArgumentException("The given duration unit is not supported.")
    }
}

/**
 * Checks if the given string is a DataDog variable
 * @return true if it's a variable else false
 */
internal fun String.isDatadogVariable(): Boolean = this.startsWith("{{")
