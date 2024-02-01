package com.personio.synthetics.e2e

import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.personio.synthetics.dsl.syntheticBrowserTest
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import com.personio.synthetics.model.config.Timeframe
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Since the builder is still in progress, don't use it for the browser test creation
 */
class E2EBrowserTest {
    /**
     * This test creates a Synthetic Browser test in Datadog.
     */
    @Test
    fun `create synthetic browser test`() {
        syntheticBrowserTest("[Example Browser Test] Synthetic-Test-As-Code-Personio") {
            status(SyntheticsTestPauseStatus.PAUSED)
            alertMessage("Test Failed", "@slack-test_slack_channel")
            recoveryMessage("Test recovered")
            tags("test")
            baseUrl(URL("https://synthetic-test.personio.de"))
            browsersAndDevices(SyntheticsDeviceID.CHROME_MOBILE_SMALL, SyntheticsDeviceID.FIREFOX_LAPTOP_LARGE)
            publicLocations(Location.IRELAND_AWS, Location.N_CALIFORNIA_AWS, Location.MUMBAI_AWS)
            testFrequency(6.minutes)
            advancedScheduling(
                Timeframe(
                    from = LocalTime.of(0, 1),
                    to = LocalTime.of(23, 59),
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.FRIDAY
                ),
                timezone = ZoneId.of("Europe/Dublin")
            )
            retry(2, 600.milliseconds)
            minFailureDuration(120.minutes)
            minLocationFailed(2)
            monitorName("Test Monitor Name")
            renotifyInterval(RenotifyInterval.HOURS_2)
            monitorPriority(MonitorPriority.P3_MEDIUM)
            useGlobalVariable("TEST_PASSWORD")
            textVariable("TEXT_VARIABLE", "test")
            numericPatternVariable(
                name = "NUMERIC_PATTERN",
                characterLength = 4,
                prefix = "test"
            )
            alphabeticPatternVariable("ALPHABETIC_PATTERN", 5)
            alphanumericPatternVariable("ALPHANUMERIC_PATTERN", 6)
            datePatternVariable(
                name = "DATE_PATTERN",
                duration = (-1).days,
                format = "MM-DD-YYYY"
            )
            timestampPatternVariable(
                name = "TIMESTAMP_PATTERN",
                duration = 10.seconds
            )
            steps {
                typeText("Type text", "new_text", TargetElement("#my-element"))
                click("Click", TargetElement("#my-element"))
                hover("Hover", TargetElement("#my-element"))
                wait("Wait", 3.seconds)
                refresh("Refresh")
                goto("Go to URL", "https://synthetic-test.personio.de/test")
                scroll("Scroll using coordinates") {
                    horizontal(10)
                    vertical(20)
                }
                pressKey("Press key", Key.BACKSPACE, Modifier.CONTROL, Modifier.SHIFT)
                assertCurrentUrlIsEmpty("Check: current URL is empty")
                assertCurrentUrlIsNotEmpty("Check: current URL is not empty")
                assertCurrentUrlContains("Check: current URL contains example.com", "example.com")
                assertCurrentUrlEquals("Check: current URL is equal to example.com", "https://example.com")
            }
        }
    }
}
