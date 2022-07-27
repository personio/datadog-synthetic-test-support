package com.personio.synthetics.step.assertion

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.addStep

/**
 * Adds a new current URL assertion step to the synthetic browser test
 * It checks if active page URL content has expected value
 * @param stepName Name of the step
 * @param check The type of check to be done on the current url
 * @param expectedContent The expected content for the url
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Current url assertion type synthetic step object
 */
fun BrowserTest.currentUrlAssertion(
    stepName: String,
    check: SyntheticsCheckType,
    expectedContent: String = "",
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    if (check !in listOf(SyntheticsCheckType.IS_EMPTY, SyntheticsCheckType.NOT_IS_EMPTY)) {
        check(!expectedContent.isNullOrEmpty()) { "Expected content is a required parameter for the passed check type $check in the step: $stepName" }
    }
    type = SyntheticsStepType.ASSERT_CURRENT_URL
    params = AssertionParams(
        check = check, value = expectedContent
    )
    if (f != null) f()
}

/**
 * Adds a new page contains text assertion step to the synthetic browser test
 * It checks if the page contains the given text
 * @param stepName Name of the step
 * @param expectedText The expected text to be verified in the page
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Page contains text assertion type synthetic step object
 */
fun BrowserTest.pageContainsTextAssertion(
    stepName: String,
    expectedText: String,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.ASSERT_PAGE_CONTAINS
    params = AssertionParams(
        value = expectedText
    )
    if (f != null) f()
}

/**
 * Adds a new Page not contains text assertion step to the synthetic browser test
 * It checks if the page does not contain the given text
 * @param stepName Name of the step
 * @param text The text that should not exist in the page
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Page not contains text assertion type synthetic step object
 */
fun BrowserTest.pageNotContainsTextAssertion(
    stepName: String,
    text: String,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.ASSERT_PAGE_LACKS
    params = AssertionParams(
        value = text
    )
    if (f != null) f()
}
