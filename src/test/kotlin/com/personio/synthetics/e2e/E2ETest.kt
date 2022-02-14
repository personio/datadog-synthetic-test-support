package com.personio.synthetics.e2e

import com.personio.synthetics.BrowserTest
import com.personio.synthetics.annotation.SyntheticUITest
import com.personio.synthetics.config.addGlobalVariable
import com.personio.synthetics.config.setUrl
import com.personio.synthetics.model.assertion.AssertionType
import com.personio.synthetics.step.ui.assertionStep
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.inputTextStep
import com.personio.synthetics.step.ui.targetElement
import com.personio.synthetics.step.ui.text
import org.junit.jupiter.api.Test

@SyntheticUITest
class E2ETest(private val syntheticsTest: BrowserTest) {
    private val TESTNAME = "[Test] Synthetic-Test-As-Code"

    @Test
    fun `create synthetic test`() {
        syntheticsTest
            .name(TESTNAME)
            .message("{{#is_alert}} @slack-test_slack_channel Test Failed {{/is_alert}}")
            .addTagsItem("synthetics-api")
            .setUrl("https://synthetic-test.personio.de")

        syntheticsTest.addGlobalVariable("TEST_PASSWORD")
        syntheticsTest.inputTextStep()
            .name("Enter username")
            .targetElement("[name='email']")
            .text("test@personio.de")
        syntheticsTest.inputTextStep()
            .name("Enter password")
            .targetElement("[name='password']")
            .text("{{ TEST_PASSWORD }}")
        syntheticsTest.clickStep()
            .name("Click login button")
            .targetElement("[name='login']")
        syntheticsTest.clickStep()
            .name("Navigate to test page")
            .targetElement("[name='test-page']")
        syntheticsTest.assertionStep(AssertionType.ELEMENT_PRESENT)
            .name("Check if test link is present")
            .targetElement("[name='link-name']")
        syntheticsTest.createBrowserTest(TESTNAME)
    }
}
