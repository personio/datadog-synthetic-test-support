package com.personio.synthetics.model.actions

import com.personio.synthetics.model.Params

internal data class WaitParams(val value: Int = 1) : Params() {
    init {
        require(value in 1..300) {
            "Waiting time must be no less than 1 and no bigger than 300 seconds."
        }
    }
}
