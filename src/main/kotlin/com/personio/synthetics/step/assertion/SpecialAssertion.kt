package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep
import org.intellij.lang.annotations.Language

/**
 * Adds a new custom Javascript assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param code The javascript code to be executed for performing the assertion
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Custom javascript assertion type synthetic step object
 */
fun BrowserTest.customJavascriptAssertion(
    stepName: String,
    @Language("JS") code: String,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.ASSERT_FROM_JAVASCRIPT
    params = AssertionParams(
        code = code
    )
    if (f != null) f()
}

/**
 * Adds a new downloaded file assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SyntheticsCommonAssertionSteps object with the downloaded file assertion step added
 */
fun BrowserTest.downloadedFileAssertion(
    stepName: String,
    fileNameCheckType: SyntheticsCheckType,
    expectedFileName: String,
    fileSizeCheckType: SyntheticsCheckType,
    expectedFileSize: Long,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.ASSERT_FILE_DOWNLOAD
    params = AssertionParams()
    if (f != null) f()
}
