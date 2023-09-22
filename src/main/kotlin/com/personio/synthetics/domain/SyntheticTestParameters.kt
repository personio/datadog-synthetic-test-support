package com.personio.synthetics.domain

data class SyntheticTestParameters(
    var message: String,
    var locations: List<String>,
    var tags: MutableList<String>
)
