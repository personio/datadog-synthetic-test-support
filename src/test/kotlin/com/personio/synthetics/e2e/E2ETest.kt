package com.personio.synthetics.e2e

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsAssertionOperator
import com.datadog.api.v1.client.model.SyntheticsAssertionType
import com.personio.synthetics.client.syntheticBrowserTest
import com.personio.synthetics.config.addGlobalVariable
import com.personio.synthetics.config.setUrl
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.assertion.AssertionType
import com.personio.synthetics.step.api.addApiStep
import com.personio.synthetics.step.api.addAssertion
import com.personio.synthetics.step.api.extractHeaderValue
import com.personio.synthetics.step.api.method
import com.personio.synthetics.step.api.requestBody
import com.personio.synthetics.step.api.requestHeaders
import com.personio.synthetics.step.api.url
import com.personio.synthetics.step.ui.assertionStep
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.inputTextStep
import com.personio.synthetics.step.ui.targetElement
import com.personio.synthetics.step.ui.text
import org.junit.jupiter.api.Test

class E2ETest {
    @Test
    fun `create synthetic test`() {
        syntheticBrowserTest("[Test] Synthetic-Test-As-Code") {
            message = "{{#is_alert}} @slack-test_slack_channel Test Failed {{/is_alert}}"
            addTagsItem("synthetics-api")
            setUrl("https://synthetic-test.personio.de")
            addGlobalVariable("TEST_PASSWORD")
            inputTextStep()
                .name("Enter username")
                .targetElement("[name='email']")
                .text("test@personio.de")
            inputTextStep()
                .name("Enter password")
                .targetElement("[name='password']")
                .text("{{ TEST_PASSWORD }}")
            clickStep()
                .name("Click login button")
                .targetElement("[name='login']")
            clickStep()
                .name("Navigate to test page")
                .targetElement("[name='test-page']", LocatorType.CSS)
            assertionStep(AssertionType.ELEMENT_PRESENT)
                .name("Check if test link is present")
                .targetElement("[name='link-name']")
            addApiStep()
                .name("API Step")
                .requestBody("{\"userName\": \"test\"}")
                .requestHeaders(mutableMapOf("content-type" to "application/json"))
                .method(HTTPMethod.POST)
                .url("login")
                .addAssertion(
                    assertionType = SyntheticsAssertionType.STATUS_CODE,
                    operator = SyntheticsAssertionOperator.IS,
                    expected = 200
                )
                .addAssertion(
                    assertionType = SyntheticsAssertionType.HEADER,
                    property = "set-cookie",
                    operator = SyntheticsAssertionOperator.CONTAINS,
                    expected = "session="
                )
                .extractHeaderValue(
                    name = "SESSION",
                    field = "set-cookie",
                )
        }
    }
}
