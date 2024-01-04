package com.personio.synthetics.step.ui.model

import com.personio.synthetics.model.actions.LocatorType
import com.personio.synthetics.model.element.Element
import com.personio.synthetics.model.element.ElementForSpecialActions
import com.personio.synthetics.model.element.UserLocator
import com.personio.synthetics.model.element.Value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TargetElementTest {
    @Test
    fun `getElementObject returns the element object with the passed locator value and default locator type`() {
        val locator = "locator"
        val expectedElement = Element(userLocator = userLocator(locator))

        val actualElement = TargetElement(locator).getElementObject()

        assertEquals(expectedElement, actualElement)
    }

    @Test
    fun `getElementObject returns the element object with the passed locator value and locator type`() {
        val locator = "locator"
        val locatorType = LocatorType.XPATH
        val expectedElement = Element(userLocator = userLocator(locator, locatorType))

        val actualElement = TargetElement(locator, locatorType).getElementObject()

        assertEquals(expectedElement, actualElement)
    }

    @Test
    fun `getSpecialActionsElementObject returns the special actions element object with passed locator value and default locator type`() {
        val locator = "#locator"
        val expectedElement = ElementForSpecialActions(userLocator = userLocator(locator))

        val actualElement = TargetElement(locator).getSpecialActionsElementObject()

        assertEquals(expectedElement, actualElement)
    }

    @Test
    fun `getSpecialActionsElementObject returns the special actions element object with passed locator value and locator type`() {
        val locator = "#locator"
        val locatorType = LocatorType.XPATH
        val expectedElement = ElementForSpecialActions(userLocator = userLocator(locator, locatorType))

        val actualElement = TargetElement(locator, locatorType).getSpecialActionsElementObject()

        assertEquals(expectedElement, actualElement)
    }

    private fun userLocator(
        locator: String,
        locatorType: LocatorType = LocatorType.CSS,
    ): UserLocator = UserLocator(values = listOf(Value(locatorType.value, locator)))
}
