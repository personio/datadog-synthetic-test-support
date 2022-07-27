package com.personio.synthetics.step.ui

import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import java.lang.IllegalArgumentException
import kotlin.time.Duration.Companion.seconds

internal class SpecialActionsStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `waitStep adds the new step item to the browser test object`() {
        browserTest.waitStep(
            stepName = "Step",
            waitingTime = 1.seconds
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `waitStep adds step with type wait and params of type WaitParams`() {
        browserTest.waitStep(
            stepName = "Step",
            waitingTime = 1.seconds
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.WAIT, step?.type)
        assertInstanceOf(WaitParams::class.java, step?.params)
    }

    @Test
    fun `waitSteps adds passed waiting time to the params object`() {
        browserTest.waitStep(
            stepName = "Step",
            waitingTime = 1.seconds
        )

        assertEquals(1, (browserTest.steps?.get(0)?.params as WaitParams).value)
    }

    @Test
    fun `waitStep throws exception for value less than 1 second`() {
        assertThrows<IllegalArgumentException> {
            browserTest.waitStep(
                stepName = "Step",
                waitingTime = 0.seconds
            )
        }
    }

    @Test
    fun `waitStep throws exception for value bigger than 300 seconds`() {
        assertThrows<IllegalArgumentException> {
            browserTest.waitStep(
                stepName = "Step",
                waitingTime = 301.seconds
            )
        }
    }

    @Test
    fun `waitStep accepts additional configuration changes to the test step`() {
        browserTest.waitStep(
            stepName = "Step",
            waitingTime = 1.seconds
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
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
            targetElement(locator = "#locatorId")
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
                targetElement(locator = "#locatorId")
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
    fun `scrollStep accepts additional configuration changes to the test step`() {
        browserTest.scrollStep("Step") {
            horizontalScroll(1)
            verticalScroll(1)
            timeout = 10
        }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `hoverStep adds step with type hover and params of type SpecialActionsParams`() {
        browserTest.hoverStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId")
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.HOVER, step?.type)
        assertInstanceOf(SpecialActionsParams::class.java, step?.params)
    }

    @Test
    fun `hoverStep adds the passed target element to the params object`() {
        val locator = "#locatorId"
        browserTest.hoverStep(
            stepName = "Step",
            targetElement = TargetElement(locator)
        )

        val params = browserTest.steps?.get(0)?.params as SpecialActionsParams

        assertEquals(locator, params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `hoverStep accepts additional configuration changes to the test step`() {
        browserTest.hoverStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId")
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `pressKeyStep adds step with type press key and params of type PressKeyParams`() {
        browserTest
            .pressKeyStep(
                stepName = "Step",
                key = Key.ENTER
            )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.PRESS_KEY, step?.type)
        assertInstanceOf(PressKeyParams::class.java, step?.params)
    }

    @Test
    fun `pressKeyStep adds the passed key to the params object`() {
        val key = Key.ENTER
        browserTest
            .pressKeyStep(
                stepName = "Step",
                key = key
            )
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(key.value, params.value)
    }

    @Test
    fun `modifier adds the modifier value to the step`() {
        browserTest
            .pressKeyStep(
                stepName = "Step",
                key = Key.ENTER
            ) { modifiers(Modifier.OPT) }
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(listOf(Modifier.OPT.value), params.modifiers)
    }

    @Test
    fun `modifier adds multiple modifier values to the step`() {
        browserTest
            .pressKeyStep(
                stepName = "Step",
                key = Key.ENTER
            ) { modifiers(Modifier.OPT, Modifier.SHIFT) }

        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(listOf(Modifier.OPT.value, Modifier.SHIFT.value), params.modifiers)
    }

    @Test
    fun `pressKeyStep accepts additional configuration changes to the test step`() {
        browserTest
            .pressKeyStep(
                stepName = "Step",
                key = Key.ENTER
            ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }
}
