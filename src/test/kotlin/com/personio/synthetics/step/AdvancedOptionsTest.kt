package com.personio.synthetics.step

import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.step.ui.refreshStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import kotlin.time.Duration.Companion.seconds

internal class AdvancedOptionsTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `waitBeforeDeclaringStepAsFailed adds the timeout to the browser test object`() {
        browserTest.refreshStep("Step") {
            waitBeforeDeclaringStepAsFailed(120.seconds)
        }

        assertEquals(120, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `waitBeforeDeclaringStepAsFailed throws an exception when wait time is lower than 0 seconds`() {
        assertThrows<IllegalArgumentException> {
            browserTest.refreshStep("Step") {
                waitBeforeDeclaringStepAsFailed(-(1).seconds)
            }
        }
    }

    @Test
    fun `waitBeforeDeclaringStepAsFailed throws an exception when wait time exceeds 300 seconds`() {
        assertThrows<IllegalArgumentException> {
            browserTest.refreshStep("Step") {
                waitBeforeDeclaringStepAsFailed(301.seconds)
            }
        }
    }

    @Test
    fun `continueWithTestIfStepFails adds an option to continue the test if step fails to the browser test object`() {
        browserTest.refreshStep("Step") {
            continueWithTestIfStepFails()
        }

        assertEquals(true, browserTest.steps?.get(0)?.allowFailure)
    }

    @Test
    fun `continueWithTestIfStepFails adds an option to continue the test if step fails and do not fail the entire test`() {
        browserTest.refreshStep("Step") {
            continueWithTestIfStepFails(false)
        }

        assertEquals(false, browserTest.steps?.get(0)?.isCritical)
    }

    @Test
    fun `continueWithTestIfStepFails adds an option to continue the test if step fails and fail the entire test`() {
        browserTest.refreshStep("Step") {
            continueWithTestIfStepFails(true)
        }

        assertEquals(true, browserTest.steps?.get(0)?.isCritical)
    }
}
