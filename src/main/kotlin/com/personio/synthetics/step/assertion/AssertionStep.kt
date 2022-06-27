package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.withParamType

/**
 * The base class for the assertions
 * Contains common functions used for the synthetic assertion step
 */
open class AssertionStep : Step() {
    /**
     * Sets the check type for the assertion step
     * @param checkType Check type for the assertion step
     * @return SyntheticsCommonAssertionSteps object with check type set
     */
    fun check(checkType: SyntheticsCheckType) = apply {
        params = withParamType<AssertionParams> {
            copy(check = checkType)
        }
    }

    /**
     * Sets the expected value to be checked for the assertion step
     * @param value String value to be checked against for the assertion step
     * For using global or local variable value, supply the parameter using the function "fromVariable(variableName)"
     * @return SyntheticsCommonAssertionSteps object with expected value set
     */
    fun expectedValue(value: String) = apply {
        params = withParamType<AssertionParams> {
            copy(value = value)
        }
    }
}
