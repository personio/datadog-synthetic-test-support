package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.ActionsParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class ActionsStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

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

    @Test
    fun `navigateStep adds the new step item to the browser test object`() {
        browserTest.navigateStep()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `navigateStep adds step with type go to url and params of type ActionsParams`() {
        browserTest.navigateStep()
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.GO_TO_URL, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `navigationUrl adds url value to the step`() {
        browserTest
            .navigateStep()
            .navigationUrl("https://synthetic-test.personio.de")
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("https://synthetic-test.personio.de", params.value)
    }

    @Test
    fun `refreshStep adds the new step item to the browser test object`() {
        browserTest.refreshStep()

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `refreshStep adds step with type refresh and params of type ActionsParams`() {
        browserTest.refreshStep()
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.REFRESH, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }
}
