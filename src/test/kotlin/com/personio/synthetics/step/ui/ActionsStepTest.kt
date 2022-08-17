package com.personio.synthetics.step.ui

import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.baseUrl
import com.personio.synthetics.config.fromVariable
import com.personio.synthetics.model.actions.ActionsParams
import com.personio.synthetics.step.ui.model.TargetElement
import com.personio.synthetics.step.waitBeforeDeclaringStepAsFailed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.net.URL
import kotlin.time.Duration.Companion.seconds

internal class ActionsStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `inputTextStep adds step with type text and params of type ActionsParams`() {
        browserTest.inputTextStep(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            text = "text"
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.TYPE_TEXT, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `inputTextStep adds the passed text to the params object`() {
        val text = "Sample Text"
        browserTest.inputTextStep(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            text = text
        )
        assertEquals(text, (browserTest.steps?.get(0)?.params as ActionsParams).value)
    }

    @Test
    fun `inputTextStep adds the passed target element to the params object`() {
        val targetElement = TargetElement("#locator")
        browserTest.inputTextStep(
            stepName = "Step",
            targetElement = targetElement,
            text = "text"
        )

        assertEquals(targetElement.getElementObject(), (browserTest.steps?.get(0)?.params as ActionsParams).element)
    }

    @Test
    fun `inputTextStep sets the passed datadog variable as the text value`() {
        val variableName = "TEXT_VALUE"
        browserTest.inputTextStep(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            text = fromVariable(variableName)
        )
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals("{{ $variableName }}", params.value)
    }

    @Test
    fun `inputTextStep accepts additional configuration changes to the test step`() {
        browserTest.inputTextStep(
            stepName = "Step",
            targetElement = TargetElement("#locator"),
            text = "text"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `clickStep adds step with type click and params of type ActionsParams`() {
        browserTest.clickStep(
            stepName = "Step",
            targetElement = TargetElement("#locatorId")
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.CLICK, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `clickStep with target element add new step to browser test object`() {
        val locator = "#locator"
        browserTest.clickStep(
            stepName = "Step",
            targetElement = TargetElement(locator)
        )
        val params = browserTest.steps?.get(0)?.params as ActionsParams

        assertEquals(locator, params.element?.userLocator?.values?.get(0)?.value)
    }

    @Test
    fun `clickStep accepts additional configuration changes to the test step`() {
        browserTest.clickStep(
            stepName = "Step",
            targetElement = TargetElement("#locator")
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `navigateStep adds step with type go to url and params of type ActionsParams`() {
        browserTest
            .navigateStep(
                stepName = "Step",
                url = "https://synthetic-test.personio.de"
            )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.GO_TO_URL, step?.type)
        assertInstanceOf(ActionsParams::class.java, step?.params)
    }

    @Test
    fun `navigateStep adds passed navigationUrl to the params object`() {
        val url = "https://synthetic-test.personio.de"
        browserTest
            .navigateStep(
                stepName = "Step",
                url = url
            )

        assertEquals(url, (browserTest.steps?.get(0)?.params as ActionsParams).value)
    }

    @Test
    fun `navigateStep appends the passed navigationUrl location to the base url of the test`() {
        val baseUrl = "https://synthetic-test.personio.de"
        val url = "/test"
        val expectedUrl = baseUrl + url

        browserTest
            .baseUrl(URL(baseUrl))
            .navigateStep(
                stepName = "Step",
                url = url
            )

        assertEquals(expectedUrl, (browserTest.steps?.get(0)?.params as ActionsParams).value)
    }

    @Test
    fun `navigateStep sets the passed datadog variable as the url`() {
        val variable = "NAVIGATION_URL"
        browserTest
            .navigateStep(
                stepName = "Step",
                url = fromVariable(variable)
            )

        assertEquals("{{ $variable }}", (browserTest.steps?.get(0)?.params as ActionsParams).value)
    }

    @Test
    fun `navigateStep accepts the datadog variable in the url`() {
        val baseUrl = "https://synthetic-test.personio.de"
        val locationVariable = "LOCATION"
        browserTest
            .navigateStep(
                stepName = "Step",
                url = "$baseUrl/${fromVariable(locationVariable)}"
            )

        assertEquals("$baseUrl/{{ $locationVariable }}", (browserTest.steps?.get(0)?.params as ActionsParams).value)
    }

    @Test
    fun `navigateStep accepts additional configuration changes to the test step`() {
        browserTest
            .navigateStep(
                stepName = "Step",
                url = "https://synthetic-test.personio.de"
            ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }
}
