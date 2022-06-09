package com.personio.synthetics.step.javascript

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.javascript.JSParams
import com.personio.synthetics.model.javascript.Variable
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType

/**
 * Add Extract from JS step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step
 * @return JSStep object with this step added
 */
fun BrowserTest.extractFromJavascriptStep(stepName: String, f: JSStep.() -> Unit): JSStep =
    addStep(stepName, JSStep()) {
        type = SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT
        params = JSParams()
        f()
    }

/**
 * Configure the JS step for the synthetic browser test
 */
class JSStep : Step() {
    /**
     * The variable where the extracted value to be stored
     * @param name Name of the variable to store the extracted value
     * @return JSStep object with the name of the variable set
     */
    fun variable(name: String) = apply {
        params = withParamType<JSParams> {
            copy(variable = Variable(name))
        }
    }
}
