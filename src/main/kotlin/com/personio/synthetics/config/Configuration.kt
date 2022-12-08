package com.personio.synthetics.config

data class Configuration(
    var credentials: Credentials,
    val testSettings: TestSettings
)

data class Credentials(
    val ddApiKey: String?,
    val ddAppKey: String?,
    val awsRegion: String?,
    val datadogCredentialsAwsArn: String?
)

data class TestSettings(
    val datadogApiHost: String,
    val defaults: Defaults
)

data class Defaults(
    val testFrequencySec: Long,
    val minFailureDurationSec: Long,
    val minLocationFailed: Long,
    val retryCount: Long,
    val retryIntervalMillisec: Double,
    val renotifyIntervalMinutes: Long,
    val runLocations: List<String>
)
