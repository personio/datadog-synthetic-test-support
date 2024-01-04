package com.personio.synthetics.config

data class Configuration(
    val credentials: Credentials,
    val datadogApiHost: String,
    val defaults: Defaults,
)

data class Credentials(
    val ddApiKey: String?,
    val ddAppKey: String?,
    val awsRegion: String?,
    val datadogCredentialsAwsArn: String?,
)

data class Defaults(
    val testFrequency: Long,
    val minFailureDuration: Long,
    val minLocationFailed: Long,
    val retryCount: Long,
    val retryInterval: Double,
    val runLocations: List<String>,
)
