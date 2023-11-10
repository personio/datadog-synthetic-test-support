package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

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

    @Test
    fun `hover adds hover step`() {
        val sut = StepsBuilder()
        sut.hover("any_name", TargetElement("any_locator"))

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.HOVER)
                .params(
                    SpecialActionsParams(
                        element = TargetElement("any_locator").getSpecialActionsElementObject()
                    )
                ),
            result.first()
        )
    }

    @Test
    fun `wait adds wait step`() {
        val sut = StepsBuilder()
        sut.wait("any_name", 5.seconds)

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.WAIT)
                .params(WaitParams(5)),
            result.first()
        )
    }

    @Test
    fun `refresh adds refresh step`() {
        val sut = StepsBuilder()
        sut.refresh("any_name")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(SyntheticsStepType.REFRESH, result.first().type)
        assertEquals("any_name", result.first().name)
    }
}
