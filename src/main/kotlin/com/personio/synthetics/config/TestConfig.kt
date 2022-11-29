package com.personio.synthetics.config

object TestConfig {
    const val DATADOG_API_HOST = "datadoghq.eu"
    const val TEST_FREQUENCY: Long = 300 // in seconds
    const val MIN_FAILURE_DURATION: Long = 300 // in seconds
    const val MIN_LOCATION_FAILED: Long = 1
    const val RETRY_COUNT: Long = 1
    const val RETRY_INTERVAL: Double = 60.0 // in milliseconds
    const val RENOTIFY_INTERVAL: Long = 10 // in minutes
    const val AWS_REGION = "eu-central-1"
    const val DATADOG_CREDENTIALS_AWS_ARN = "arn:aws:secretsmanager:eu-central-1:630288522805:secret:datadog/synthetics-Uhu4uY"
    val RUN_LOCATIONS = listOf("aws:eu-central-1")
}
