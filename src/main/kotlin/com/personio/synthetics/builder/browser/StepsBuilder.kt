package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep

class StepsBuilder {
    private val steps = mutableListOf<SyntheticsStep>()

    fun build(): List<SyntheticsStep> {
        return steps
    }

    fun step(name: String, stepBuilder: StepBuilder = StepBuilder(name), init: StepBuilder.() -> Unit) {
        steps += stepBuilder.apply(init).build()
    }
}
