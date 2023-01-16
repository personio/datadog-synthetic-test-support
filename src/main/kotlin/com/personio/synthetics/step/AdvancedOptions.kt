package com.personio.synthetics.step

import com.datadog.api.client.v1.model.SyntheticsStep
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Adds a wait to the step before declaring it as failed
 * @param waitTime The timeout passed to the step before declaring it as failed (up to 300 seconds)
 * If not set, the step is declared as failed after 60 seconds
 * @return SyntheticsStep object with timeout set
 */
fun SyntheticsStep.waitBeforeDeclaringStepAsFailed(waitTime: Duration) = apply {
    require(waitTime in 0.seconds..300.seconds) {
        "Waiting time must not exceed 300 seconds."
    }
    timeout = waitTime.inWholeSeconds
}

/**
 * Allows the test to continue if the step fails
 * @param markTestAsFailed Set to true to mark the entire test as failed if the step fails (optional)
 * By default markTestAsFailed is set to false allowing the test to pass if the step fails
 * @return SyntheticsStep object with allowFailure and isCritical options set
 */

fun SyntheticsStep.continueWithTestIfStepFails(markTestAsFailed: Boolean = false) = apply {
    allowFailure = true
    isCritical = markTestAsFailed
}
