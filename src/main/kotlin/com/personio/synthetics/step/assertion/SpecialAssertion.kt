package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.addStep

/**
 * Adds a new custom Javascript assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return Step object with the custom Javascript assertion step added
 */
fun BrowserTest.customJavascriptAssertion(stepName: String, f: Step.() -> Unit): Step =
    addStep(stepName, Step()) {
        type = SyntheticsStepType.ASSERT_FROM_JAVASCRIPT
        params = AssertionParams()
        f()
    }

/**
 * Adds a new downloaded file assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SyntheticsCommonAssertionSteps object with the downloaded file assertion step added
 */
fun BrowserTest.downloadedFileAssertion(stepName: String, f: AssertionStep.() -> Unit): AssertionStep =
    addStep(stepName, AssertionStep()) {
        type = SyntheticsStepType.ASSERT_FILE_DOWNLOAD
        params = AssertionParams()
        f()
    }
