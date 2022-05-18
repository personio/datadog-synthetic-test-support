package com.personio.synthetics.model.actions

internal data class WaitParams(val value: Int = 1) {
    init {
        require(value in 1..300) {
            "Waiting time must be no less than 1 and no bigger than 300 seconds"
        }
    }
}
