package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SyntheticsCommonAssertionSteps object with the current URL assertion step added
 */
fun BrowserTest.currentUrlAssertion(stepName: String, f: AssertionStep.() -> Unit): AssertionStep =
    addStep(stepName, AssertionStep()) {
        type = SyntheticsStepType.ASSERT_CURRENT_URL
        params = AssertionParams()
        f()
    }

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SyntheticsCommonAssertionSteps object with the current URL assertion step added
 */
fun BrowserTest.pageContainsTextAssertion(stepName: String, f: AssertionStep.() -> Unit): AssertionStep =
    addStep(stepName, AssertionStep()) {
        type = SyntheticsStepType.ASSERT_PAGE_CONTAINS
        params = AssertionParams()
        f()
    }

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return SyntheticsCommonAssertionSteps object with the current URL assertion step added
 */
fun BrowserTest.pageNotContainsTextAssertion(stepName: String, f: AssertionStep.() -> Unit): AssertionStep =
    addStep(stepName, AssertionStep()) {
        type = SyntheticsStepType.ASSERT_PAGE_LACKS
        params = AssertionParams()
        f()
    }
