package com.personio.synthetics.model.config

enum class RenotifyInterval(val valueInMinutes: Long) {
    MINUTES_0(0),
    MINUTES_10(10),
    MINUTES_20(20),
    MINUTES_30(30),
    MINUTES_40(40),
    MINUTES_50(50),
    MINUTES_60(60),
    MINUTES_90(90),
    HOURS_2(120),
    HOURS_3(180),
    HOURS_4(240),
    HOURS_5(300),
    HOURS_6(360),
    HOURS_12(720),
    HOURS_24(1440)
}
