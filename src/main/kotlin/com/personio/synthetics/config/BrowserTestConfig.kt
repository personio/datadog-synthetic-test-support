package com.personio.synthetics.config

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsBrowserVariable
import com.datadog.api.v1.client.model.SyntheticsBrowserVariableType
import com.datadog.api.v1.client.model.SyntheticsDeviceID
import com.datadog.api.v1.client.model.SyntheticsTestRequest
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import java.net.URL
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/**
 * Sets the base url for the synthetic browser test
 * @param url the base url for the test
 * @return SyntheticsBrowserTest object with url set
 */
fun BrowserTest.baseUrl(url: URL) = apply {
    config
        ?.request(
            SyntheticsTestRequest()
                .method(HTTPMethod.GET)
                .url(url.toString())
        )
}

/**
 * Creates a local variable and add it to the synthetic browser test
 * @param name name of the variable
 * @param type type of the variable
 * @param pattern pattern of the variables (Optional parameter). Allowed patterns are
 * {{ numeric(8) }}, {{ alphabetic(15) }}, {{ alphanumeric(15) }}, {{ date(0d, MM-DD-YYYY) }}, {{ timestamp(0, s) }}
 * @return SyntheticsBrowserTest object with this created variable
 */
fun BrowserTest.addLocalVariable(name: String, type: SyntheticsBrowserVariableType, pattern: String? = null) = apply {
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name)
                .type(type)
                .pattern(pattern)
                .example("")
        )
}

/**
 * Adds the global variable to the test
 * @param name name of the existing global variable (case sensitive)
 * @return BrowserTest object with this added global variable
 */
fun BrowserTest.addGlobalVariable(name: String) = apply {
    val variableId = getGlobalVariableId(name)
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name)
                .id(variableId)
                .type(SyntheticsBrowserVariableType.GLOBAL)
        )
}

/**
 * Sets the test frequency for the synthetic browser test
 * @param frequency The frequency of the test
 * Allowed test frequency is between 5 minutes and 7 days
 * @return BrowserTest object with test frequency set
 */
fun BrowserTest.testFrequency(frequency: Duration) = apply {
    require(frequency in 5.minutes..7.days) {
        "Frequency should be between 5 minutes and 7 days."
    }
    options.tickEvery = frequency.inWholeSeconds
}

/**
 * Sets the retry count and interval for the synthetic browser test
 * @param retryCount The retry count for the test
 * Allowed retry count is between 0 and 2
 * @param retryInterval The retry interval for the test
 * Allowed retry interval is between 0 and 15 minutes
 * @return BrowserTest object with test retry count and interval set
 */
fun BrowserTest.retry(retryCount: Long, retryInterval: Duration) = apply {
    require(retryCount in 0..2) {
        "Retry count should be between 0 and 2."
    }
    require(retryInterval in 0.milliseconds..15.minutes) {
        "Retry interval should be between 0 and 15 minutes."
    }
    options.retry!!.count = retryCount
    options.retry!!.interval = retryInterval.inWholeMilliseconds.toDouble()
}

/**
 * Sets the minimum failure duration for the synthetic browser test
 * @param minFailureDuration The minimum failure duration of the test
 * Allowed minimum failure duration is between 0 and 120 minutes
 * @return BrowserTest object with minimum failure duration set
 */
fun BrowserTest.minFailureDuration(minFailureDuration: Duration) = apply {
    require(minFailureDuration in 0.minutes..120.minutes) {
        "Minimum failure duration should be between 0 and 120 minutes."
    }
    options.minFailureDuration = minFailureDuration.inWholeSeconds
}

/**
 * Sets the minimum location failed for the synthetic browser test
 * @param minLocationFailed The minimum number of locations in which browser test has failed in order to trigger the alert
 * Allowed minimum location failed is between 1 and the number of locations where the test is configured to run
 * @return BrowserTest object with minimum location failed set
 */
fun BrowserTest.minLocationFailed(minLocationFailed: Long) = apply {
    require(minLocationFailed in 1..locations.count()) {
        "Minimum location failed should be between 1 and the number of locations where the test is configured to run: ${locations.count()}."
    }
    options.minLocationFailed = minLocationFailed
}

/**
 * Sets the monitor name for the synthetic browser test
 * @param monitorName The monitor name of the test
 * @return BrowserTest object with monitor name set
 */
fun BrowserTest.monitorName(monitorName: String) = apply {
    options.monitorName = monitorName
}

/**
 * Sets the renotify interval for the synthetic browser test
 * @param renotifyInterval The renotify interval for the test derived from RenotifyInterval enum class
 * @return BrowserTest object with renotify interval set
 */
fun BrowserTest.renotifyInterval(renotifyInterval: RenotifyInterval) = apply {
    options.monitorOptions!!.renotifyInterval = renotifyInterval.valueInMinutes
}

/**
 * Sets the monitor priority for the synthetic browser test
 * @param monitorPriority The monitor priority of the test
 * Allowed monitor priority is one of [1, 2, 3, 4, 5]
 * @return BrowserTest object with monitor priority set
 */
fun BrowserTest.monitorPriority(monitorPriorities: MonitorPriority) = apply {
    options.monitorPriority = monitorPriorities.priorityValue
}

/**
 * Sets the locations for the synthetic browser test
 * @param locationItems Pass comma separated locations to the test config derived from Location enum class
 * @return BrowserTest object with locations set
 */
fun BrowserTest.publicLocation(vararg locationItems: Location) = apply {
    locations = locationItems.map { it.value }
}

/**
 * Sets the browsers and devices for the synthetic browser test
 * @param deviceIds Pass comma separated browsers and devices to the test config derived from SyntheticsDeviceID class
 * @return BrowserTest object with browsers and devices set
 */
fun BrowserTest.browserAndDevice(vararg deviceIds: SyntheticsDeviceID) = apply {
    options.deviceIds = deviceIds.map { it }
}
