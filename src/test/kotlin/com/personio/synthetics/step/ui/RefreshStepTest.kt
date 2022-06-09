package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.Params
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class RefreshStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `refreshStep adds the new step item to the browser test object`() {
        browserTest.refreshStep("Step")

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `refreshStep adds step with type refresh and params of type ActionsParams`() {
        browserTest.refreshStep("Step")
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.REFRESH, step?.type)
        assertInstanceOf(Params::class.java, step?.params)
    }
}
