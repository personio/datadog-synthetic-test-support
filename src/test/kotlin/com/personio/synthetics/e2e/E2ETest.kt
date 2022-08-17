package com.personio.synthetics.e2e

import com.datadog.api.client.v1.model.HTTPMethod
import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionType
import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsDeviceID
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.personio.synthetics.client.syntheticBrowserTest
import com.personio.synthetics.config.alertMessage
import com.personio.synthetics.config.alphabeticPatternVariable
import com.personio.synthetics.config.alphanumericPatternVariable
import com.personio.synthetics.config.baseUrl
import com.personio.synthetics.config.browserAndDevice
import com.personio.synthetics.config.datePatternVariable
import com.personio.synthetics.config.minFailureDuration
import com.personio.synthetics.config.minLocationFailed
import com.personio.synthetics.config.monitorName
import com.personio.synthetics.config.monitorPriority
import com.personio.synthetics.config.numericPatternVariable
import com.personio.synthetics.config.publicLocation
import com.personio.synthetics.config.recoveryMessage
import com.personio.synthetics.config.renotifyInterval
import com.personio.synthetics.config.retry
import com.personio.synthetics.config.testFrequency
import com.personio.synthetics.config.textVariable
import com.personio.synthetics.config.timestampPatternVariable
import com.personio.synthetics.config.useGlobalVariable
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.assertion.FileNameCheckType
import com.personio.synthetics.model.assertion.FileSizeCheckType
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import com.personio.synthetics.step.api.apiStep
import com.personio.synthetics.step.assertion.currentUrlAssertion
import com.personio.synthetics.step.assertion.customJavascriptAssertion
import com.personio.synthetics.step.assertion.downloadedFileAssertion
import com.personio.synthetics.step.assertion.elementAttributeAssertion
import com.personio.synthetics.step.assertion.elementContentAssertion
import com.personio.synthetics.step.assertion.elementPresentAssertion
import com.personio.synthetics.step.assertion.pageContainsTextAssertion
import com.personio.synthetics.step.assertion.pageNotContainsTextAssertion
import com.personio.synthetics.step.continueWithTestIfStepFails
import com.personio.synthetics.step.extract.extractFromJavascriptStep
import com.personio.synthetics.step.extract.extractTextFromElementStep
import com.personio.synthetics.step.file.uploadFileStep
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.hoverStep
import com.personio.synthetics.step.ui.inputTextStep
import com.personio.synthetics.step.ui.model.TargetElement
import com.personio.synthetics.step.ui.navigateStep
import com.personio.synthetics.step.ui.pressKeyStep
import com.personio.synthetics.step.ui.refreshStep
import com.personio.synthetics.step.ui.scrollStep
import com.personio.synthetics.step.ui.waitStep
import com.personio.synthetics.step.waitBeforeDeclaringStepAsFailed
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class E2ETest {
    @Test
    fun `create synthetic test`() {
        syntheticBrowserTest("[Test] Synthetic-Test-As-Code") {
            status(SyntheticsTestPauseStatus.PAUSED)
            alertMessage("Test Failed", "@opsgenie-o11y-integration team=TS")
            recoveryMessage("Test recovered")
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
            useGlobalVariable("PASSWORD_PROD_TEST")
            textVariable("TEXT_VARIABLE", "test")
            numericPatternVariable(
                name = "NUMERIC_PATTERN",
                characterLength = 4,
                prefix = "test"
            )
            alphabeticPatternVariable("ALPHABETIC_PATTERN", 5)
            alphanumericPatternVariable("ALPHANUMERIC_PATTERN", 6)
            datePatternVariable(
                name = "DATE_PATTERN",
                duration = (-1).days,
                format = "MM-DD-YYYY"
            )
            timestampPatternVariable(
                name = "TIMESTAMP_PATTERN",
                duration = 10.seconds
            )
            inputTextStep(
                stepName = "Enter username",
                targetElement = TargetElement("#email"),
                text = "abc@personio.de"
            )
            inputTextStep(
                stepName = "Enter password",
                targetElement = TargetElement("#password"),
                text = "{{ PASSWORD_PROD_TEST }}"
            )
            clickStep(
                stepName = "Click login button",
                targetElement = TargetElement("button[type='submit']")
            ) {
                waitBeforeDeclaringStepAsFailed(75.seconds)
            }
            currentUrlAssertion(
                stepName = "Check current URL",
                expectedContent = "https://denys-demo.personio.de",
                check = SyntheticsCheckType.CONTAINS
            )
            pageContainsTextAssertion(
                stepName = "Check that company overview heading is present on the page",
                expectedText = "CS Demo GmbH im Überblick"
            ) {
                waitBeforeDeclaringStepAsFailed(30.seconds)
                continueWithTestIfStepFails(true)
            }
            pageNotContainsTextAssertion(
                stepName = "Check text is not present on the page",
                text = "string that should not be present"
            )
            clickStep(
                stepName = "Navigate to settings page",
                targetElement = TargetElement("[data-test-id='navsidebar-settings']")
            )
            elementPresentAssertion(
                stepName = "Check if cost centers link is present",
                targetElement = TargetElement("[data-test-id='settings-listitem-cost-centers']")
            )
            elementContentAssertion(
                stepName = "Check text of the cost centers link",
                targetElement = TargetElement("[data-test-id='settings-listitem-cost-centers']"),
                check = SyntheticsCheckType.EQUALS,
                expectedContent = "Kostenstellen"
            )
            elementAttributeAssertion(
                stepName = "Check that href of the cost centers link is not empty",
                targetElement = TargetElement("[data-test-id='settings-listitem-cost-centers']"),
                attribute = "href",
                check = SyntheticsCheckType.NOT_IS_EMPTY
            )
            extractTextFromElementStep(
                stepName = "Extract text from element",
                variableName = "EXTRACT_TEXT",
                targetElement = TargetElement("[data-test-id='settings-listitem-cost-centers']")
            )
            navigateStep(
                stepName = "Navigate to Dashboard page by URL",
                url = "https://denys-demo.personio.de/my-desk"
            )
            customJavascriptAssertion(
                stepName = "Check custom JS",
                code = "return true;"
            )
            uploadFileStep(
                stepName = "Upload file",
                element = TargetElement("[data-test-id='upload-file']"),
                uploadFile = File.createTempFile("upload", ".pdf").apply { writeText("Test") }
            )
            downloadedFileAssertion("Check downloaded file assertion step") {
                nameCheck(FileNameCheckType.NOT_IS_EMPTY)
                sizeCheck(FileSizeCheckType.GREATER, 1)
                expectedMd5("123")
            }
            refreshStep("Refresh Dashboard page")
            waitStep(
                stepName = "Wait for a few seconds on the page",
                waitingTime = 10.seconds
            )
            scrollStep("Scroll to Employees Joining widget") {
                targetElement(locator = "[data-action-name='dbv2-kpi-employees-joining']")
            }
            scrollStep("Scroll to Employees Joining widget with x,y coordinates") {
                horizontalScroll(10)
                verticalScroll(10)
            }
            hoverStep(
                stepName = "Hover over Employees Joining widget",
                targetElement = TargetElement("[data-action-name='dbv2-kpi-employees-joining']")
            )
            pressKeyStep(
                stepName = "Press Ctrl+Shift+Backspace keys",
                key = Key.BACKSPACE
            ) { modifiers(Modifier.CONTROL, Modifier.SHIFT) }
            extractFromJavascriptStep(
                stepName = "Extract from js",
                code = "return 'abc'",
                variableName = "JS_STEP_VAR"
            )
            apiStep("API Step", HTTPMethod.POST) {
                requestBody("{\"email\": \"abc\"}")
                requestHeaders(mutableMapOf("content-type" to "application/json"))
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
                waitBeforeDeclaringStepAsFailed(90.seconds)
                continueWithTestIfStepFails()
            }
        }
    }
}
