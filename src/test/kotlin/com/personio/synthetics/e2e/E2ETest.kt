package com.personio.synthetics.e2e

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsAssertionOperator
import com.datadog.api.v1.client.model.SyntheticsAssertionType
import com.datadog.api.v1.client.model.SyntheticsCheckType
import com.datadog.api.v1.client.model.SyntheticsDeviceID
import com.personio.synthetics.client.syntheticBrowserTest
import com.personio.synthetics.config.addGlobalVariable
import com.personio.synthetics.config.baseUrl
import com.personio.synthetics.config.browserAndDevice
import com.personio.synthetics.config.minFailureDuration
import com.personio.synthetics.config.minLocationFailed
import com.personio.synthetics.config.monitorName
import com.personio.synthetics.config.monitorPriority
import com.personio.synthetics.config.publicLocation
import com.personio.synthetics.config.renotifyInterval
import com.personio.synthetics.config.retry
import com.personio.synthetics.config.testFrequency
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class E2ETest {
    @Test
    fun `create synthetic test`() {
        syntheticBrowserTest("[Test] Synthetic-Test-As-Code") {
            message = "{{#is_alert}} @slack-not_gitlab_qa Test Failed {{/is_alert}}"
            tags(listOf("env:qa", "synthetics-api"))
            baseUrl(URL("https://denys-demo.personio.de"))
            browserAndDevice(SyntheticsDeviceID.CHROME_MOBILE_SMALL, SyntheticsDeviceID.FIREFOX_LAPTOP_LARGE)
            publicLocation(Location.IRELAND_AWS, Location.N_CALIFORNIA_AWS, Location.MUMBAI_AWS)
            testFrequency(6.minutes)
            retry(2, 600.milliseconds)
            minFailureDuration(120.minutes)
            minLocationFailed(2)
            monitorName("Test Monitor Name")
            renotifyInterval(RenotifyInterval.HOURS_2)
            monitorPriority(MonitorPriority.P3_MEDIUM)
            addGlobalVariable("PASSWORD_PROD_TEST")
            inputTextStep("Enter username") {
                targetElement {
                    locator = "#email"
                }
                text("abc@personio.de")
            }
            inputTextStep("Enter password") {
                targetElement {
                    locator = "#password"
                }
                text("{{ PASSWORD_PROD_TEST }}")
            }
            clickStep("Click login button") {
                targetElement {
                    locator = "button[type='submit']"
                }
            }
            currentUrlAssertion("Check current URL") {
                expectedValue("https://denys-demo.personio.de")
                check(SyntheticsCheckType.CONTAINS)
            }
            pageContainsTextAssertion("Check that company overview heading is present on the page") {
                expectedValue("CS Demo GmbH im Überblick")
            }
            pageNotContainsTextAssertion("Check text is not present on the page") {
                expectedValue("string that should not be present")
            }
            clickStep("Navigate to settings page") {
                targetElement {
                    locator = "[data-test-id='navsidebar-settings']"
                }
            }
            elementPresentAssertion("Check if cost centers link is present") {
                targetElement {
                    locator = "[data-test-id='settings-listitem-cost-centers']"
                }
            }
            elementContentAssertion("Check text of the cost centers link") {
                targetElement {
                    locator = "[data-test-id='settings-listitem-cost-centers']"
                }

                expectedValue("Kostenstellen")
                check(SyntheticsCheckType.EQUALS)
            }
            elementAttributeAssertion("Check that href of the cost centers link is not empty") {
                targetElement {
                    locator = "[data-test-id='settings-listitem-cost-centers']"
                }
                attribute("href")
                expectedValue("")
                check(SyntheticsCheckType.NOT_IS_EMPTY)
            }
            navigateStep("Navigate to Dashboard page by URL") {
                navigationUrl("https://denys-demo.personio.de/my-desk")
            }
            customJavascriptAssertion("Check custom JS") {
                code("return true;")
            }
            refreshStep("Refresh Dashboard page")
            waitStep("Wait for a few seconds on the page") {
                waitingTime(10.seconds)
            }
            scrollStep("Scroll to Employees Joining widget") {
                targetElement {
                    locator = "[data-action-name='dbv2-kpi-employees-joining']"
                }
                horizontalScroll(1)
                verticalScroll(1)
            }
            hoverStep("Hover over Employees Joining widget") {
                targetElement {
                    locator = "[data-action-name='dbv2-kpi-employees-joining']"
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
                requestBody("{\"email\": \"abc\"}")
                requestHeaders(mutableMapOf("content-type" to "application/json"))
                method(HTTPMethod.POST)
                url("/login/wizard")
                assertion {
                    target = 302
                    type = SyntheticsAssertionType.STATUS_CODE
                    operator = SyntheticsAssertionOperator.IS
                }
                assertion {
                    type = SyntheticsAssertionType.HEADER
                    property = "set-cookie"
                    operator = SyntheticsAssertionOperator.CONTAINS
                    target = "personio_session="
                }
                extractHeaderValue(
                    name = "PERSONIO_SESSION",
                    field = "set-cookie",
                )
            }
        }
    }
}
