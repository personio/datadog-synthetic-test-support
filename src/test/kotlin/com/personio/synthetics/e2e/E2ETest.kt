package com.personio.synthetics.e2e

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsAssertionOperator
import com.datadog.api.v1.client.model.SyntheticsAssertionType
import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.personio.synthetics.client.syntheticBrowserTest
import com.personio.synthetics.config.addGlobalVariable
import com.personio.synthetics.config.setUrl
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.step.api.addApiStep
import com.personio.synthetics.step.api.addAssertion
import com.personio.synthetics.step.api.extractHeaderValue
import com.personio.synthetics.step.api.method
import com.personio.synthetics.step.api.requestBody
import com.personio.synthetics.step.api.requestHeaders
import com.personio.synthetics.step.api.url
import com.personio.synthetics.step.assertion.attribute
import com.personio.synthetics.step.assertion.check
import com.personio.synthetics.step.assertion.currentUrlAssertion
import com.personio.synthetics.step.assertion.customJavascriptAssertion
import com.personio.synthetics.step.assertion.elementAttributeAssertion
import com.personio.synthetics.step.assertion.elementContentAssertion
import com.personio.synthetics.step.assertion.elementPresentAssertion
import com.personio.synthetics.step.assertion.expectedValue
import com.personio.synthetics.step.assertion.pageContainsTextAssertion
import com.personio.synthetics.step.assertion.pageNotContainsTextAssertion
import com.personio.synthetics.step.code
import com.personio.synthetics.step.targetElement
import com.personio.synthetics.step.ui.addModifier
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.horizontalScroll
import com.personio.synthetics.step.ui.hoverStep
import com.personio.synthetics.step.ui.inputTextStep
import com.personio.synthetics.step.ui.key
import com.personio.synthetics.step.ui.navigateStep
import com.personio.synthetics.step.ui.navigationUrl
import com.personio.synthetics.step.ui.pressKeyStep
import com.personio.synthetics.step.ui.refreshStep
import com.personio.synthetics.step.ui.scrollStep
import com.personio.synthetics.step.ui.text
import com.personio.synthetics.step.ui.verticalScroll
import com.personio.synthetics.step.ui.waitStep
import com.personio.synthetics.step.ui.waitingTime
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.time.Duration.Companion.seconds

class E2ETest {
    @Test
    fun `create synthetic test`() {
        syntheticBrowserTest("[Test] Synthetic-Test-As-Code") {
            message = "{{#is_alert}} @slack-test_slack_channel Test Failed {{/is_alert}}"
            addTagsItem("synthetics-api")
            setUrl(URL("https://synthetic-test.personio.de"))
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
            currentUrlAssertion()
                .name("Check current URL")
                .expectedValue("https://synthetic-test.personio.de")
                .check(SyntheticsCheckType.CONTAINS)
            pageContainsTextAssertion()
                .name("Check that test element is present on the page")
                .expectedValue("string that should be present")
            pageNotContainsTextAssertion()
                .name("Check text is not present on the page")
                .expectedValue("string that should not be present")
            clickStep()
                .name("Navigate to test page")
                .targetElement("[name='test-page']", LocatorType.CSS)
            elementPresentAssertion()
                .name("Check if test link is present")
                .targetElement("[name='link-name']")
            elementContentAssertion()
                .name("Check text of the test link")
                .targetElement("[name='link-name']")
                .expectedValue("Test")
                .check(SyntheticsCheckType.EQUALS)
            elementAttributeAssertion()
                .name("Check that href of the test link is not empty")
                .targetElement("[name='link-name']")
                .attribute("href")
                .expectedValue("")
                .check(SyntheticsCheckType.NOT_IS_EMPTY)
            navigateStep()
                .name("Navigate to test page by URL")
                .navigationUrl("https://synthetic-test.personio.de/test")
            customJavascriptAssertion()
                .name("Check custom JS")
                .code("return true;")
            refreshStep()
                .name("Refresh test page")
            waitStep()
                .name("Wait for a few seconds on the page")
                .waitingTime(10.seconds)
            scrollStep()
                .name("Scroll to test element")
                .targetElement("[name='test-element']")
                .horizontalScroll(1)
                .verticalScroll(1)
            hoverStep()
                .name("Hover over test element")
                .targetElement("[name='test-element']")
            pressKeyStep()
                .name("Press Ctrl+Shift+Backspace keys")
                .key("Backspace")
                .addModifier(Modifier.CONTROL)
                .addModifier(Modifier.SHIFT)
            addApiStep()
                .name("API Step")
                .requestBody("{\"userName\": \"test\"}")
                .requestHeaders(mutableMapOf("content-type" to "application/json"))
                .method(HTTPMethod.POST)
                .url("/login")
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
