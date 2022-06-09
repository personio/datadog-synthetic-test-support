package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.Params
import com.personio.synthetics.step.addStep

/**
 * Adds a refresh step to the synthetic browser test
 * @param stepName Name of the step
 * @return RefreshStep object with the refresh step added
 */
fun BrowserTest.refreshStep(stepName: String): RefreshStep =
    addStep(stepName, RefreshStep()) {
        type = SyntheticsStepType.REFRESH
        params = Params()
    }

class RefreshStep : SyntheticsStep()
