package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StepsBuilderTest {
    @Test
    fun `typeText adds typeText step`() {
        val sut = StepsBuilder()
        sut.typeText("any_name", "any_text", TargetElement("any_locator"))

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.TYPE_TEXT)
                .params(
                    ActionsParams(
                        element = TargetElement("any_locator").getElementObject(),
                        value = "any_text",
                        delay = 25
                    )
                ),
            result.first()
        )
    }

    @Test
    fun `click adds click step`() {
        val sut = StepsBuilder()
        sut.click("any_name", TargetElement("any_locator"))

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.CLICK)
                .params(
                    ActionsParams(
                        element = TargetElement("any_locator").getElementObject()
                    )
                ),
            result.first()
        )
    }
}
