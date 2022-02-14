package com.personio.synthetics.step.api

import com.datadog.api.v1.client.model.HTTPMethod
import com.datadog.api.v1.client.model.SyntheticsAssertion
import com.datadog.api.v1.client.model.SyntheticsAssertionOperator
import com.datadog.api.v1.client.model.SyntheticsAssertionTarget
import com.datadog.api.v1.client.model.SyntheticsAssertionType
import com.datadog.api.v1.client.model.SyntheticsBrowserTest
import com.datadog.api.v1.client.model.SyntheticsGlobalVariableParseTestOptionsType
import com.datadog.api.v1.client.model.SyntheticsGlobalVariableParserType
import com.datadog.api.v1.client.model.SyntheticsStep
import com.datadog.api.v1.client.model.SyntheticsStepType
import com.datadog.api.v1.client.model.SyntheticsTestRequest
import com.datadog.api.v1.client.model.SyntheticsVariableParser
import com.personio.synthetics.model.api.ExtractValue
import com.personio.synthetics.model.api.RequestParams

/**
 * Adds a new API step to the synthetic browser test
 * @return Synthetic step object with this step added
 */
fun SyntheticsBrowserTest.addApiStep(): SyntheticsStep {
    val apiStep = SyntheticsStep().type(SyntheticsStepType.RUN_API_TEST)
    val requestParams = RequestParams()
    requestParams.request.config.request(SyntheticsTestRequest().url(config?.request?.url))
    requestParams.request.subtype = "http"
    apiStep.params = requestParams
    addStepsItem(apiStep)
    return apiStep
}

/**
 * Adds a new assertion to the API step
 * @param assertionType Type of assertion
 * @param property Header property where assertion is to be performed (Optional)
 * This parameter is not required for body or status code assertions
 * @param operator Operator type
 * @param expected Expected value for assertion
 * @return SyntheticsStep object with this assertion added
 */
fun SyntheticsStep.addAssertion(
    assertionType: SyntheticsAssertionType,
    property: String? = null,
    operator: SyntheticsAssertionOperator,
    expected: Any = Any()
): SyntheticsStep {
    val assertion = SyntheticsAssertion(
        SyntheticsAssertionTarget()
            .operator(operator)
            .property(property)
            .target(expected)
            .type(assertionType)
    )
    val requestParams = params as RequestParams
    requestParams.request.config.assertions?.add(assertion)
    return this
}

/**
 * Body to be passed to the API request
 * @param body The body of the request
 * @return SyntheticsStep object with the request body set
 */
fun SyntheticsStep.requestBody(body: String): SyntheticsStep {
    val requestParams = params as RequestParams
    requestParams.request.config.request.body = body
    return this
}

/**
 * Headers to be passed to the API request
 * @param headers a map of header name and header values
 * eg: mapOf("content-type" to "application/json")
 * @return SyntheticsStep object with the request headers set
 */
fun SyntheticsStep.requestHeaders(headers: Map<String, String>): SyntheticsStep {
    val requestParams = params as RequestParams
    requestParams.request.config.request.headers = headers
    return this
}

/**
 * The method of the API request
 * @param method The method of the request GET/POST
 * @return SyntheticsStep object with the method set
 */
fun SyntheticsStep.method(method: HTTPMethod): SyntheticsStep {
    val requestParams = params as RequestParams
    requestParams.request.config.request.method = method
    return this
}

/**
 * Set the url used for the API request
 * @param url url to be used for the API request
 * Use only the location (eg: /test/page) for appending to the base url of the test
 * else pass full url including http(s)://
 */
fun SyntheticsStep.url(url: String): SyntheticsStep {
    val requestParams = params as RequestParams
    var requestUrl = url
    if (!url.contains("http")) {
        requestUrl = requestParams.request.config.request.url.plus("/$url")
    }
    requestParams.request.config.request.url = requestUrl
    return this
}

/**
 * Extract the header value from the API response
 * @param name Name of the variable to extract the value to
 * @param field Header field that need to be extracted from the response
 * @param regex Regular expression to extract the value in the header field (Optional parameter)
 * @return SyntheticsStep object with the extract variable step added
 */
fun SyntheticsStep.extractHeaderValue(name: String, field: String, regex: String? = null): SyntheticsStep {
    val requestParams = params as RequestParams
    val extractValue = ExtractValue(name)
    var parserType = SyntheticsGlobalVariableParserType.RAW

    if (regex != null) {
        parserType = SyntheticsGlobalVariableParserType.REGEX
    }
    extractValue
        .field(field)
        .parser(
            SyntheticsVariableParser()
                .type(parserType)
                .value(regex)
        )
        .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)

    if (requestParams.request.options.extract_values.isNullOrEmpty()) {
        requestParams.request.options.extract_values = mutableListOf(extractValue)
    } else {
        requestParams.request.options.extract_values?.add(extractValue)
    }
    return this
}

/**
 * Extract value from the response body
 * @param name Name of the variable to extract the value to
 * @param parserType Type of parser to use
 * @param parserValue Parser value (Optional parameter)
 * For REGEX type, pass the regular expression
 * For JSON_PATH type, pass the json path
 * For X_PATH type, pass the xpath
 * For RAW type, this parameter is not required
 * @return SyntheticsStep object with the extract variable step added
 */
fun SyntheticsStep.extractBodyValue(
    name: String,
    parserType: SyntheticsGlobalVariableParserType,
    parserValue: String? = null
): SyntheticsStep {
    val requestParams = params as RequestParams

    val parser = SyntheticsVariableParser()
        .type(parserType)
        .value(parserValue)

    val extractValue = ExtractValue(name)
    extractValue
        .parser(parser)
        .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
    if (requestParams.request.options.extract_values.isNullOrEmpty()) {
        requestParams.request.options.extract_values = mutableListOf(extractValue)
    } else {
        requestParams.request.options.extract_values?.add(extractValue)
    }
    return this
}
