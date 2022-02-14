package com.personio.synthetics.config

import com.datadog.api.v1.client.api.SyntheticsApi
import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig
import com.datadog.api.v1.client.model.SyntheticsBrowserVariable
import com.datadog.api.v1.client.model.SyntheticsBrowserVariableType
import com.personio.synthetics.BrowserTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

internal class BrowserTestConfigTest {
    private val syntheticsApi = Mockito.mock(SyntheticsApi::class.java)
    private val browserTest = BrowserTest(syntheticsApi).config(SyntheticsBrowserTestConfig())

    @Test
    fun `setUrl sets the base url to the test config`() {
        val expectedUrl = "https://synthetic-test.personio.de"
        browserTest.setUrl(expectedUrl)

        assertEquals(expectedUrl, browserTest.config!!.request.url)
    }

    @Test
    fun `addLocalVariable adds a local variable to the test config`() {
        val variableName = "Variable1"
        val variableType = SyntheticsBrowserVariableType.TEXT
        val pattern = "pattern"
        browserTest.addLocalVariable(variableName, variableType, pattern)

        val expectedResult = SyntheticsBrowserVariable()
            .name(variableName)
            .type(variableType)
            .pattern(pattern)
            .example("")
        assertEquals(expectedResult, browserTest.config!!.variables!![0])
    }

    @Test
    fun `addGlobalVariable adds a global variable to the test config`() {
        val variableName = "VARIABLE1"
        val variableType = SyntheticsBrowserVariableType.GLOBAL
        val variableId = "variable-id"
        val browserTest = Mockito.mock(BrowserTest::class.java)
        `when`(browserTest.getGlobalVariableId(variableName)).thenReturn(variableId)
        `when`(browserTest.config).thenReturn(SyntheticsBrowserTestConfig())

        browserTest.addGlobalVariable(variableName)

        val expectedResult = SyntheticsBrowserVariable()
            .name(variableName)
            .type(variableType)
            .id(variableId)
        assertEquals(expectedResult, browserTest.config!!.variables!![0])
    }
}
