package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * @return SyntheticsStep object with the current URL assertion step added
 */
fun BrowserTest.currentUrlAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_CURRENT_URL
        params = AssertionParams()
    }

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * @return SyntheticsStep object with the current URL assertion step added
 */
fun BrowserTest.pageContainsTextAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_PAGE_CONTAINS
        params = AssertionParams()
    }

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * @return SyntheticsStep object with the current URL assertion step added
 */
fun BrowserTest.pageNotContainsTextAssertion(): SyntheticsStep =
    addStep {
        type = SyntheticsStepType.ASSERT_PAGE_LACKS
        params = AssertionParams()
    }
