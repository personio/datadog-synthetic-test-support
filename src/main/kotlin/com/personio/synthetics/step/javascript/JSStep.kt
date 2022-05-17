package com.personio.synthetics.step.javascript

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.javascript.JSParams
import com.personio.synthetics.model.javascript.Variable
import org.intellij.lang.annotations.Language

/**
 * Add Extract from JS step to the synthetic browser test
 * @return SyntheticsStep object with this step added
 */
fun BrowserTest.addExtractFromJavascriptStep(): SyntheticsStep {
    val jsStep = SyntheticsStep()
        .type(SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT)
        .params(JSParams())
    addStepsItem(jsStep)
    return jsStep
}

/**
 * Pass the code to be executed in the extract from JS step
 * @param code JS code to be executed. The code should start with "return"
 * @return SyntheticsStep object with the code set to the extract from JS step
 */
fun SyntheticsStep.code(@Language("JS") code: String) = apply {
    params = (params as? JSParams ?: throw IllegalArgumentException("Cannot use code on params $params"))
        .copy(code = code)
}

/**
 * The variable where the extracted value to be stored
 * @param name Name of the variable to store the extracted value
 * @return SyntheticsStep object with the name of the variable set
 */
fun SyntheticsStep.variable(name: String) = apply {
    params = (params as? JSParams ?: throw IllegalArgumentException("Cannot use variable on params $params"))
        .copy(variable = Variable(name))
}
