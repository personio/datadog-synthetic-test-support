package com.personio.synthetics.step.ui

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.Params
import com.personio.synthetics.step.addStep

/**
 * Adds a refresh step to the synthetic browser test
 * @param stepName Name of the step
 * @return Refresh type synthetic step object
 */
fun BrowserTest.refreshStep(stepName: String, f: (SyntheticsStep.() -> Unit)? = null) =
    addStep(stepName) {
        type = SyntheticsStepType.REFRESH
        params = Params()
        if (f != null) f()
    }
