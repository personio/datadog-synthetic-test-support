package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsTestOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsMonitorOptions
import com.datadog.api.client.v1.model.SyntheticsTestOptionsRetry
import com.datadog.api.client.v1.model.SyntheticsTestOptionsScheduling
import com.datadog.api.client.v1.model.SyntheticsTestOptionsSchedulingTimeframe
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.Defaults
import com.personio.synthetics.domain.SyntheticTestParameters
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import com.personio.synthetics.model.config.SyntheticsDeviceID
import com.personio.synthetics.model.config.Timeframe
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * An abstract class that wraps common functions for Synthetic Browser and API tests
 */
abstract class SyntheticTestBuilder(
    open val name: String,
    defaults: Defaults,
    private val apiClient: SyntheticsApiClient,
) {
    protected var parameters: SyntheticTestParameters
    protected var options: SyntheticsTestOptions
    protected var status: SyntheticsTestPauseStatus? = null

    init {
        parameters =
            SyntheticTestParameters(
                message = "",
                locations = defaults.runLocations,
                tags = mutableListOf(),
            )

        options =
            SyntheticsTestOptions()
                .addDeviceIdsItem(SyntheticsDeviceID.CHROME_LAPTOP_LARGE.value)
                .tickEvery(defaults.testFrequency / 1000)
                .minFailureDuration(defaults.minFailureDuration / 1000)
                .minLocationFailed(defaults.minLocationFailed)
                .retry(
                    SyntheticsTestOptionsRetry()
                        .count(defaults.retryCount)
                        .interval(defaults.retryInterval),
                )
                .monitorOptions(
                    SyntheticsTestOptionsMonitorOptions(),
                )
    }

    /**
     * Sets the monitor name for the synthetic test
     * @param monitorName The monitor name of the test
     */
    fun monitorName(monitorName: String) {
        options.monitorName = monitorName
    }

    /**
     * Configures the alert message to be sent when the test fails
     * @param failureMessage The message that needs to be sent to the configured alert medium upon test failure
     * @param alertMedium Specify one or more alert mediums. It can be Slack channels, email addresses and so on
     * For Slack channels, the channel name should be prefixed with "@slack-"
     * For email address, the email should be prefixed with "@"
     * Example: Slack channel -> @slack-test_slack_channel
     * Email -> @firstName.lastName@domain.com
     */
    fun alertMessage(
        failureMessage: String,
        vararg alertMedium: String,
    ) {
        parameters.message += "${alertMedium.joinToString(" ")} {{#is_alert}} $failureMessage {{/is_alert}} "
    }

    /**
     * Configures the recovery message when the test recovers
     * @param recoveryMessage The message that needs to be sent upon recovery from a failure
     */
    fun recoveryMessage(recoveryMessage: String) {
        parameters.message += " {{#is_recovery}} $recoveryMessage {{/is_recovery}} "
    }

    /**
     * Adds the tags for the synthetic test
     * @param tags List of tags
     */
    fun tags(vararg tags: String) {
        parameters.tags.addAll(tags)
    }

    /**
     * Adds the tags for the synthetic test
     * @param tags List of tags
     */
    @Deprecated(
        message = "The function is deprecated. Please use `tags` instead.",
        replaceWith = ReplaceWith("tags(*tags.toTypedArray())"),
    )
    fun tags(tags: List<String>) {
        parameters.tags.addAll(tags)
    }

    /**
     * Sets the locations for the synthetic test
     * @param locations List of locations
     */
    fun publicLocations(vararg locations: Location) {
        parameters.locations = locations.map { it.value }
    }

    /**
     * Sets the locations for the synthetic test
     * @param locationItems List of locations
     */
    @Deprecated(
        message = "The function is deprecated. Please use `publicLocations` instead.",
        replaceWith = ReplaceWith("publicLocations(*locationItems)"),
    )
    fun publicLocation(vararg locationItems: Location) {
        parameters.locations = locationItems.map { it.value }
    }

    /**
     * Sets the test execution frequency for the synthetic test
     * @param frequency The frequency of the test
     * Allowed test frequency is between 30 seconds and 7 days
     */
    open fun testFrequency(frequency: Duration) {
        require(frequency in 30.seconds..7.days) {
            "Frequency should be between 30 seconds and 1 week."
        }
        options.tickEvery = frequency.inWholeSeconds
    }

    /**
     * Sets the advanced scheduling configuration for the synthetic test
     * @param timeframe Time range and days when the test should be scheduled to run
     * - from/to time -> pass LocalTime data type values where From value is earlier than To value
     * - days -> pass comma separated DayOfWeek data type values
     * @param timezone Timezone where the tests should be scheduled to run
     * Pass ZoneId data type value, e.g. 'ZoneId.of("Europe/Berlin")'
     * Offset value is set as the local timezone of the machine where the test creation runs, if the value is not explicitly set
     */
    fun advancedScheduling(
        timeframe: Timeframe,
        timezone: ZoneId = ZoneId.systemDefault(),
    ) {
        options.scheduling = SyntheticsTestOptionsScheduling()
        options.scheduling.timeframes =
            timeframe.days.map {
                SyntheticsTestOptionsSchedulingTimeframe().apply {
                    from = timeframe.from.truncatedTo(ChronoUnit.MINUTES).toString()
                    to = timeframe.to.truncatedTo(ChronoUnit.MINUTES).toString()
                    day = it.value
                }
            }
        options.scheduling.timezone = timezone.toString()
    }

    /**
     * Sets the retry count and interval for the synthetic test
     * @param retryCount The retry count for the test
     * Allowed retry count is between 0 and 2
     * @param retryInterval The retry interval for the test
     * Allowed retry interval is between 0 and 15 minutes
     */
    fun retry(
        retryCount: Long,
        retryInterval: Duration,
    ) {
        require(retryCount in 0..2) {
            "Retry count should be between 0 and 2."
        }
        require(retryInterval in 0.milliseconds..15.minutes) {
            "Retry interval should be between 0 and 15 minutes."
        }
        options.retry.count = retryCount
        options.retry.interval = retryInterval.inWholeMilliseconds.toDouble()
    }

    /**
     * Sets the minimum failure duration for the synthetic test
     * @param minFailureDuration The minimum failure duration of the test
     * Allowed minimum failure duration is between 0 and 120 minutes
     */
    fun minFailureDuration(minFailureDuration: Duration) {
        require(minFailureDuration in 0.minutes..120.minutes) {
            "Minimum failure duration should be between 0 and 120 minutes."
        }
        options.minFailureDuration = minFailureDuration.inWholeSeconds
    }

    /**
     * Sets the minimum location failed for the synthetic test
     * @param minLocationFailed The minimum number of locations in which the test has failed to trigger the alert
     * Allowed minimum location failed is between 1 and the number of locations where the test is configured to run
     */
    fun minLocationFailed(minLocationFailed: Long) {
        require(minLocationFailed in 1..parameters.locations.count()) {
            "Minimum location failed should be between 1 and the number of locations where the test is " +
                "configured to run: ${parameters.locations.count()}."
        }
        options.minLocationFailed = minLocationFailed
    }

    /**
     * Sets the renotify interval for the synthetic test
     * @param renotifyInterval The renotify interval for the test derived from RenotifyInterval enum class
     */
    fun renotifyInterval(renotifyInterval: RenotifyInterval) {
        options.monitorOptions!!.renotifyInterval = renotifyInterval.valueInMinutes
    }

    /**
     * Sets the monitor priority for the synthetic test
     * @param monitorPriority The monitor priority of the test
     * Allowed monitor priority is one of [1, 2, 3, 4, 5]
     */
    fun monitorPriority(monitorPriority: MonitorPriority) {
        options.monitorPriority = monitorPriority.priorityValue
    }

    /**
     * Creates a local variable with the supplied string as a value
     * @param name Name of the variable. The name would be converted to upper case letters
     * @param value Supply the text to be set for the variable
     */
    fun textVariable(
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
     */
    fun numericPatternVariable(
        name: String,
        characterLength: Int,
        prefix: String = "",
        suffix: String = "",
    ) {
        addLocalVariable(name, "$prefix{{ numeric($characterLength) }}$suffix")
    }

    /**
     * Creates a local variable with the alphabetic pattern
     * The value of the variable will be a generated random alphabetic string with n characters
     * @param name Name of the variable. The name would be converted to upper case letters
     * @param characterLength Length of the random alphabetic value that's generated
     * @param prefix String to be appended before the pattern
     * @param suffix String to be appended after the pattern
     */
    fun alphabeticPatternVariable(
        name: String,
        characterLength: Int,
        prefix: String = "",
        suffix: String = "",
    ) {
        addLocalVariable(name, "$prefix{{ alphabetic($characterLength) }}$suffix")
    }

    /**
     * Creates a local variable with the alphanumeric pattern
     * The value of the variable will be a generated random alphanumeric string with n characters
     * @param name Name of the variable. The name would be converted to upper case letters
     * @param characterLength Length of the random alphanumeric value that's generated
     * @param prefix String to be appended before the pattern
     * @param suffix String to be appended after the pattern
     */
    fun alphanumericPatternVariable(
        name: String,
        characterLength: Int,
        prefix: String = "",
        suffix: String = "",
    ) {
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
     */
    fun datePatternVariable(
        name: String,
        duration: Duration,
        format: String,
        prefix: String = "",
        suffix: String = "",
    ) {
        val (scaledValue, unit) =
            checkNotNull(getScaledDate(duration)) {
                "The passed duration should be less than 10_000_000 days for the date pattern variable $name."
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
     */
    fun timestampPatternVariable(
        name: String,
        duration: Duration,
        prefix: String = "",
        suffix: String = "",
    ) {
        val (scaledValue, unit) =
            checkNotNull(getScaledTimestamp(duration)) {
                "The passed duration should be less than 1_000_000_000 seconds for the timestamp pattern variable $name."
            }
        addLocalVariable(name, "$prefix{{ timestamp($scaledValue, $unit) }}$suffix")
    }

    /**
     * Creates a local variable with the UUID pattern
     * @param name Name of the variable. The name would be converted to upper case letters
     */
    fun uuidVariable(name: String) {
        addLocalVariable(name, "{{ uuid }}")
    }

    /**
     * Adds "env:<envName>" tag
     * @param envName Environment name. Examples: prod, stage, dev.
     */
    fun env(envName: String) {
        tags("env:$envName")
    }

    /**
     * Adds "team:<teamName>" tag
     * @param teamName Team name
     */
    fun team(teamName: String) {
        tags("team:$teamName")
    }

    /**
     * Sets a synthetic test status
     * @param status Synthetic test status
     */
    fun status(status: SyntheticsTestPauseStatus) {
        this.status = status
    }

    private fun getScaledDate(value: Duration): Pair<Long, String>? =
        value.getScaledValue(
            sequenceOf(DurationUnit.MILLISECONDS, DurationUnit.SECONDS, DurationUnit.MINUTES, DurationUnit.HOURS, DurationUnit.DAYS),
            10_000_000,
        )

    private fun getScaledTimestamp(value: Duration): Pair<Long, String>? =
        value.getScaledValue(
            sequenceOf(DurationUnit.MILLISECONDS, DurationUnit.SECONDS),
            1_000_000_000,
        )

    private fun Duration.getScaledValue(
        sequence: Sequence<DurationUnit>,
        limit: Long,
    ): Pair<Long, String>? =
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
            else -> throw IllegalArgumentException("The given duration unit is not supported.")
        }
    }

    protected fun getGlobalVariableId(variableName: String) =
        apiClient.listGlobalVariables()
            .variables
            .orEmpty()
            .filterNotNull()
            .find { it.name.equals(variableName) }
            ?.id

    protected abstract fun addLocalVariable(
        name: String,
        pattern: String,
    )

    abstract fun useGlobalVariable(name: String)
}
