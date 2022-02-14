package com.personio.synthetics.step.javascript

import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.BrowserTest
import com.personio.synthetics.model.javascript.JSParams
import com.personio.synthetics.model.javascript.Variable

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
fun SyntheticsStep.code(code: String): SyntheticsStep {
    val jsParams = params as JSParams
    jsParams.code = code
    return this
}

/**
 * The variable where the extracted value to be stored
 * @param name Name of the variable to store the extracted value
 * @return SyntheticsStep object with the name of the variable set
 */
fun SyntheticsStep.variable(name: String): SyntheticsStep {
    val jsParams = params as JSParams
    jsParams.variable = Variable(name)
    return this
}
