package com.personio.synthetics.step.assertion

import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.datadog.api.v1.client.model.SyntheticsStep
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.step.withParamType

/**
 * Sets the check type for the assertion step
 * @param checkType Check type for the assertion step
 * @return SyntheticsStep object with check type set
 */
fun SyntheticsStep.check(checkType: SyntheticsCheckType) = apply {
    params = withParamType<AssertionParams> {
        copy(check = checkType)
    }
}

/**
 * Sets the expected value to be checked for the assertion step
 * @param value String value to be checked against for the assertion step
 * @return SyntheticsStep object with expected value set
 */
fun SyntheticsStep.expectedValue(value: String) = apply {
    params = withParamType<AssertionParams> {
        copy(value = value)
    }
}
