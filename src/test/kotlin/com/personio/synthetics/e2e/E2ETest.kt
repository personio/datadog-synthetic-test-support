package com.personio.synthetics.e2e

import com.datadog.api.client.v1.model.SyntheticsAssertionOperator
import com.datadog.api.client.v1.model.SyntheticsAssertionType
import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.datadog.api.client.v1.model.SyntheticsTestPauseStatus
import com.personio.synthetics.client.syntheticBrowserTest
import com.personio.synthetics.config.advancedScheduling
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
import com.personio.synthetics.config.uuidPatternVariable
import com.personio.synthetics.model.actions.Key
import com.personio.synthetics.model.actions.Modifier
import com.personio.synthetics.model.assertion.FileNameCheckType
import com.personio.synthetics.model.assertion.FileSizeCheckType
import com.personio.synthetics.model.config.Location
import com.personio.synthetics.model.config.MonitorPriority
import com.personio.synthetics.model.config.RenotifyInterval
import com.personio.synthetics.model.config.SyntheticsDeviceID
import com.personio.synthetics.model.config.Timeframe
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
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class E2ETest {
    @Test
    fun `create synthetic test`() {
        syntheticBrowserTest("[Test] Synthetic-Test-As-Code") {
            status(SyntheticsTestPauseStatus.PAUSED)
            alertMessage("Test Failed", "@slack-test_slack_channel")
            recoveryMessage("Test recovered")
            tags(listOf("env:qa", "synthetics-api"))
            baseUrl(URL("https://synthetic-test.personio.de"))
            browserAndDevice(SyntheticsDeviceID.CHROME_MOBILE_SMALL, SyntheticsDeviceID.FIREFOX_LAPTOP_LARGE)
            publicLocation(Location.IRELAND_AWS, Location.N_CALIFORNIA_AWS, Location.MUMBAI_AWS)
            testFrequency(6.minutes)
            advancedScheduling(
                Timeframe(
                    from = LocalTime.of(0, 1),
                    to = LocalTime.of(23, 59),
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.FRIDAY,
                ),
                timezone = ZoneId.of("Europe/Dublin"),
            )
            retry(2, 600.milliseconds)
            minFailureDuration(120.minutes)
            minLocationFailed(2)
            monitorName("Test Monitor Name")
            renotifyInterval(RenotifyInterval.HOURS_2)
            monitorPriority(MonitorPriority.P3_MEDIUM)
            useGlobalVariable("TEST_PASSWORD")
            textVariable("TEXT_VARIABLE", "test")
            numericPatternVariable(
                name = "NUMERIC_PATTERN",
                characterLength = 4,
                prefix = "test",
            )
            alphabeticPatternVariable("ALPHABETIC_PATTERN", 5)
            alphanumericPatternVariable("ALPHANUMERIC_PATTERN", 6)
            datePatternVariable(
                name = "DATE_PATTERN",
                duration = (-1).days,
                format = "MM-DD-YYYY",
            )
            timestampPatternVariable(
                name = "TIMESTAMP_PATTERN",
                duration = 10.seconds,
            )
            uuidPatternVariable(
                name = "UUID_PATTERN",
            )
            inputTextStep(
                stepName = "Enter username",
                targetElement = TargetElement("[name='email']"),
                text = "test@personio.de",
            )
            inputTextStep(
                stepName = "Enter password",
                targetElement = TargetElement("[name='password']"),
                text = "{{ TEST_PASSWORD }}",
            )
            clickStep(
                stepName = "Click login button",
                targetElement = TargetElement("[name='login']"),
            ) {
                waitBeforeDeclaringStepAsFailed(75.seconds)
            }
            currentUrlAssertion(
                stepName = "Check current URL",
                expectedContent = "https://synthetic-test.personio.de",
                check = SyntheticsCheckType.CONTAINS,
            )
            pageContainsTextAssertion(
                stepName = "Check text is present on the page",
                expectedText = "string that should be present",
            ) {
                waitBeforeDeclaringStepAsFailed(30.seconds)
                continueWithTestIfStepFails(true)
            }
            pageNotContainsTextAssertion(
                stepName = "Check text is not present on the page",
                text = "string that should not be present",
            )
            clickStep(
                stepName = "Navigate to test page",
                targetElement = TargetElement("[name='test-page']"),
            )
            elementPresentAssertion(
                stepName = "Check if test link is present",
                targetElement = TargetElement("[name='link-name']"),
            )
            elementContentAssertion(
                stepName = "Check text of the test link",
                targetElement = TargetElement("[value='link-text']"),
                check = SyntheticsCheckType.EQUALS,
                expectedContent = "Test",
            )
            elementAttributeAssertion(
                stepName = "Check that href of the test link is not empty",
                targetElement = TargetElement("[href='link-href']"),
                attribute = "href",
                check = SyntheticsCheckType.NOT_IS_EMPTY,
            )
            extractTextFromElementStep(
                stepName = "Extract text from element",
                variableName = "EXTRACT_TEXT",
                targetElement = TargetElement("[value='test-extract']"),
            )
            navigateStep(
                stepName = "Navigate to test page by URL",
                url = "https://synthetic-test.personio.de/test",
            )
            customJavascriptAssertion(
                stepName = "Check custom JS",
                code = "return true;",
            )
            uploadFileStep(
                stepName = "Upload file",
                element = TargetElement("[name='upload-file']"),
                uploadFile = File.createTempFile("upload", ".pdf").apply { writeText("Test") },
            )
            downloadedFileAssertion("Check downloaded file assertion step") {
                nameCheck(FileNameCheckType.NOT_IS_EMPTY)
                sizeCheck(FileSizeCheckType.GREATER, 1)
                expectedMd5("123")
            }
            refreshStep("Refresh test page")
            waitStep(
                stepName = "Wait for a few seconds on the page",
                waitingTime = 10.seconds,
            )
            scrollStep("Scroll to test element") {
                targetElement(locator = "[name='test-element']")
            }
            scrollStep("Scroll to test element with x,y coordinates") {
                horizontalScroll(10)
                verticalScroll(10)
            }
            hoverStep(
                stepName = "Hover over test element",
                targetElement = TargetElement("[name='test-element']"),
            )
            pressKeyStep(
                stepName = "Press Ctrl+Shift+Backspace keys",
                key = Key.BACKSPACE,
            ) { modifiers(Modifier.CONTROL, Modifier.SHIFT) }
            extractFromJavascriptStep(
                stepName = "Extract from js",
                code = "return 'abc'",
                variableName = "JS_STEP_VAR",
            )
            apiStep("API Step", "POST") {
                requestBody("{\"userName\": \"test\"}")
                requestHeaders(mutableMapOf("content-type" to "application/json"))
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
                waitBeforeDeclaringStepAsFailed(90.seconds)
                continueWithTestIfStepFails()
            }
        }
    }
}
