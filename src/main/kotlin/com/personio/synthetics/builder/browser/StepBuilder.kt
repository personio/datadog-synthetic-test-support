package com.personio.synthetics.builder.browser

import com.datadog.api.client.v1.model.SyntheticsStep

class StepBuilder(val name: String) {
    fun build() = SyntheticsStep()
}
