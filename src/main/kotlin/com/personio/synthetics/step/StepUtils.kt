package com.personio.synthetics.step

import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.client.BrowserTest

internal inline fun <reified T> SyntheticsStep.withParamType(f: T.() -> T): T =
    (params as? T ?: throw IllegalArgumentException("Expected ${T::class} but found ${params!!::class}")).f()

internal inline fun <reified T> BrowserTest.addStep(stepName: String, step: T, f: T.() -> Unit): T {
    check(stepName.isNotBlank()) { "The step name must not be empty" }
    with(step as? SyntheticsStep ?: throw IllegalArgumentException("Expected object of ${SyntheticsStep::class} or it's child but got ${T::class}")) {
        name = stepName
        addStepsItem(this)
    }
    return step.apply { f() }
}
