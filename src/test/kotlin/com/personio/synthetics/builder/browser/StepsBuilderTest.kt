package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.builder.browser.step.ScrollBuilder
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever
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

    @Test
    fun `goto adds go to URL step`() {
        val sut = StepsBuilder()
        sut.goto("any_name", "any_url")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.GO_TO_URL)
                .params(ActionsParams(value = "any_url")),
            result.first()
        )
    }

    @Test
    fun `scroll adds scroll step`() {
        val sut = StepsBuilder()
        sut.scroll("any_name", makeScrollBuilderMock(10 to 15)) {}

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.SCROLL)
                .params(SpecialActionsParams(x = 10, y = 15)),
            result.first()
        )
    }

    @Test
    fun `assertCurrentUrlIsEmpty adds assertion step with ASSERT_CURRENT_URL type and IS_EMPTY check`() {
        val sut = StepsBuilder()
        sut.assertCurrentUrlIsEmpty("any_name")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.ASSERT_CURRENT_URL)
                .params(AssertionParams(check = SyntheticsCheckType.IS_EMPTY)),
            result.first()
        )
    }

    @Test
    fun `assertCurrentUrlIsNotEmpty adds assertion step with ASSERT_CURRENT_URL type and NOT_IS_EMPTY check`() {
        val sut = StepsBuilder()
        sut.assertCurrentUrlIsNotEmpty("any_name")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.ASSERT_CURRENT_URL)
                .params(AssertionParams(check = SyntheticsCheckType.NOT_IS_EMPTY)),
            result.first()
        )
    }

    @Test
    fun `assertCurrentUrlContains adds assertion step with ASSERT_CURRENT_URL type and CONTAINS check`() {
        val sut = StepsBuilder()
        sut.assertCurrentUrlContains("any_name", "any_substring")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.ASSERT_CURRENT_URL)
                .params(AssertionParams(check = SyntheticsCheckType.CONTAINS, value = "any_substring")),
            result.first()
        )
    }

    @Test
    fun `assertCurrentUrlEquals adds assertion step with ASSERT_CURRENT_URL type and EQUALS check`() {
        val sut = StepsBuilder()
        sut.assertCurrentUrlEquals("any_name", "any_url")

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.ASSERT_CURRENT_URL)
                .params(AssertionParams(check = SyntheticsCheckType.EQUALS, value = "any_url")),
            result.first()
        )
    }

    @Test
    fun `pressKey adds PressKey step`() {
        val sut = StepsBuilder()
        sut.pressKey("any_name", Key.ENTER, Modifier.META, Modifier.CONTROL)

        val result = sut.build()

        assertEquals(1, result.count())
        assertEquals(
            SyntheticsStep()
                .name("any_name")
                .type(SyntheticsStepType.PRESS_KEY)
                .params(
                    PressKeyParams(
                        value = Key.ENTER.value,
                        modifiers = listOf(Modifier.META.value, Modifier.CONTROL.value)
                    )
                ),
            result.first()
        )
    }

    private fun makeScrollBuilderMock(coordinates: Pair<Int, Int>): ScrollBuilder {
        val mock = Mockito.mock(ScrollBuilder::class.java)
        whenever(mock.build()).thenReturn(coordinates)
        return mock
    }
}
