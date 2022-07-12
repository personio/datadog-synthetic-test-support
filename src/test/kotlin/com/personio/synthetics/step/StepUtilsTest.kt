package com.personio.synthetics.step

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.step.ui.SpecialActionsStep
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

internal class StepUtilsTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `withParamType returns the object which is an instance of the type at the caller`() {
        val step = browserTest.clickStep("click", TargetElement("#locator"))
        val params = step.withParamType<ActionsParams> {
            copy(element = TargetElement("#newLocator").getElementObject())
        }

        assertInstanceOf(ActionsParams::class.java, params)
    }

    @Test
    fun `withParamType throws exception when different params class is passed`() {
        val step = browserTest.clickStep("click", TargetElement("#locator"))
        assertThrows<IllegalArgumentException> {
            step.withParamType<SpecialActionsParams> {
                copy(element = TargetElement("#newLocator").getSpecialActionsElementObject())
            }
        }
    }

    @Test
    fun `addStep which accepts step type parameter adds a new synthetic step to the browser test`() {
        val syntheticStepType = SyntheticsStepType.SCROLL
        browserTest.addStep("Step", SpecialActionsStep()) {
            type = syntheticStepType
        }

        assertEquals(1, browserTest.steps?.size)
        assertEquals(syntheticStepType, browserTest.steps?.get(0)?.type)
    }

    @Test
    fun `addStep which accepts step type parameter assigns the passed step name parameter to the step`() {
        val stepName = "Step"
        browserTest.addStep(stepName, SpecialActionsStep()) {
            type = SyntheticsStepType.SCROLL
        }

        assertEquals(stepName, browserTest.steps?.get(0)?.name)
    }

    @Test
    fun `addStep which accepts step type parameter throws exception if step name is empty`() {
        assertThrows<IllegalStateException> {
            browserTest.addStep("", SpecialActionsStep()) {
                type = SyntheticsStepType.SCROLL
            }
        }
    }

    @Test
    fun `addStep adds a new synthetic step to the browser test`() {
        val syntheticStepType = SyntheticsStepType.TYPE_TEXT
        browserTest.addStep("Step") {
            type = syntheticStepType
        }

        assertEquals(1, browserTest.steps?.size)
        assertEquals(syntheticStepType, browserTest.steps?.get(0)?.type)
    }

    @Test
    fun `addStep assigns the passed step name parameter to the step`() {
        val stepName = "Step"
        browserTest.addStep(stepName) {
            type = SyntheticsStepType.TYPE_TEXT
        }

        assertEquals(stepName, browserTest.steps?.get(0)?.name)
    }

    @Test
    fun `addStep throws exception if step name is empty`() {
        assertThrows<IllegalStateException> {
            browserTest.addStep("") {
                type = SyntheticsStepType.TYPE_TEXT
            }
        }
    }
}
