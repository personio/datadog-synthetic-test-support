package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep

/**
 * Adds a new custom Javascript assertion step to the synthetic browser test
 * @return SyntheticsStep object with the custom Javascript assertion step added
 */
fun BrowserTest.customJavascriptAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_FROM_JAVASCRIPT
        params = AssertionParams()
    }

/**
 * Adds a new downloaded file assertion step to the synthetic browser test
 * @return SyntheticsStep object with the downloaded file assertion step added
 */
fun BrowserTest.downloadedFileAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_FILE_DOWNLOAD
        params = AssertionParams()
    }
