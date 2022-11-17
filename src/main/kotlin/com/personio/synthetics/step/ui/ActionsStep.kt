package com.personio.synthetics.step.ui

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.config.isDatadogVariable
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.ui.model.TargetElement
import java.net.URL

private const val DEFAULT_TEXT_DELAY: Long = 25 // in milliseconds

/**
 * Adds a new input text step to the synthetic browser test
 * @param stepName Name of the step
 * @param targetElement The web element where the text need to be set
 * @param text The text value that need to be set
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Input text type synthetic step object
 */
fun BrowserTest.inputTextStep(
    stepName: String,
    targetElement: TargetElement,
    text: String,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.TYPE_TEXT
    params = ActionsParams(
        element = targetElement.getElementObject(),
        value = text,
        delay = DEFAULT_TEXT_DELAY
    )
    if (f != null) f()
}

/**
 * Adds a new click step to the synthetic browser test
 * @param stepName Name of the step
 * @param targetElement The web element where the click is to be performed
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Click type synthetic step object
 */
fun BrowserTest.clickStep(
    stepName: String,
    targetElement: TargetElement,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.CLICK
    params = ActionsParams(
        element = targetElement.getElementObject()
    )
    if (f != null) f()
}

/**
 * Adds a navigate step to the synthetic browser test
 * @param stepName Name of the step
 * @param url The navigation url. You can pass url like the following
 * - only the location (eg: /test/page) for appending to the base url of the test
 * - pass full url including http(s)://
 * - global or local variable. For using those, use the function "fromVariable(variableName)" in the parameter
 *   for example /test/page/${fromVariable("TEST")}
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Navigate type synthetic step object
 */
fun BrowserTest.navigateStep(
    stepName: String,
    url: String,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.GO_TO_URL
    val target = if (url.isDatadogVariable()) url else {
        runCatching { URL(url) }
            .recover { URL(config.request.url + url) }
            .getOrThrow()
            .toString()
    }
    params = ActionsParams(value = target)
    if (f != null) f()
}
