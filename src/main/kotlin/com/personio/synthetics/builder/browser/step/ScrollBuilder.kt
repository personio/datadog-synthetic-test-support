package com.personio.synthetics.builder.browser.step

class ScrollBuilder {
    private var x: Int = 0
    private var y: Int = 0

    fun build(): Pair<Int, Int> {
        return x to y
    }

    fun horizontal(value: Int) {
        x += value
    }

    fun vertical(value: Int) {
        y += value
    }
}
