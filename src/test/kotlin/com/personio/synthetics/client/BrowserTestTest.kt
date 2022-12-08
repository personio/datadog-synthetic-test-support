package com.personio.synthetics.client

import com.datadog.api.client.v1.model.SyntheticsBrowserTestConfig
import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsGlobalVariable
import com.datadog.api.client.v1.model.SyntheticsListGlobalVariablesResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.personio.synthetics.config.Defaults
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.IllegalStateException

internal class BrowserTestTest {
    private val defaults = Defaults(300, 300, 1, 1, 60.0, 10, listOf("awsregion"))
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi, defaults)

    @Test
    fun `initialising a BrowserTest object sets the default test locations`() {
        assertEquals(defaults.runLocations, browserTest.locations)
    }

    @Test
    fun `initialising a BrowserTest object sets the default config`() {
        assertEquals(SyntheticsBrowserTestConfig(), browserTest.config)
    }

    @Test
    fun `initialising a BrowserTest object sets the device id to chrome laptop large`() {
        assertEquals(listOf(SyntheticsDeviceID.CHROME_LAPTOP_LARGE), browserTest.options.deviceIds)
    }

    @Test
    fun `initialising a BrowserTest object sets the default test frequency as in TestConfig`() {
        assertEquals(defaults.testFrequencySec, browserTest.options.tickEvery)
    }

    @Test
    fun `initialising a BrowserTest object sets the default min failure duration as in TestConfig`() {
        assertEquals(defaults.minFailureDurationSec, browserTest.options.minFailureDuration)
    }

    @Test
    fun `initialising a BrowserTest object sets the default min location failed as in TestConfig`() {
        assertEquals(defaults.minLocationFailed, browserTest.options.minLocationFailed)
    }

    @Test
    fun `initialising a BrowserTest object sets a default retry count as in TestConfig`() {
        assertEquals(defaults.retryCount, browserTest.options.retry?.count)
    }

    @Test
    fun `initialising a BrowserTest object sets a default retry interval as in TestConfig`() {
        assertEquals(defaults.retryIntervalMillisec, browserTest.options.retry?.interval)
    }

    @Test
    fun `initialising a BrowserTest object sets a default renotify interval as in TestConfig`() {
        assertEquals(defaults.renotifyIntervalMinutes, browserTest.options.monitorOptions?.renotifyInterval)
    }

    @Test
    fun `getGlobalVariableId returns the id of the global variable`() {
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

    private fun getGlobalVariableObject(id: String, name: String): SyntheticsGlobalVariable {
        val globalVariable = "{ \"id\": \"${id}\", \"description\": \"desc\", \"name\": \"${name}\", \"tags\": [], \"value\": {\"value\": \"abc\"}}"
        return jacksonObjectMapper().readValue(globalVariable, SyntheticsGlobalVariable::class.java)
    }
}
