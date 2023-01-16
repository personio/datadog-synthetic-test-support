package com.personio.synthetics.config

import com.personio.synthetics.client.BrowserTest

/**
 * Configures the alert message to be sent when the test fails
 * @param failureMessage The message that needs to be sent to the configured alert medium upon test failure
 * @param alertMedium Specify one or more alert mediums. It can be Slack channels, email addresses etc.
 * For Slack channels, the channel name should be prefixed with "@slack-"
 * For email address, the email should be prefixed with "@"
 * Example: Slack channel -> @slack-test_slack_channel
 * Email -> @firstName.lastName@domain.com
 */
fun BrowserTest.alertMessage(failureMessage: String, vararg alertMedium: String) {
    message += "${alertMedium.joinToString(" ")} {{#is_alert}} $failureMessage {{/is_alert}} "
}

/**
 * Configures the recovery message when the test recovers
 * @param recoveryMessage The message that needs to be sent upon recovery from a failure
 */
fun BrowserTest.recoveryMessage(recoveryMessage: String) {
    message += " {{#is_recovery}} $recoveryMessage {{/is_recovery}} "
}
