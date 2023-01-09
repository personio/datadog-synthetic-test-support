package com.personio.synthetics.client

import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsGlobalVariable
import com.datadog.api.client.v1.model.SyntheticsListGlobalVariablesResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.personio.synthetics.config.Config
import com.personio.synthetics.config.Credentials
import com.personio.synthetics.config.Defaults
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class BrowserTestTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()

    @Test
    fun `initialising a BrowserTest object sets the default test locations`() {
        val expectedTestLocation = listOf("eu-central")
        val browserTest = BrowserTest(
            "Test",
            syntheticsApi,
            defaults(runLocations = expectedTestLocation)
        )
        assertEquals(expectedTestLocation, browserTest.locations)
    }

    @Test
    fun `initialising a BrowserTest object sets the default config`() {
        val browserTest = BrowserTest("Test", syntheticsApi, defaults())
        assertEquals(SyntheticsBrowserTestConfig(), browserTest.config)
    }

    @Test
    fun `initialising a BrowserTest object sets the device id to chrome laptop large`() {
        val browserTest = BrowserTest("Test", syntheticsApi, defaults())
        assertEquals(listOf(SyntheticsDeviceID.CHROME_LAPTOP_LARGE), browserTest.options.deviceIds)
    }

    @Test
    fun `initialising a BrowserTest object sets the default test frequency converted into seconds`() {
        val expectedTestFrequencyInMillis = 10L
        val browserTest = BrowserTest(
            "Test",
            syntheticsApi,
            defaults(testFrequency = expectedTestFrequencyInMillis)
        )
        assertEquals(expectedTestFrequencyInMillis / 1000, browserTest.options.tickEvery)
    }

    @Test
    fun `initialising a BrowserTest object sets the default min failure duration converted into seconds`() {
        val expectedMinFailureDurationInMillis = 10L
        val browserTest = BrowserTest(
            "Test",
            syntheticsApi,
            defaults(minFailureDuration = expectedMinFailureDurationInMillis)
        )
        assertEquals(expectedMinFailureDurationInMillis / 1000, browserTest.options.minFailureDuration)
    }

    @Test
    fun `initialising a BrowserTest object sets the default min location failed`() {
        val expectedMinLocationFailed = 10L
        val browserTest = BrowserTest(
            "Test",
            syntheticsApi,
            defaults(minLocationFailed = expectedMinLocationFailed)
        )
        assertEquals(expectedMinLocationFailed, browserTest.options.minLocationFailed)
    }

    @Test
    fun `initialising a BrowserTest object sets a default retry count`() {
        val expectedRetryCount = 10L
        val browserTest = BrowserTest(
            "Test",
            syntheticsApi,
            defaults(retryCount = expectedRetryCount)
        )
        assertEquals(expectedRetryCount, browserTest.options.retry?.count)
    }

    @Test
    fun `initialising a BrowserTest object sets a default retry interval`() {
        val expectedRetryInterval = 10.0
        val browserTest = BrowserTest(
            "Test",
            syntheticsApi,
            defaults(retryInterval = expectedRetryInterval)
        )
        assertEquals(expectedRetryInterval, browserTest.options.retry?.interval)
    }

    @Test
    fun `getGlobalVariableId returns the id of the global variable`() {
        val browserTest = BrowserTest("Test", syntheticsApi, defaults())
        val expectedId = "id"
        val variableName = "VARIABLE"
        val globalVariable = getGlobalVariableObject(expectedId, variableName)
        val response = SyntheticsListGlobalVariablesResponse().addVariablesItem(globalVariable)
        whenever(syntheticsApi.listGlobalVariables()).thenReturn(response)

        val id = browserTest.getGlobalVariableId(variableName)

        assertEquals(expectedId, id)
    }

    @Test
    fun `getGlobalVariableId returns null if the global variable name is not found`() {
        val browserTest = BrowserTest("Test", syntheticsApi, defaults())
        val variableName = "VARIABLE1"
        val globalVariable = getGlobalVariableObject("id", variableName)
        val response = SyntheticsListGlobalVariablesResponse().addVariablesItem(globalVariable)
        whenever(syntheticsApi.listGlobalVariables()).thenReturn(response)

        val id = browserTest.getGlobalVariableId("NONEXISTINGVARIABLE")

        assertEquals(null, id)
    }

    @Test
    fun `adding synthetic browser test with blank name throws exception`() {
        assertThrows<IllegalStateException> {
            syntheticBrowserTest("") {
            }
        }
    }

    @Test
    fun `getCredentialsProvider returns AwsSecretsManagerCredentialsProvider if datadogCredentialsAwsArn is set`() {
        Config.testConfig = mock()
        whenever(Config.testConfig.credentials).thenReturn(Credentials(ddApiKey = null, ddAppKey = null, awsRegion = "awsRegion", datadogCredentialsAwsArn = "awsArn"))
        Assertions.assertInstanceOf(AwsSecretsManagerCredentialsProvider::class.java, getCredentialsProvider())
    }

    @Test
    fun `getCredentialsProvider returns ConfigCredentialsProvider if datadogCredentialsAwsArn is null and ddApiKey and ddAppKey are not null`() {
        Config.testConfig = mock()
        whenever(Config.testConfig.credentials).thenReturn(Credentials(ddApiKey = "apiKey", ddAppKey = "appKey", awsRegion = "", datadogCredentialsAwsArn = ""))
        Assertions.assertInstanceOf(ConfigCredentialsProvider::class.java, getCredentialsProvider())
    }

    @Test
    fun `Validate exception is thrown when api key is not set`() {
        Config.testConfig = mock()
        whenever(Config.testConfig.credentials).thenReturn(Credentials(ddApiKey = "", ddAppKey = "appKey", null, datadogCredentialsAwsArn = ""))
        val exception = assertThrows<IllegalStateException> { getCredentialsProvider() }
        assertEquals("Please set the required config values for credentials in the \"configuration.yaml\" under resources.", exception.message)
    }

    @Test
    fun `Validate exception is thrown when app key is not set`() {
        Config.testConfig = mock()
        whenever(Config.testConfig.credentials).thenReturn(Credentials(ddApiKey = "apiKey", ddAppKey = "", null, datadogCredentialsAwsArn = ""))
        val exception = assertThrows<IllegalStateException> { getCredentialsProvider() }
        assertEquals("Please set the required config values for credentials in the \"configuration.yaml\" under resources.", exception.message)
    }

    private fun getGlobalVariableObject(id: String, name: String): SyntheticsGlobalVariable {
        val globalVariable = "{ \"id\": \"${id}\", \"description\": \"desc\", \"name\": \"${name}\", \"tags\": [], \"value\": {\"value\": \"abc\"}}"
        return jacksonObjectMapper().readValue(globalVariable, SyntheticsGlobalVariable::class.java)
    }

    private fun defaults(
        testFrequency: Long = 1,
        minFailureDuration: Long = 1,
        minLocationFailed: Long = 1,
        retryCount: Long = 1,
        retryInterval: Double = 1.0,
        runLocations: List<String> = listOf("location1")
    ) = Defaults(testFrequency, minFailureDuration, minLocationFailed, retryCount, retryInterval, runLocations)
}
