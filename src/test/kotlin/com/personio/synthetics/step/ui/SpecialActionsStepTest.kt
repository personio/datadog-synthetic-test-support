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
    fun `scrollStep adds the new step item to the browser test object`() {
        browserTest.scrollStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `scrollStep adds step with type scroll and params of type SpecialActionsParams`() {
        browserTest.scrollStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.SCROLL, step?.type)
        assertInstanceOf(SpecialActionsParams::class.java, step?.params)
    }

    @Test
    fun `horizontalScroll adds horizontal scroll value to the step`() {
        browserTest
            .scrollStep("Step") {
                horizontalScroll(1)
            }
        val params = browserTest.steps?.get(0)?.params as SpecialActionsParams

        assertEquals(1, params.x)
    }

    @Test
    fun `verticalScroll adds vertical scroll value to the step`() {
        browserTest
            .scrollStep("Step") {
                verticalScroll(1)
            }
        val params = browserTest.steps?.get(0)?.params as SpecialActionsParams

        assertEquals(1, params.y)
    }

    @Test
    fun `hoverStep adds the new step item to the browser test object`() {
        browserTest.hoverStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `hoverStep adds step with type scroll and params of type SpecialActionsParams`() {
        browserTest.hoverStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.HOVER, step?.type)
        assertInstanceOf(SpecialActionsParams::class.java, step?.params)
    }

    @Test
    fun `pressKeyStep adds the new step item to the browser test object`() {
        browserTest.pressKeyStep("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `pressKeyStep adds step with type scroll and params of type PressKeyParams`() {
        browserTest.pressKeyStep("Step") {}
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.PRESS_KEY, step?.type)
        assertInstanceOf(PressKeyParams::class.java, step?.params)
    }

    @Test
    fun `key adds key value to the step`() {
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
                modifiers(Modifier.OPT)
            }
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(listOf(Modifier.OPT.value), params.modifiers)
    }

    @Test
    fun `modifier adds multiple modifier values to the step`() {
        browserTest
            .pressKeyStep("Step") {
                modifiers(Modifier.OPT, Modifier.SHIFT)
            }
        val params = browserTest.steps?.get(0)?.params as PressKeyParams

        assertEquals(listOf(Modifier.OPT.value, Modifier.SHIFT.value), params.modifiers)
    }
}
