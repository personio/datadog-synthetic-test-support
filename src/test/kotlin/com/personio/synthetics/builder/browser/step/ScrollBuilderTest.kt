package com.personio.synthetics.builder.browser.step

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScrollBuilderTest {
    private val sut = ScrollBuilder()

    @Test
    fun `up increases y`() {
        sut.up(10)

        assertEquals(10, sut.build().second)
    }

    @Test
    fun `down decreases y`() {
        sut.down(10)

        assertEquals(-10, sut.build().second)
    }

    @Test
    fun `right increases x`() {
        sut.right(10)

        assertEquals(10, sut.build().first)
    }

    @Test
    fun `left decreases x`() {
        sut.left(10)

        assertEquals(-10, sut.build().first)
    }
}
