package com.personio.synthetics.step.ui

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.Params
import com.personio.synthetics.step.addStep

/**
 * Adds a new navigation step for refreshing the active page to the synthetic browser test
 * @param stepName Name of the step
 * @return Synthetic step object with refreshStep added
 */
fun BrowserTest.refreshStep(stepName: String, f: (SyntheticsStep.() -> Unit)? = null) =
    addStep(stepName) {
        type = SyntheticsStepType.REFRESH
        params = Params()
        if (f != null) f()
    }
