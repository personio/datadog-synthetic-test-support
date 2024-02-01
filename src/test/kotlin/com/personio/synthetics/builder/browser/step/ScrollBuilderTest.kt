package com.personio.synthetics.builder.browser.step

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScrollBuilderTest {
    private val sut = ScrollBuilder()

    @Test
    fun `vertical adjusts y`() {
        sut.vertical(10)

        assertEquals(10, sut.build().second)
    }

    @Test
    fun `horizontal increases x`() {
        sut.horizontal(10)

        assertEquals(10, sut.build().first)
    }
}
