package com.personio.synthetics.step

import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.extract.ExtractParams
import com.personio.synthetics.step.assertion.customJavascriptAssertion
import com.personio.synthetics.step.assertion.elementPresentAssertion
import com.personio.synthetics.step.extract.extractFromJavascriptStep
import com.personio.synthetics.step.extract.extractTextFromElementStep
import com.personio.synthetics.step.ui.clickStep
import com.personio.synthetics.step.ui.hoverStep
import com.personio.synthetics.step.ui.inputTextStep
import com.personio.synthetics.step.ui.scrollStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class StepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `targetElement adds Userlocator to the params object`() {
        browserTest
            .inputTextStep("Step") {
                text("SampleText")
                targetElement {
                    locator = "//button"
                    locatorType = LocatorType.XPATH
                }
            }
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("xpath", params.element!!.userLocator.values?.get(0)?.type)
        assertEquals("//button", params.element.userLocator.values?.get(0)?.value)
    }

    @Test
    fun `targetElement without locator type adds Userlocator with default locator type as css`() {
        browserTest
            .inputTextStep("Step") {
                text("SampleText")
                targetElement {
                    locator = "[name]='test'"
                }
            }
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("css", params.element!!.userLocator.values?.get(0)?.type)
    }

    @Test
    fun `targetElement adds params as ActionParams if clickStep is added`() {
        browserTest
            .clickStep("Step") {
                targetElement {
                    locator = "[name]='test'"
                }
            }

        assertInstanceOf(ActionsParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as AssertionParams if elementPresentAssertion is added`() {
        browserTest
            .elementPresentAssertion("Step") {
                targetElement {
                    locator = "[name]='test'"
                }
            }

        assertInstanceOf(AssertionParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement and x,y coordinates adds params as SpecialActionsParams if scrollStep is added`() {
        browserTest
            .scrollStep("Step") {
                targetElement {
                    locator = "[name]='test'"
                }
                horizontalScroll(1)
                verticalScroll(1)
            }

        assertInstanceOf(SpecialActionsParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as SpecialActionsParams if hoverStep is added`() {
        browserTest
            .hoverStep("Step") {
                targetElement {
                    locator = "[name]='test'"
                }
            }

        assertInstanceOf(SpecialActionsParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as ExtractParams if extractFromJavascriptStep is added`() {
        browserTest
            .extractFromJavascriptStep("Step") {
                targetElement {
                    locator = "[name]='test'"
                }
                code("return 1")
                variable("EXTRACT_JS_VAR")
            }

        assertInstanceOf(ExtractParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as ExtractParams if extractTextFromElementStep is added`() {
        browserTest
            .extractTextFromElementStep("Step") {
                targetElement {
                    locator = "[name]='test'"
                }
                variable("EXTRACT_TEST_VAR")
            }

        assertInstanceOf(ExtractParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `code adds params as JSParams if extractFromJavascriptStep is added`() {
        browserTest
            .extractFromJavascriptStep("Step") {
                code("return 1")
                variable("EXTRACT_JS_VAR")
            }

        assertInstanceOf(ExtractParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `code adds params as AssertionParams if extractFromJavascriptStep is added`() {
        browserTest
            .customJavascriptAssertion("Step") {
                code("return true;")
            }

        assertInstanceOf(AssertionParams::class.java, browserTest.steps?.get(0)?.params)
    }
}
