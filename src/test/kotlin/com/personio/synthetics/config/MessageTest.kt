package com.personio.synthetics.config

import com.personio.synthetics.client.BrowserTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class MessageTest {
    private val browserTest = BrowserTest("Test", mock(), mock())

    @Test
    fun `alertMessage function sets the passed message to the message field`() {
        val message = "Alert Message"
        browserTest.alertMessage(message)
        assertEquals(getFormattedAlertMessage(message), browserTest.message)
    }

    @Test
    fun `alertMessage function sets the passed alert medium to the message field`() {
        val message = "Alert Message"
        val slackChannel = "@slack-test_slack_channel"
        browserTest.alertMessage(message, slackChannel)
        val expectedMessage = "$slackChannel${getFormattedAlertMessage(message)}"
        assertEquals(expectedMessage, browserTest.message)
    }

    @Test
    fun `alertMessage function accepts multiple alert medium to be passed and add it to the message field`() {
        val message = "Alert Message"
        val slackChannel1 = "@slack-test_slack-channel_1"
        val slackChannel2 = "@slack-test_slack_channel_2"
        browserTest.alertMessage(message, slackChannel1, slackChannel2)
        val expectedMessage = "$slackChannel1 $slackChannel2${getFormattedAlertMessage(message)}"
        assertEquals(expectedMessage, browserTest.message)
    }

    @Test
    fun `recoveryMessage function sets the passed message to the message field`() {
        val message = "Recovery Message"
        browserTest.recoveryMessage(message)
        assertEquals(getFormattedRecoveryMessage(message), browserTest.message)
    }

    @Test
    fun `alertMessage and recoveryMessage functions sets both the passed messages to the message field`() {
        val alertMessage = "Alert Message"
        val recoveryMessage = "Recovery Message"
        browserTest.apply {
            alertMessage(alertMessage)
            recoveryMessage(recoveryMessage)
        }
        val expectedMessage = "${getFormattedAlertMessage(alertMessage)}${getFormattedRecoveryMessage(recoveryMessage)}"
        assertEquals(expectedMessage, browserTest.message)
    }

    private fun getFormattedAlertMessage(message: String): String =
        " {{#is_alert}} $message {{/is_alert}} "

    private fun getFormattedRecoveryMessage(message: String): String =
        " {{#is_recovery}} $message {{/is_recovery}} "
}
