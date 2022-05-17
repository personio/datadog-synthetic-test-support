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
            message = "{{#is_alert}} @slack-not_gitlab_qa Test Failed {{/is_alert}}"
            addTagsItem("synthetics-api")
            setUrl("https://denys-demo.personio.de")
            addGlobalVariable("PASSWORD_PROD_TEST")
            inputTextStep()
                .name("Enter username")
                .targetElement("#email")
                .text("qa-engineering@personio.de")
            inputTextStep()
                .name("Enter password")
                .targetElement("#password")
                .text("{{ PASSWORD_PROD_TEST }}")
            clickStep()
                .name("Click login button")
                .targetElement("button[type=\"submit\"]")
            clickStep()
                .name("Navigate to settings page")
                .targetElement("[data-test-id=\"navsidebar-settings\"]", LocatorType.CSS)
            assertionStep(AssertionType.ELEMENT_PRESENT)
                .name("Check if cost centers link is present")
                .targetElement("[data-test-id=\"settings-listitem-cost-centers\"]")
            addApiStep()
                .name("API Step")
                .requestBody("{\"email\": \"abc\"}")
                .requestHeaders(mutableMapOf("content-type" to "application/json"))
                .method(HTTPMethod.POST)
                .url("login/wizard")
                .addAssertion(
                    assertionType = SyntheticsAssertionType.STATUS_CODE,
                    operator = SyntheticsAssertionOperator.IS,
                    expected = 302
                )
                .addAssertion(
                    assertionType = SyntheticsAssertionType.HEADER,
                    property = "set-cookie",
                    operator = SyntheticsAssertionOperator.CONTAINS,
                    expected = "personio_session="
                )
                .extractHeaderValue(
                    name = "PERSONIO_SESSION",
                    field = "set-cookie",
                )
        }
    }
}
