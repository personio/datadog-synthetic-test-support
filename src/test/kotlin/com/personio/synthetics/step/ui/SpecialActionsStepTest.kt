package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import kotlin.time.Duration.Companion.seconds

internal class SpecialActionsStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `waitStep adds the new step item to the browser test object`() {
        browserTest.waitStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `waitStep adds step with type wait and params of type WaitParams`() {
        browserTest.waitStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.WAIT, step?.type)
        assertInstanceOf(WaitParams::class.java, step?.params)
    }

    @Test
    fun `waitingTime adds waiting time value to the step`() {
        browserTest
            .waitStep("Step") {
                waitingTime(1.seconds)
            }
        val params = browserTest.steps?.get(0)?.params as WaitParams

        assertEquals(1, params.value)
    }

    @Test
    fun `waitingTime throws exception for value less than 1 second`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.waitStep("Step") {
                waitingTime(0.seconds)
            }
        }
    }

    @Test
    fun `waitingTime throws exception for value bigger than 300 seconds`() {
        assertThrows(IllegalArgumentException::class.java) {
            browserTest.waitStep("Step") {
                waitingTime(301.seconds)
            }
        }
    }

    @Test
    fun `scrollStep without x,y coordinates and target element throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {}
        }
    }

    @Test
    fun `scrollStep with x,y coordinates adds step of type scroll and params of type SpecialActionsParams`() {
        browserTest.scrollStep("Step") {
            horizontalScroll(1)
            verticalScroll(1)
        }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.SCROLL, step?.type)
        assertInstanceOf(SpecialActionsParams::class.java, step?.params)
    }

    @Test
    fun `scrollStep with target element adds step of type scroll and params of type SpecialActionsParams`() {
        browserTest.scrollStep("Step") {
            targetElement { locator = "#locatorId" }
        }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.SCROLL, step?.type)
        assertInstanceOf(SpecialActionsParams::class.java, step?.params)
    }

    @Test
    fun `scrollStep with x,y coordinates add new step to the browser test object`() {
        browserTest.scrollStep("Step") {
            horizontalScroll(1)
            verticalScroll(1)
        }
        val params = browserTest.steps?.get(0)?.params as SpecialActionsParams

        assertEquals(1, params.x)
        assertEquals(1, params.y)
    }

    @Test
    fun `scrollStep with target element adds new step to the browser test object`() {
        browserTest
            .scrollStep("Step") {
                targetElement { locator = "#locatorId" }
            }
        val params = browserTest.steps?.get(0)?.params as SpecialActionsParams

        assertEquals("#locatorId", params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `scrollStep without y coordinate throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {
                horizontalScroll(1)
            }
        }
    }

    @Test
    fun `scrollStep without x coordinate throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {
                verticalScroll(1)
            }
        }
    }

    @Test
    fun `scrollStep throws exception for x coordinate less than 0 pixels`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {
                verticalScroll(1)
                horizontalScroll(-1)
            }
        }
    }

    @Test
    fun `scrollStep throws exception for y coordinate less than 0 pixels`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {
                verticalScroll(-1)
                horizontalScroll(1)
            }
        }
    }

    @Test
    fun `scrollStep throws exception for x coordinate greater than 9999 pixels`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {
                verticalScroll(1)
                horizontalScroll(10000)
            }
        }
    }

    @Test
    fun `scrollStep throws exception for y coordinate greater than 9999 pixels`() {
        assertThrows<IllegalStateException> {
            browserTest.scrollStep("Step") {
                verticalScroll(10000)
                horizontalScroll(1)
            }
        }
    }

    @Test
    fun `hoverStep without element throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.hoverStep("Step") {}
        }
    }

    @Test
    fun `hoverStep adds step with type hover and params of type SpecialActionsParams`() {
        browserTest.hoverStep("Step") { targetElement { locator = "#locatorId" } }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.HOVER, step?.type)
        assertInstanceOf(SpecialActionsParams::class.java, step?.params)
    }

    @Test
    fun `hoverStep adds the new step item to the browser test object`() {
        browserTest.hoverStep("Step") { targetElement { locator = "#locatorId" } }

        val params = browserTest.steps?.get(0)?.params as SpecialActionsParams

        assertEquals("#locatorId", params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `pressKeyStep without key to be pressed throws exception`() {
        assertThrows<IllegalStateException> {
            browserTest.pressKeyStep("Step") {}
        }
    }

    @Test
    fun `pressKeyStep adds step with type press key and params of type PressKeyParams`() {
        browserTest
            .pressKeyStep("Step") {
                key(Key.ENTER)
            }
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.PRESS_KEY, step?.type)
        assertInstanceOf(PressKeyParams::class.java, step?.params)
    }

    @Test
    fun `pressKeyStep adds the new step item to the browser test object`() {
        browserTest
            .pressKeyStep("Step") {
                key(Key.ENTER)
            }
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals("Enter", params.value)
    }

    @Test
    fun `modifier adds the modifier value to the step`() {
        browserTest
            .pressKeyStep("Step") {
                key(Key.ENTER)
                modifiers(Modifier.OPT)
            }
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(listOf(Modifier.OPT.value), params.modifiers)
    }

    @Test
    fun `modifier adds multiple modifier values to the step`() {
        browserTest
            .pressKeyStep("Step") {
                key(Key.ENTER)
                modifiers(Modifier.OPT, Modifier.SHIFT)
            }
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(listOf(Modifier.OPT.value, Modifier.SHIFT.value), params.modifiers)
    }
}
