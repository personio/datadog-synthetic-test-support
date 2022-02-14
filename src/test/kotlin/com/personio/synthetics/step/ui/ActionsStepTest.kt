package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.api.SyntheticsApi
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.BrowserTest
import com.personio.synthetics.model.actions.ActionsParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class ActionsStepTest {
    private val syntheticsApi = Mockito.mock(SyntheticsApi::class.java)
    private val browserTest = BrowserTest(syntheticsApi)

    @Test
    fun `inputTextStep adds the new step item to the browser test object`() {
        browserTest.inputTextStep()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `inputTextStep adds step with type text and params of type ActionsParams`() {
        browserTest.inputTextStep()
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.TYPE_TEXT, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `text adds text value to the step`() {
        browserTest
            .inputTextStep()
            .text("SampleText")
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("SampleText", params.value)
    }

    @Test
    fun `clickStep adds the new step item to the browser test object`() {
        browserTest.clickStep()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `clickStep adds step with type click and params of type ActionsParams`() {
        browserTest.clickStep()
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.CLICK, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }
}
