package com.personio.synthetics.config

object TestConfig {
    const val DATADOG_API_HOST = "datadoghq.eu"
    const val TEST_FREQUENCY: Long = 300 // in seconds
    const val MIN_FAILURE_DURATION: Long = 300 // in seconds
    const val MIN_LOCATION_FAILED: Long = 1
    const val RETRY_COUNT: Long = 1
    const val RETRY_INTERVAL: Double = 60.0 // in milliseconds
    const val RENOTIFY_INTERVAL: Long = 10 // in minutes
    val RUN_LOCATIONS = listOf("aws:eu-central-1")
}
