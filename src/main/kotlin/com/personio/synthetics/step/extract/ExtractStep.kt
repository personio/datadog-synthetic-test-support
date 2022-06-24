package com.personio.synthetics.step.extract

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.extract.ExtractParams
import com.personio.synthetics.model.extract.Variable
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType

/**
 * Add Extract from JS step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step such as code, variable, targetElement
 * @return ExtractStep object with this step added
 */
fun BrowserTest.extractFromJavascriptStep(stepName: String, f: ExtractStep.() -> Unit): ExtractStep =
    addStep(stepName, ExtractStep()) {
        type = SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT
        params = ExtractParams()
        f()
    }

/**
 * Add Extract text from element step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Add all the parameters required for this test step such as variable, target element
 * @return ExtractStep object with this step added
 */
fun BrowserTest.extractTextFromElementStep(stepName: String, f: ExtractStep.() -> Unit): ExtractStep =
    addStep(stepName, ExtractStep()) {
        type = SyntheticsStepType.EXTRACT_VARIABLE
        params = ExtractParams()
        f()
    }

/**
 * Configure the JS step for the synthetic browser test
 */
class ExtractStep : Step() {
    /**
     * The variable where the extracted value to be stored
     * @param name Name of the variable where the extracted value is to be stored
     * @return ExtractStep object after setting the variable
     */
    fun variable(name: String) = apply {
        params = withParamType<ExtractParams> {
            copy(variable = Variable(name = name))
        }
    }
}
