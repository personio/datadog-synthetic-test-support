package com.personio.synthetics.step.ui

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.actions.ActionsParams
import java.net.URL

private const val DEFAULT_TEXT_DELAY: Long = 25 // in milliseconds

/**
 * Adds a new input text step to the synthetic browser test
 * @return SyntheticsStep object with the input text step added
 */
fun BrowserTest.inputTextStep(): SyntheticsStep {
    val textStep = SyntheticsStep()
        .type(SyntheticsStepType.TYPE_TEXT)
        .params(ActionsParams(delay = DEFAULT_TEXT_DELAY))
    addStepsItem(textStep)
    return textStep
}

/**
 * Adds a new click step to the synthetic browser test
 * @return SyntheticsStep object with the click step added
 */
fun BrowserTest.clickStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.CLICK)
        .params(ActionsParams())
    addStepsItem(step)
    return step
}

/**
 * Sets the text to be sent for the input text step
 * @param value Text to be sent for the input text step
 * @return SyntheticsStep object with text value set
 */
fun SyntheticsStep.text(value: String) = apply {
    params = (params as? ActionsParams ?: throw IllegalArgumentException("Cannot use text on params $params"))
        .copy(value = value)
}

/**
 * Adds a navigate step with default url to the synthetic browser test
 * But default url value is set to use the base url
 * Url value should be overridden using SyntheticsStep url function
 * @return SyntheticsStep object with the navigate step added
 */
fun BrowserTest.navigateStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.GO_TO_URL)
        .params(ActionsParams(value = config?.request?.url))
    addStepsItem(step)
    return step
}

/**
 * Sets the URL to be sent for the navigate step
 * @param url value to be sent for the navigate step
 * @return SyntheticsStep object with URL value set
 */
fun SyntheticsStep.navigationUrl(url: String) = apply {
    params = with(params as? ActionsParams ?: throw IllegalArgumentException("Cannot use navigationUrl on params $params")) {
        val target = runCatching { URL(url) }
            .recover { URL(value + url) }
            .getOrThrow()
        copy(value = target.toString())
    }
}

/**
 * Adds a refresh step to the synthetic browser test
 * @return SyntheticsStep object with the refresh step added
 */
fun BrowserTest.refreshStep(): SyntheticsStep {
    val step = SyntheticsStep()
        .type(SyntheticsStepType.REFRESH)
        .params(ActionsParams())
    addStepsItem(step)
    return step
}
