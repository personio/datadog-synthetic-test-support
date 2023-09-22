package com.personio.synthetics.model.config

import java.time.DayOfWeek
import java.time.LocalTime

class Timeframe private constructor(val from: LocalTime, val to: LocalTime, vararg val days: DayOfWeek) {
    companion object {
        operator fun invoke(from: LocalTime, to: LocalTime, vararg days: DayOfWeek): Timeframe {
            require(from < to) {
                "From time must be earlier than To time."
            }

            return Timeframe(from, to, *days)
        }
    }
}
