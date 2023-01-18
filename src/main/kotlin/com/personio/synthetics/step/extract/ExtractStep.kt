package com.personio.synthetics.step.extract

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.extract.ExtractParams
import com.personio.synthetics.model.extract.Variable
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.ui.model.TargetElement
import org.intellij.lang.annotations.Language

/**
 * Adds a new step for extracting variable from Javascript to the synthetic browser test
 * @param stepName Name of the step
 * @param variableName Name of the variable to which the result of the JS code is to be saved
 * @param code The Javascript code to be executed
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with extractFromJavascriptStep added
 */
fun BrowserTest.extractFromJavascriptStep(
    stepName: String,
    variableName: String,
    @Language("JS") code: String,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.EXTRACT_FROM_JAVASCRIPT
    params = ExtractParams(
        code = code,
        variable = Variable(name = variableName)
    )
    if (f != null) f()
}

/**
 * Adds a new step for extracting text from an element to the synthetic browser test
 * @param stepName Name of the step
 * @param variableName Name of the Datadog variable to which the text from element need to be extracted to
 * @param targetElement The target element from which the text need to be extracted
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with extractTextFromElementStep added
 */
fun BrowserTest.extractTextFromElementStep(
    stepName: String,
    variableName: String,
    targetElement: TargetElement,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.EXTRACT_VARIABLE
    params = ExtractParams(
        variable = Variable(name = variableName),
        element = targetElement.getElementObject()
    )
    if (f != null) f()
}
