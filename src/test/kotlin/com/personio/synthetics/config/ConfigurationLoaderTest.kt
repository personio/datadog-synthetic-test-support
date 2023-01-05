package com.personio.synthetics.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConfigurationLoaderTest {

    @Test
    fun `loadConfiguration loads the dd api key from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals("apiKey", Config.testConfig.credentials.ddApiKey)
    }

    @Test
    fun `loadConfiguration loads the dd app key from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals("appKey", Config.testConfig.credentials.ddAppKey)
    }

    @Test
    fun `loadConfiguration loads the datadog credentials aws arn from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals("awsArn", Config.testConfig.credentials.datadogCredentialsAwsArn)
    }

    @Test
    fun `loadConfiguration loads the aws region from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals("eu-central-1", Config.testConfig.credentials.awsRegion)
    }

    @Test
    fun `loadConfiguration loads the datadog api host from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals("datadoghq.eu", Config.testConfig.datadogApiHost)
    }

    @Test
    fun `loadConfiguration loads the test frequency from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals(1, Config.testConfig.defaults.testFrequency)
    }

    @Test
    fun `loadConfiguration loads the min failure duration from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals(2, Config.testConfig.defaults.minFailureDuration)
    }

    @Test
    fun `loadConfiguration loads the min location failed from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals(3, Config.testConfig.defaults.minLocationFailed)
    }

    @Test
    fun `loadConfiguration loads the retry count from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals(4, Config.testConfig.defaults.retryCount)
    }

    @Test
    fun `loadConfiguration loads the retry interval from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals(5.0, Config.testConfig.defaults.retryInterval)
    }

    @Test
    fun `loadConfiguration loads the run locations from the config file into the config object`() {
        loadConfiguration("config-unit-test.yaml")
        assertEquals(listOf("aws:eu-central-1"), Config.testConfig.defaults.runLocations)
    }
}
