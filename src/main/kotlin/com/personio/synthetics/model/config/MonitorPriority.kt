package com.personio.synthetics.model.config

enum class MonitorPriority(
    val priorityValue: Int,
) {
    P1_CRITICAL(1),
    P2_HIGH(2),
    P3_MEDIUM(3),
    P4_LOW(4),
    P5_INFO(5),
}
