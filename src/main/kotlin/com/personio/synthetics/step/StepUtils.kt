package com.personio.synthetics.step

import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.client.BrowserTest

internal inline fun BrowserTest.addStep(f: SyntheticsStep.() -> Unit): SyntheticsStep =
    SyntheticsStep().apply {
        f()
        addStepsItem(this)
    }

internal inline fun <reified T> SyntheticsStep.withParamType(f: T.() -> T): T =
    (params as? T ?: throw IllegalArgumentException("Expected ${T::class} but found ${params!!::class}")).f()
