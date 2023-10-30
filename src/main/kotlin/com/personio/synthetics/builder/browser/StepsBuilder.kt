package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.step.ui.model.TargetElement

private const val DEFAULT_TEXT_DELAY: Long = 25 // in milliseconds

class StepsBuilder {
    private val steps = mutableListOf<SyntheticsStep>()

    fun build(): List<SyntheticsStep> {
        return steps
    }

    fun input(name: String, text: String, targetElement: TargetElement) {
        addStep(
            name = name,
            type = SyntheticsStepType.TYPE_TEXT,
            params = ActionsParams(
                element = targetElement.getElementObject(),
                value = text,
                delay = DEFAULT_TEXT_DELAY
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

    fun hover(name: String,targetElement: TargetElement) {
        addStep(
            name = name,
            type = SyntheticsStepType.HOVER,
            params = SpecialActionsParams(element = targetElement.getSpecialActionsElementObject())
        )
    }

    fun addStep(name: String, type: SyntheticsStepType, params: Any) {
        steps += SyntheticsStep()
            .name(name)
            .type(type)
            .params(params)
    }

    fun step(name: String, stepBuilder: StepBuilder = StepBuilder(name), init: StepBuilder.() -> Unit) {
        steps += stepBuilder.apply(init).build()
    }
}
