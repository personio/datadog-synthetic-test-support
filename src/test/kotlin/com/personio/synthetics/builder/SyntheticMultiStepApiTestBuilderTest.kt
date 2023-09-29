package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsAPIStep
import com.datadog.api.client.v1.model.SyntheticsConfigVariable
import com.datadog.api.client.v1.model.SyntheticsConfigVariableType
import com.personio.synthetics.builder.api.StepsBuilder
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.getConfigFromFile
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.Timeframe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SyntheticMultiStepApiTestBuilderTest {
    private lateinit var sut: SyntheticMultiStepApiTestBuilder

    @BeforeEach
    fun prepareSut() {
        val apiClientMock = Mockito.mock(SyntheticsApiClient::class.java)
        sut = SyntheticMultiStepApiTestBuilder(
            "any_name",
            getConfigFromFile("config-unit-test.yaml").defaults,
            apiClientMock
        )
    }

    @Test
    fun `monitorName sets monitor name`() {
        sut.monitorName("any_name")
        val result = sut.build()

        assertEquals(
            "any_name",
            result.options.monitorName
        )
    }

    @Test
    fun `alertMessage appends formatted alert message`() {
        sut.alertMessage("any_failure_message", "any_alert_medium")
        val result = sut.build()

        assertEquals(
            "any_alert_medium {{#is_alert}} any_failure_message {{/is_alert}} ",
            result.message
        )
    }

    @Test
    fun `recoveryMessage appends formatted recovery message`() {
        sut.recoveryMessage("any_recovery_message")
        val result = sut.build()

        assertEquals(
            " {{#is_recovery}} any_recovery_message {{/is_recovery}} ",
            result.message
        )
    }

    @Test
    fun `tags sets tags`() {
        sut.tags("any_tag1", "any_tag2")
        val result = sut.build()

        assertEquals(
            listOf("any_tag1", "any_tag2"),
            result.tags
        )
    }

    @Test
    fun `publicLocations sets locations`() {
        sut.publicLocations(Location.FRANKFURT_AWS, Location.LONDON_AWS)
        val result = sut.build()

        assertEquals(
            listOf(Location.FRANKFURT_AWS.value, Location.LONDON_AWS.value),
            result.locations
        )
    }

    @Test
    fun `testFrequency sets options tickEvery`() {
        sut.testFrequency(5.minutes)
        val result = sut.build()

        assertEquals(
            5.minutes.inWholeSeconds,
            result.options.tickEvery
        )
    }

    @Test
    fun `testFrequency throws IllegalArgumentException when frequency is less than 30 seconds`() {
        assertThrows<IllegalArgumentException> {
            sut.testFrequency(29.seconds)
        }
    }

    @Test
    fun `testFrequency throws IllegalArgumentException when frequency is more than 7 days`() {
        assertThrows<IllegalArgumentException> {
            sut.testFrequency((604800 + 1).seconds) // 7 days + 1 second
        }
    }

    @Test
    fun `advancedScheduling sets the advanced scheduling configuration`() {
        sut.advancedScheduling(
            Timeframe(
                from = LocalTime.of(0, 1),
                to = LocalTime.of(23, 59),
                DayOfWeek.MONDAY,
                DayOfWeek.SUNDAY
            ),
            timezone = ZoneId.of("Europe/Dublin")
        )
        val result = sut.build()

        assertEquals(1, result.options.scheduling.timeframes[0].day)
        assertEquals("00:01", result.options.scheduling.timeframes[0].from)
        assertEquals("23:59", result.options.scheduling.timeframes[0].to)
        assertEquals(7, result.options.scheduling.timeframes[1].day)
        assertEquals("00:01", result.options.scheduling.timeframes[1].from)
        assertEquals("23:59", result.options.scheduling.timeframes[1].to)
        assertEquals("Europe/Dublin", result.options.scheduling.timezone)
    }

    @Test
    fun `advancedScheduling function sets the advanced scheduling with default timezone in the test config`() {
        sut.advancedScheduling(
            Timeframe(
                from = LocalTime.of(0, 1),
                to = LocalTime.of(23, 59),
                DayOfWeek.MONDAY
            )
        )
        val result = sut.build()

        assertNotNull(result.options.scheduling.timezone)
        assertTrue(result.options.scheduling.timezone.isNotEmpty())
    }

    @Test
    fun `uuidVariable adds variable of UUID pattern`() {
        sut.uuidVariable("any_name")
        val result = sut.build()

        assertTrue(
            result.config.configVariables.contains(
                SyntheticsConfigVariable()
                    .name("ANY_NAME")
                    .type(SyntheticsConfigVariableType.TEXT)
                    .example("")
                    .pattern("{{ uuid }}")
            )
        )
    }

    @Test
    fun `timestampPatternVariable adds variable of timestamp pattern`() {
        sut.timestampPatternVariable("any_name", 5.minutes, "prefix-", "-suffix")
        val result = sut.build()

        assertTrue(
            result.config.configVariables.contains(
                SyntheticsConfigVariable()
                    .name("ANY_NAME")
                    .pattern("prefix-{{ timestamp(300000, ms) }}-suffix")
                    .type(SyntheticsConfigVariableType.TEXT)
                    .example("")
            )
        )
    }

    @Test
    fun `datePatternVariable adds variable of timestamp pattern`() {
        sut.datePatternVariable("any_name", 5.days, "YYYY-MM-DD", "prefix-", "-suffix")
        val result = sut.build()

        assertTrue(
            result.config.configVariables.contains(
                SyntheticsConfigVariable()
                    .name("ANY_NAME")
                    .pattern("prefix-{{ date(432000s, YYYY-MM-DD) }}-suffix")
                    .type(SyntheticsConfigVariableType.TEXT)
                    .example("")
            )
        )
    }

    @Test
    fun `alphanumericPatternVariable adds variable of timestamp pattern`() {
        sut.alphanumericPatternVariable("any_name", 10, "prefix-", "-suffix")
        val result = sut.build()

        assertTrue(
            result.config.configVariables.contains(
                SyntheticsConfigVariable()
                    .name("ANY_NAME")
                    .pattern("prefix-{{ alphanumeric(10) }}-suffix")
                    .type(SyntheticsConfigVariableType.TEXT)
                    .example("")
            )
        )
    }

    @Test
    fun `alphabeticPatternVariable adds variable of timestamp pattern`() {
        sut.alphabeticPatternVariable("any_name", 10, "prefix-", "-suffix")
        val result = sut.build()

        assertTrue(
            result.config.configVariables.contains(
                SyntheticsConfigVariable()
                    .name("ANY_NAME")
                    .pattern("prefix-{{ alphabetic(10) }}-suffix")
                    .type(SyntheticsConfigVariableType.TEXT)
                    .example("")
            )
        )
    }

    @Test
    fun `numericPatternVariable adds variable of timestamp pattern`() {
        sut.numericPatternVariable("any_name", 10, "prefix-", "-suffix")
        val result = sut.build()

        assertTrue(
            result.config.configVariables.contains(
                SyntheticsConfigVariable()
                    .name("ANY_NAME")
                    .pattern("prefix-{{ numeric(10) }}-suffix")
                    .type(SyntheticsConfigVariableType.TEXT)
                    .example("")
            )
        )
    }

    @Test
    fun `steps sets provided list of steps`() {
        sut.steps(
            listOf(
                SyntheticsAPIStep(),
                SyntheticsAPIStep()
            )
        )
        val result = sut.build()

        assertEquals(
            2,
            result.config.steps.count()
        )
    }

    @Test
    fun `steps evaluates provided lambda and sets steps`() {
        val stepsBuilderMock = Mockito.mock(StepsBuilder::class.java)
        whenever(stepsBuilderMock.build())
            .thenReturn(listOf(SyntheticsAPIStep(), SyntheticsAPIStep()))
        sut.steps(stepsBuilderMock) {}
        val result = sut.build()

        assertEquals(
            2,
            result.config.steps.count()
        )
    }

    @Test
    fun `env adds env tag`() {
        sut.env("env1")
        sut.env("env2")

        val result = sut.build()

        assertTrue(result.tags.contains("env:env1"))
        assertTrue(result.tags.contains("env:env2"))
    }

    @Test
    fun `team adds team tag`() {
        sut.team("team-one")
        sut.team("team-two")

        val result = sut.build()

        assertTrue(result.tags.contains("team:team-one"))
        assertTrue(result.tags.contains("team:team-two"))
    }
}
