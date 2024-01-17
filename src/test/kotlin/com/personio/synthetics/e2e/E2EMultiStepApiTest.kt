package com.personio.synthetics.e2e

import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.datadog.api.client.v1.model.SyntheticsTestRequestBodyType
import com.personio.synthetics.builder.RequestMethod
import com.personio.synthetics.dsl.syntheticMultiStepApiTest
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.Timeframe
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class E2EMultiStepApiTest {
    /**
     * This test creates a Synthetic Multi-Step API test in Datadog.
     */
    @Test
    fun `create mutli-step api synthetic test`() {
        syntheticMultiStepApiTest("[Multi-Step] Synthetic-Test-As-Code") {
            alertMessage("Test failed", "@slack-test_slack_channel")
            recoveryMessage("Test recovered")
            env("qa")
            status(SyntheticsTestPauseStatus.PAUSED)
            publicLocations(Location.IRELAND_AWS, Location.N_CALIFORNIA_AWS, Location.MUMBAI_AWS)
            testFrequency(1.minutes)
            advancedScheduling(
                Timeframe(
                    from = LocalTime.of(0, 1),
                    to = LocalTime.of(23, 59),
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.FRIDAY,
                ),
                timezone = ZoneId.of("Africa/Bissau"),
            )
            monitorName("Test Monitor Name")
            alphabeticPatternVariable("ALPHABETIC_PATTERN", 5)
            alphanumericPatternVariable("ALPHANUMERIC_PATTERN", 6)
            useGlobalVariable("TEST_PASSWORD")
            textVariable("TEXT_VARIABLE", "test")
            numericPatternVariable(
                name = "NUMERIC_PATTERN",
                characterLength = 4,
                prefix = "test",
            )
            datePatternVariable(
                name = "DATE_PATTERN",
                duration = (-1).days,
                format = "MM-DD-YYYY",
            )
            timestampPatternVariable(
                name = "TIMESTAMP_PATTERN",
                duration = 10.seconds,
            )
            steps {
                step("Do http request") {
                    retry(4, 3000.milliseconds)
                    request {
                        url("https://synthetic-test.personio.de/")
                        method(RequestMethod.POST)
                        bodyType(SyntheticsTestRequestBodyType.APPLICATION_JSON)
                        body(
                            """
                            {
                                "key": "value",
                            }
                            """.trimIndent(),
                        )
                        headers(
                            mapOf(
                                "Content-Type" to "application/json",
                            ),
                        )
                        ignoreServerCertificateError(true)
                        assertions {
                            statusCode(200)
                            bodyContainsJsonPath("\$.success", "true")
                            bodyContains("some_data")
                            headerContains("set-cookie", "cookie_name")
                        }
                        extract("COOKIE_VARIABLE") {
                            headerRegex("set-cookie", "(?<=cookie_name\\=)[^;]+(?=;)")
                        }
                    }
                }
            }
        }
    }
}
