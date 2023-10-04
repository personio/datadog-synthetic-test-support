package com.personio.synthetics.e2e

import com.personio.synthetics.dsl.syntheticBrowserTest
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import com.personio.synthetics.model.config.Timeframe
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class E2EBrowserTest {
    @Test
    fun `create synthetic browser test`() {
        syntheticBrowserTest("[Browser] Synthetic-Test-As-Code") {
            alertMessage("Test Failed", "@slack-test_slack_channel")
            recoveryMessage("Test recovered")
            env("qa")
            tags("synthetics-api")
            baseUrl(URL("https://synthetic-test.personio.de"))
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
            monitorName("Monitor for [Browser] Synthetic-Test-As-Code")
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
        }
    }
}
