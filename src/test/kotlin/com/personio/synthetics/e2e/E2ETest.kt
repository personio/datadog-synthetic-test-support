package com.personio.synthetics.e2e

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsAssertionOperator
import com.datadog.api.v1.client.model.SyntheticsAssertionType
import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.personio.synthetics.client.syntheticBrowserTest
import com.personio.synthetics.config.addGlobalVariable
import com.personio.synthetics.config.setBaseUrl
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.step.api.apiStep
import com.personio.synthetics.step.assertion.currentUrlAssertion
import com.personio.synthetics.step.assertion.customJavascriptAssertion
import com.personio.synthetics.step.assertion.elementAttributeAssertion
import com.personio.synthetics.step.assertion.elementContentAssertion
import com.personio.synthetics.step.assertion.elementPresentAssertion
import com.personio.synthetics.step.assertion.pageContainsTextAssertion
import com.personio.synthetics.step.assertion.pageNotContainsTextAssertion
import com.personio.synthetics.step.javascript.extractFromJavascriptStep
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.hoverStep
import com.personio.synthetics.step.ui.inputTextStep
import com.personio.synthetics.step.ui.navigateStep
import com.personio.synthetics.step.ui.pressKeyStep
import com.personio.synthetics.step.ui.refreshStep
import com.personio.synthetics.step.ui.scrollStep
import com.personio.synthetics.step.ui.waitStep
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.time.Duration.Companion.seconds

class E2ETest {
    @Test
    fun `create synthetic test`() {
        syntheticBrowserTest("[Test] Synthetic-Test-As-Code") {
            message = "{{#is_alert}} @slack-test_slack_channel Test Failed {{/is_alert}}"
            addTagsItem("synthetics-api")
            setBaseUrl(URL("https://synthetic-test.personio.de"))
            addGlobalVariable("TEST_PASSWORD")
            inputTextStep("Enter username") {
                targetElement {
                    locator = "[name='email']"
                }
                text("test@personio.de")
            }
            inputTextStep("Enter password") {
                targetElement {
                    locator = "[name='password']"
                }
                text("{{ TEST_PASSWORD }}")
            }
            clickStep("Click login button") {
                targetElement {
                    locator = "[name='login']"
                }
            }
            currentUrlAssertion("Check current URL") {
                expectedValue("https://synthetic-test.personio.de")
                check(SyntheticsCheckType.CONTAINS)
            }
            pageContainsTextAssertion("Check that test element is present on the page") {
                expectedValue("string that should be present")
            }
            pageNotContainsTextAssertion("Check text is not present on the page") {
                expectedValue("string that should not be present")
            }
            clickStep("Navigate to test page") {
                targetElement {
                    locator = "[name='test-page']"
                }
            }
            elementPresentAssertion("Check if test link is present") {
                targetElement {
                    locator = "[name='link-name']"
                }
            }
            elementContentAssertion("Check text of the test link") {
                targetElement {
                    locator = "[name='link-name']"
                }

                expectedValue("Test")
                check(SyntheticsCheckType.EQUALS)
            }
            elementAttributeAssertion("Check that href of the test link is not empty") {
                targetElement {
                    locator = "[name='link-name']"
                }
                attribute("href")
                expectedValue("")
                check(SyntheticsCheckType.NOT_IS_EMPTY)
            }
            navigateStep("Navigate to test page by URL") {
                navigationUrl("https://synthetic-test.personio.de/test")
            }
            customJavascriptAssertion("Check custom JS") {
                code("return true;")
            }
            refreshStep("Refresh test page")
            waitStep("Wait for a few seconds on the page") {
                waitingTime(10.seconds)
            }
            scrollStep("Scroll to test element") {
                targetElement {
                    locator = "[name='test-element']"
                }
                horizontalScroll(1)
                verticalScroll(1)
            }
            hoverStep("Hover over test element") {
                targetElement {
                    locator = "[name='test-element']"
                }
            }
            pressKeyStep("Press Ctrl+Shift+Backspace keys") {
                key(Key.BACKSPACE)
                modifiers(Modifier.CONTROL, Modifier.SHIFT)
            }
            extractFromJavascriptStep("Extract from js") {
                allowFailure(true)
                code("return 'abc'")
                variable("JS_STEP_VAR")
            }
            apiStep("API Step") {
                requestBody("{\"userName\": \"test\"}")
                requestHeaders(mutableMapOf("content-type" to "application/json"))
                method(HTTPMethod.POST)
                url("/login")
                assertion {
                    target = 200
                    type = SyntheticsAssertionType.STATUS_CODE
                    operator = SyntheticsAssertionOperator.IS
                }
                assertion {
                    type = SyntheticsAssertionType.HEADER
                    property = "set-cookie"
                    operator = SyntheticsAssertionOperator.CONTAINS
                    target = "session="
                }
                extractHeaderValue(
                    name = "SESSION",
                    field = "set-cookie",
                )
            }
        }
    }
}
