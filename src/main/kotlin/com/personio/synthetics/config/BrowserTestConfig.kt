package com.personio.synthetics.config

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsBrowserTest
import com.datadog.api.v1.client.model.SyntheticsBrowserVariable
import com.datadog.api.v1.client.model.SyntheticsBrowserVariableType
import com.datadog.api.v1.client.model.SyntheticsTestRequest
import com.personio.synthetics.client.BrowserTest
import java.net.URL

/**
 * Sets the base url for the synthetic browser test
 * @param url the base url for the test
 * @return SyntheticsBrowserTest object with url set
 */
fun SyntheticsBrowserTest.setUrl(url: URL) = apply {
    config
        ?.request(
            SyntheticsTestRequest()
                .method(HTTPMethod.GET)
                .url(url.toString())
        )
}

/**
 * Creates a local variable and add it to the synthetic browser test
 * @param name name of the variable
 * @param type type of the variable
 * @param pattern pattern of the variables (Optional parameter). Allowed patterns are
 * {{ numeric(8) }}, {{ alphabetic(15) }}, {{ alphanumeric(15) }}, {{ date(0d, MM-DD-YYYY) }}, {{ timestamp(0, s) }}
 * @return SyntheticsBrowserTest object with this created variable
 */
fun SyntheticsBrowserTest.addLocalVariable(name: String, type: SyntheticsBrowserVariableType, pattern: String? = null) = apply {
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name)
                .type(type)
                .pattern(pattern)
                .example("")
        )
}

/**
 * Adds the global variable to the test
 * @param name name of the existing global variable (case sensitive)
 * @return BrowserTest object with this added global variable
 */
fun BrowserTest.addGlobalVariable(name: String) = apply {
    val variableId = getGlobalVariableId(name)
    config
        ?.addVariablesItem(
            SyntheticsBrowserVariable()
                .name(name)
                .id(variableId)
                .type(SyntheticsBrowserVariableType.GLOBAL)
        )
}
