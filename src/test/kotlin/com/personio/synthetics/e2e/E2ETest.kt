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
            .message("{{#is_alert}} @slack-not_gitlab_qa Test Failed {{/is_alert}}")
            .addTagsItem("synthetics-api")
            .setUrl("https://denys-demo.personio.de")

        syntheticsTest.addGlobalVariable("PASSWORD_PROD_TEST")
        syntheticsTest.inputTextStep()
            .name("Enter username")
            .targetElement("#email")
            .text("qa-engineering@personio.de")
        syntheticsTest.inputTextStep()
            .name("Enter password")
            .targetElement("#password")
            .text("{{ PASSWORD_PROD_TEST }}")
        syntheticsTest.clickStep()
            .name("Click login button")
            .targetElement("button[type=\"submit\"]")
        syntheticsTest.clickStep()
            .name("Navigate to settings page")
            .targetElement("[data-test-id=\"navsidebar-settings\"]")
        syntheticsTest.assertionStep(AssertionType.ELEMENT_PRESENT)
            .name("Check if cost centers link is present")
            .targetElement("[data-test-id=\"settings-listitem-cost-centers\"]")
        syntheticsTest.createBrowserTest(TESTNAME)
    }
}
