package com.personio.synthetics.config

import com.datadog.api.v1.client.model.SyntheticsBrowserTestConfig
import com.datadog.api.v1.client.model.SyntheticsBrowserVariable
import com.datadog.api.v1.client.model.SyntheticsBrowserVariableType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.net.URL

internal class BrowserTestConfigTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `setUrl sets the base url to the test config`() {
        val expectedUrl = "https://synthetic-test.personio.de"
        browserTest.setUrl(URL(expectedUrl))

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
        whenever(browserTest.getGlobalVariableId(variableName)).thenReturn(variableId)
        whenever(browserTest.config).thenReturn(SyntheticsBrowserTestConfig())

        browserTest.addGlobalVariable(variableName)

        val expectedResult = SyntheticsBrowserVariable()
            .name(variableName)
            .type(variableType)
            .id(variableId)
        assertEquals(expectedResult, browserTest.config!!.variables!![0])
    }
}
