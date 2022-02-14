package com.personio.synthetics

import com.datadog.api.v1.client.api.SyntheticsApi
import com.datadog.api.v1.client.model.SyntheticsGlobalVariable
import com.datadog.api.v1.client.model.SyntheticsListGlobalVariablesResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class BrowserTestTest {
    private val syntheticsApi = Mockito.mock(SyntheticsApi::class.java)
    private val browserTest = BrowserTest(syntheticsApi)

    @Test
    fun `createBrowserTest with empty test name throws error`() {
        val exception = assertThrows(Exception::class.java) {
            browserTest.createBrowserTest("")
        }
        assertEquals("Please pass test name", exception.message)
    }

    @Test
    fun `getGlobalVariableId returns the id of the global variable`() {
        val expectedId = "id"
        val variableName = "VARIABLE"
        val globalVariable = getGlobalVariableObject(expectedId, variableName)
        val response = SyntheticsListGlobalVariablesResponse().addVariablesItem(globalVariable)
        Mockito.`when`(syntheticsApi.listGlobalVariables()).thenReturn(response)

        val id = browserTest.getGlobalVariableId(variableName)

        assertEquals(expectedId, id)
    }

    @Test
    fun `getGlobalVariableId returns null if the global variable name is not found`() {
        val variableName = "VARIABLE1"
        val globalVariable = getGlobalVariableObject("id", variableName)
        val response = SyntheticsListGlobalVariablesResponse().addVariablesItem(globalVariable)
        Mockito.`when`(syntheticsApi.listGlobalVariables()).thenReturn(response)

        val id = browserTest.getGlobalVariableId("NONEXISTINGVARIABLE")

        assertEquals(null, id)
    }

    private fun getGlobalVariableObject(id: String, name: String): SyntheticsGlobalVariable {
        val globalVariable = "{ \"id\": \"${id}\", \"description\": \"desc\", \"name\": \"${name}\", \"tags\": [], \"value\": {\"value\": \"abc\"}}"
        return jacksonObjectMapper().readValue(globalVariable, SyntheticsGlobalVariable::class.java)
    }
}
