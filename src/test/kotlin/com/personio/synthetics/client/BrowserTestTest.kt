package com.personio.synthetics.client

import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig
import com.datadog.api.v1.client.model.SyntheticsDeviceID
import com.datadog.api.v1.client.model.SyntheticsGlobalVariable
import com.datadog.api.v1.client.model.SyntheticsListGlobalVariablesResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.personio.synthetics.config.TestConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.IllegalStateException

internal class BrowserTestTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `initialising a BrowserTest object sets the default test locations`() {
        assertEquals(TestConfig.RUN_LOCATIONS, browserTest.locations)
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
        assertEquals(TestConfig.TEST_FREQUENCY, browserTest.options.tickEvery)
    }

    @Test
    fun `initialising a BrowserTest object sets the default min failure duration as in TestConfig`() {
        assertEquals(TestConfig.MIN_FAILURE_DURATION, browserTest.options.minFailureDuration)
    }

    @Test
    fun `initialising a BrowserTest object sets the default min location failed as in TestConfig`() {
        assertEquals(TestConfig.MIN_LOCATION_FAILED, browserTest.options.minLocationFailed)
    }

    @Test
    fun `initialising a BrowserTest object sets a default retry count as in TestConfig`() {
        assertEquals(TestConfig.RETRY_COUNT, browserTest.options.retry?.count)
    }

    @Test
    fun `initialising a BrowserTest object sets a default retry interval as in TestConfig`() {
        assertEquals(TestConfig.RETRY_INTERVAL, browserTest.options.retry?.interval)
    }

    @Test
    fun `initialising a BrowserTest object sets a default monitor priority as in TestConfig`() {
        assertEquals(TestConfig.MONITOR_PRIORITY, browserTest.options.monitorPriority)
    }

    @Test
    fun `initialising a BrowserTest object sets a default renotify interval as in TestConfig`() {
        assertEquals(TestConfig.RENOTIFY_INTERVAL, browserTest.options.monitorOptions?.renotifyInterval)
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
