package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.actions.WaitParams
import com.personio.synthetics.step.ui.model.TargetElement
import kotlin.time.Duration

private const val DEFAULT_TEXT_DELAY_MILLIS: Long = 25

class StepsBuilder {
    private val steps = mutableListOf<SyntheticsStep>()

    fun build(): List<SyntheticsStep> {
        return steps
    }

    /**
     * Adds a new "type text" step to the synthetic browser test
     * @param stepName Name of the step
     * @param targetElement The web element where the text needs to be set
     */
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

    /**
     * Adds a new click step to the synthetic browser test
     * @param stepName Name of the step
     * @param targetElement The web element where the click is to be performed
     */
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

    /**
     * Adds a new hover step to the synthetic browser test
     * @param stepName Name of the step
     * @param targetElement The web element to which the hover has to be performed
     */
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

    /**
     * Adds a new wait step to the synthetic browser test
     * @param stepName Name of the step
     * @param duration The duration to wait
     */
    fun wait(
        stepName: String,
        duration: Duration,
    ) {
        addStep(
            stepName = stepName,
            type = SyntheticsStepType.WAIT,
            params = WaitParams(value = duration.inWholeSeconds.toInt()),
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
