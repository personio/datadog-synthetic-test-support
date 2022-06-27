package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.config.isDatadogVariable
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType
import java.net.URL

private const val DEFAULT_TEXT_DELAY: Long = 25 // in milliseconds

/**
 * Adds a new input text step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return ActionsStep object with the input text step added
 */
fun BrowserTest.inputTextStep(stepName: String, f: ActionsStep.() -> Unit): ActionsStep =
    addStep(stepName, ActionsStep()) {
        type = SyntheticsStepType.TYPE_TEXT
        params = ActionsParams(delay = DEFAULT_TEXT_DELAY)
        f()
        with(params as ActionsParams) {
            check(!value.isNullOrBlank()) { "Input text should be set for the step:'$stepName'" }
            checkNotNull(element) { "Target element should be set for the step:'$stepName'" }
        }
    }

/**
 * Adds a new click step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return ActionsStep object with the click step added
 */
fun BrowserTest.clickStep(stepName: String, f: ActionsStep.() -> Unit): ActionsStep =
    addStep(stepName, ActionsStep()) {
        type = SyntheticsStepType.CLICK
        params = ActionsParams()
        f()
        checkNotNull((params as ActionsParams).element) { "Target element should be set for the step:'$stepName'" }
    }

/**
 * Adds a navigate step with default url to the synthetic browser test
 * But default url value is set to use the base url
 * Url value should be overridden using SyntheticsStep url function
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return ActionsStep object with the navigate step added
 */
fun BrowserTest.navigateStep(stepName: String, f: ActionsStep.() -> Unit): ActionsStep =
    addStep(stepName, ActionsStep()) {
        type = SyntheticsStepType.GO_TO_URL
        params = ActionsParams(value = config?.request?.url)
        f()
        check(!(params as ActionsParams).value.isNullOrBlank()) { "URL to navigate should be set for the step:'$stepName'" }
    }

/**
 * Configure the Actions step for the synthetic browser test
 */
class ActionsStep : Step() {
    /**
     * Sets the text to be sent for the input text step
     * @param value Text to be sent for the input text step
     * For using global or local variable value, supply the parameter using the function "fromVariable(variableName)"
     * @return ActionsStep object with text value set
     */
    fun text(value: String) = apply {
        params = withParamType<ActionsParams> {
            copy(value = value)
        }
    }

    /**
     * Sets the URL to be sent for the navigate step
     * @param url value to be sent for the navigate step
     * For using global or local variable value, supply the parameter using the function "fromVariable(variableName)"
     * @return ActionsStep object with URL value set
     */
    fun navigationUrl(url: String) = apply {
        params = withParamType<ActionsParams> {
            val target = if (url.isDatadogVariable()) url else {
                runCatching { URL(url) }
                    .recover { URL(value + url) }
                    .getOrThrow()
                    .toString()
            }
            copy(value = target)
        }
    }
}
