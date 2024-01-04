package com.personio.synthetics.model.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.time.DayOfWeek
import java.time.LocalTime

class TimeframeTest {
    @Test
    fun `invoke throws IllegalArgumentException when from time is later than to time`() {
        assertThrows<IllegalArgumentException> {
            Timeframe(
                from = LocalTime.of(19, 0),
                to = LocalTime.of(17, 0),
                DayOfWeek.MONDAY,
                DayOfWeek.SUNDAY,
            )
        }
    }

    @Test
    fun `invoke throws IllegalArgumentException when from time equals to to time`() {
        assertThrows<IllegalArgumentException> {
            Timeframe(
                from = LocalTime.of(23, 1),
                to = LocalTime.of(23, 1),
                DayOfWeek.MONDAY,
                DayOfWeek.SUNDAY,
            )
        }
    }
}
