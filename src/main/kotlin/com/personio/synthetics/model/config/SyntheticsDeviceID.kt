package com.personio.synthetics.model.config

enum class SyntheticsDeviceID(
    val value: String,
) {
    LAPTOP_LARGE("laptop_large"),
    TABLET("tablet"),
    MOBILE_SMALL("mobile_small"),

    CHROME_LAPTOP_LARGE("chrome.laptop_large"),
    CHROME_TABLET("chrome.tablet"),
    CHROME_MOBILE_SMALL("chrome.mobile_small"),

    FIREFOX_LAPTOP_LARGE("firefox.laptop_large"),
    FIREFOX_TABLET("firefox.tablet"),
    FIREFOX_MOBILE_SMALL("firefox.mobile_small"),

    EDGE_LAPTOP_LARGE("edge.laptop_large"),
    EDGE_TABLET("edge.tablet"),
    EDGE_MOBILE_SMALL("edge.mobile_small"),
}
