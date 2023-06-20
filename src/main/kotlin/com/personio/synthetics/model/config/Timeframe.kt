package com.personio.synthetics.model.config

import java.time.DayOfWeek
import java.time.LocalTime

class Timeframe private constructor(
    val from: LocalTime,
    val to: LocalTime,
    val days: Set<DayOfWeek>
) {
    companion object {
        operator fun invoke(
            from: LocalTime,
            to: LocalTime,
            day: DayOfWeek,
            vararg moreDays: DayOfWeek
        ) = Timeframe(from, to, setOf(day) + moreDays.toSet())
    }
}
