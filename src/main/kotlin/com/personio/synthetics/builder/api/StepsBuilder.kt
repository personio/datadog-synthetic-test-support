package com.personio.synthetics.builder.api

import com.datadog.api.client.v1.model.SyntheticsAPIStep

class StepsBuilder {
    private val steps = mutableListOf<SyntheticsAPIStep>()

    fun build(): List<SyntheticsAPIStep> {
        return steps
    }

    /**
     * Appends a SyntheticsAPIStep
     * @param name Step name
     * @param stepBuilder Optional step builder
     * @param init Configuration to be applied on the step builder
     */
    fun step(
        name: String,
        stepBuilder: StepBuilder = StepBuilder(name),
        init: StepBuilder.() -> Unit,
    ) {
        steps += stepBuilder.apply(init).build()
    }
}
