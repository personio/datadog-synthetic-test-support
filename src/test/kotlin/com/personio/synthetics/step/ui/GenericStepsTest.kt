package com.personio.synthetics.step.ui

import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.actions.SpecialActionsParams
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.assertion.AssertionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class GenericStepsTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `targetElement adds Userlocator to the params object`() {
        browserTest
            .inputTextStep()
            .targetElement("//button", LocatorType.XPATH)
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("xpath", params.element!!.userLocator.values?.get(0)?.type)
        assertEquals("//button", params.element.userLocator.values?.get(0)?.value)
    }

    @Test
    fun `targetElement without locator type adds Userlocator with default locator type as css`() {
        browserTest
            .inputTextStep()
            .targetElement("[name]=\"test\"")
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("css", params.element!!.userLocator.values?.get(0)?.type)
    }

    @Test
    fun `targetElement adds params as ActionParams if clickStep is added`() {
        browserTest
            .clickStep()
            .targetElement("[name]=\"test\"")

        assertInstanceOf(ActionsParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as AssertionParams if assertionStep is added`() {
        browserTest
            .assertionStep(AssertionType.ELEMENT_PRESENT)
            .targetElement("[name]=\"test\"")

        assertInstanceOf(AssertionParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as SpecialActionsParams if scrollStep is added`() {
        browserTest
            .scrollStep()
            .targetElement("[name]=\"test\"")

        assertInstanceOf(SpecialActionsParams::class.java, browserTest.steps?.get(0)?.params)
    }

    @Test
    fun `targetElement adds params as SpecialActionsParams if hoverStep is added`() {
        browserTest
            .hoverStep()
            .targetElement("[name]=\"test\"")

        assertInstanceOf(SpecialActionsParams::class.java, browserTest.steps?.get(0)?.params)
    }
}
