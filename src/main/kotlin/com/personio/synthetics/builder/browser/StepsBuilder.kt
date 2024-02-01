package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.builder.browser.step.ScrollBuilder
import com.personio.synthetics.model.Params
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.actions.PressKeyParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.ui.model.TargetElement
import kotlin.time.Duration

private const val DEFAULT_TEXT_DELAY_MILLIS: Long = 25

class StepsBuilder {
    private val steps = mutableListOf<SyntheticsStep>()

    fun build(): List<SyntheticsStep> {
        return steps
    }

    fun typeText(name: String, text: String, targetElement: TargetElement) {
        addStep(
            name = name,
            type = SyntheticsStepType.TYPE_TEXT,
            params = ActionsParams(
                element = targetElement.getElementObject(),
                value = text,
                delay = DEFAULT_TEXT_DELAY_MILLIS
            )
        )
    }

    fun click(name: String, targetElement: TargetElement) {
        addStep(
            name = name,
            type = SyntheticsStepType.CLICK,
            params = ActionsParams(
                element = targetElement.getElementObject()
            )
        )
    }

    fun hover(name: String, targetElement: TargetElement) {
        addStep(
            name = name,
            type = SyntheticsStepType.HOVER,
            params = SpecialActionsParams(
                element = targetElement.getSpecialActionsElementObject()
            )
        )
    }

    fun wait(name: String, duration: Duration) {
        addStep(
            name = name,
            type = SyntheticsStepType.WAIT,
            params = WaitParams(value = duration.inWholeSeconds.toInt())
        )
    }

    fun refresh(name: String) {
        addStep(
            name = name,
            type = SyntheticsStepType.REFRESH,
            params = Params()
        )
    }

    fun goto(name: String, url: String) {
        addStep(
            name = name,
            type = SyntheticsStepType.GO_TO_URL,
            params = ActionsParams(value = url)
        )
    }

    fun scroll(name: String, scrollBuilder: ScrollBuilder = ScrollBuilder(), init: ScrollBuilder.() -> Unit) {
        val coordinates = scrollBuilder.apply(init).build()

        addStep(
            name = name,
            type = SyntheticsStepType.SCROLL,
            params = SpecialActionsParams(
                x = coordinates.first,
                y = coordinates.second
            )
        )
    }

    fun assertCurrentUrlIsEmpty(name: String) {
        addStep(
            name = name,
            type = SyntheticsStepType.ASSERT_CURRENT_URL,
            params = AssertionParams(
                check = SyntheticsCheckType.IS_EMPTY
            )
        )
    }

    fun assertCurrentUrlIsNotEmpty(name: String) {
        addStep(
            name = name,
            type = SyntheticsStepType.ASSERT_CURRENT_URL,
            params = AssertionParams(
                check = SyntheticsCheckType.NOT_IS_EMPTY
            )
        )
    }

    fun assertCurrentUrlContains(name: String, content: String) {
        addStep(
            name = name,
            type = SyntheticsStepType.ASSERT_CURRENT_URL,
            params = AssertionParams(
                check = SyntheticsCheckType.CONTAINS,
                value = content
            )
        )
    }

    fun assertCurrentUrlEquals(name: String, url: String) {
        addStep(
            name = name,
            type = SyntheticsStepType.ASSERT_CURRENT_URL,
            params = AssertionParams(
                check = SyntheticsCheckType.EQUALS,
                value = url
            )
        )
    }

    private fun addStep(name: String, type: SyntheticsStepType, params: Any) {
        steps += SyntheticsStep()
            .name(name)
            .type(type)
            .params(params)
    }

    fun pressKey(name: String, key: Key, vararg modifiers: Modifier) {
        addStep(
            name = name,
            type = SyntheticsStepType.PRESS_KEY,
            params = PressKeyParams(
                value = key.value,
                modifiers = modifiers.map { it.value }
            )
        )
    }
}
