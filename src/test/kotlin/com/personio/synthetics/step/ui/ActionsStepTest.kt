package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.ActionsParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import java.lang.IllegalStateException

internal class ActionsStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `inputTextStep without text value and target element throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.inputTextStep("Step") {}
        }
    }

    @Test
    fun `inputTextStep without target element throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.inputTextStep("Step") {
                text("SampleText")
            }
        }
    }

    @Test
    fun `inputTextStep without text value throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.inputTextStep("Step") {
                targetElement { locator = "#locatorId" }
            }
        }
    }

    @Test
    fun `inputTextStep adds step with type text and params of type ActionsParams`() {
        browserTest.inputTextStep("Step") {
            text("SampleText")
            targetElement { locator = "#locatorId" }
        }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.TYPE_TEXT, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `inputTextStep with text value and target element adds new step to the browser test object`() {
        browserTest
            .inputTextStep("Step") {
                text("SampleText")
                targetElement { locator = "#locatorId" }
            }
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("SampleText", params.value)
        assertEquals("#locatorId", params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `clickStep without target element throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.clickStep("Step") {}
        }
    }

    @Test
    fun `clickStep adds step with type click and params of type ActionsParams`() {
        browserTest.clickStep("Step") {
            targetElement { locator = "#locatorId" }
        }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.CLICK, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `clickStep with target element add new step to browser test object`() {
        browserTest.clickStep("Step") {
            targetElement { locator = "#locatorId" }
        }
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("#locatorId", params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `navigateStep without URL throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.navigateStep("Step") {}
        }
    }

    @Test
    fun `navigateStep adds step with type go to url and params of type ActionsParams`() {
        browserTest
            .navigateStep("Step") {
                navigationUrl("https://synthetic-test.personio.de")
            }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.GO_TO_URL, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `navigationUrl adds url value to the step`() {
        browserTest
            .navigateStep("Step") {
                navigationUrl("https://synthetic-test.personio.de")
            }
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("https://synthetic-test.personio.de", params.value)
    }
}
