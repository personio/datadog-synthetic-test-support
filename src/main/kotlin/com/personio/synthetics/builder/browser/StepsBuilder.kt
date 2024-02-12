package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.step.ui.model.TargetElement

private const val DEFAULT_TEXT_DELAY_MILLIS: Long = 25

class StepsBuilder {
    private val steps = mutableListOf<SyntheticsStep>()

    fun build(): List<SyntheticsStep> {
        return steps
    }

    fun typeText(
        stepName: String,
        targetElement: TargetElement,
        text: String,
    ) {
        addStep(
            stepName = stepName,
            type = SyntheticsStepType.TYPE_TEXT,
            params =
                ActionsParams(
                    element = targetElement.getElementObject(),
                    value = text,
                    delay = DEFAULT_TEXT_DELAY_MILLIS,
                ),
        )
    }

    fun click(
        stepName: String,
        targetElement: TargetElement,
    ) {
        addStep(
            stepName = stepName,
            type = SyntheticsStepType.CLICK,
            params =
                ActionsParams(
                    element = targetElement.getElementObject(),
                ),
        )
    }

    fun hover(
        stepName: String,
        targetElement: TargetElement,
    ) {
        addStep(
            stepName = stepName,
            type = SyntheticsStepType.HOVER,
            params =
                SpecialActionsParams(
                    element = targetElement.getSpecialActionsElementObject(),
                ),
        )
    }

    private fun addStep(
        stepName: String,
        type: SyntheticsStepType,
        params: Any,
    ) {
        steps +=
            SyntheticsStep()
                .name(stepName)
                .type(type)
                .params(params)
    }
}
