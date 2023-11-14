package com.personio.synthetics.builder.browser.step

class ScrollBuilder {
    private var x: Int = 0
    private var y: Int = 0

    fun build(): Pair<Int, Int> {
        return x to y
    }

    fun up(value: Int) {
        y += value
    }

    fun down(value: Int) {
        up(-value)
    }

    fun left(value: Int) {
        right(-value)
    }

    fun right(value: Int) {
        x += value
    }
}
