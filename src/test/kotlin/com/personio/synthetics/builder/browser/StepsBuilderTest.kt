package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.TEST_LOCATOR
import com.personio.synthetics.TEST_STEP_NAME
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StepsBuilderTest {
    @Test
    fun `typeText adds typeText step`() {
        val sut = StepsBuilder()
        sut.typeText(TEST_STEP_NAME, TargetElement(TEST_LOCATOR), "any_text")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name(TEST_STEP_NAME)
                .type(SyntheticsStepType.TYPE_TEXT)
                .params(
                    ActionsParams(
                        element = TargetElement(TEST_LOCATOR).getElementObject(),
                        value = "any_text",
                        delay = 25,
                    ),
                ),
            result.first(),
        )
    }

    @Test
    fun `click adds click step`() {
        val sut = StepsBuilder()
        sut.click(TEST_STEP_NAME, TargetElement(TEST_LOCATOR))

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name(TEST_STEP_NAME)
                .type(SyntheticsStepType.CLICK)
                .params(
                    ActionsParams(
                        element = TargetElement(TEST_LOCATOR).getElementObject(),
                    ),
                ),
            result.first(),
        )
    }
}
