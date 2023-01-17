package com.personio.synthetics.step.api

import com.datadog.api.client.v1.model.SyntheticsAssertion
import com.datadog.api.client.v1.model.SyntheticsAssertionTarget
import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParseTestOptionsType
import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParserType
import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.datadog.api.client.v1.model.SyntheticsTestRequest
import com.datadog.api.client.v1.model.SyntheticsVariableParser
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.config.isDatadogVariable
import com.personio.synthetics.model.api.ExtractValue
import com.personio.synthetics.model.api.RequestParams
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType
import java.net.URL

/**
 * Adds a new API step to the synthetic browser test
 * @param stepName Name of the step
 * @param method The method of the http request GET/POST
 * @param f Add all the parameters required for this test step
 * @return ApiStep object with this step added
 */
fun BrowserTest.apiStep(stepName: String, httpMethod: String, f: ApiStep.() -> Unit): ApiStep =
    addStep(stepName, ApiStep()) {
        type = SyntheticsStepType.RUN_API_TEST
        params = with(RequestParams()) {
            request.config.request(SyntheticsTestRequest().url(config?.request?.url).method(httpMethod))
            copy(request = request.copy(subtype = "http"))
        }
        f()
    }

/**
 * Configures the API step for the synthetic browser test
 */
class ApiStep : SyntheticsStep() {
    /**
     * Adds a new assertion to the API step
     * @param f Add the assertion required for the API cal
     * type: Add the type of assertion
     * property: Header property where assertion is to be performed (Optional)
     * property parameter is not required for body or status code assertions
     * operator: Operator type
     * expected: Expected value for assertion
     * @return ApiStep object with this assertion added
     */
    fun assertion(f: SyntheticsAssertionTarget.() -> Unit) = apply {
        val assertionTarget = SyntheticsAssertionTarget()
        assertionTarget.f()
        withParamType<RequestParams> {
            apply { request.config.assertions?.add(SyntheticsAssertion(assertionTarget)) }
        }
    }

    /**
     * Sets the request body to be passed to the API request
     * @param body The body of the request
     * @return ApiStep object with the request body set
     */
    fun requestBody(body: String) = apply {
        withParamType<RequestParams> {
            apply { request.config.request.body = body }
        }
    }

    /**
     * Sets the request headers to be passed to the API request
     * @param headers a map of header name and header values
     * eg: mapOf("content-type" to "application/json")
     * @return ApiStep object with the request headers set
     */
    fun requestHeaders(headers: Map<String, String>) = apply {
        withParamType<RequestParams> {
            apply { request.config.request.headers = headers }
        }
    }

    /**
     * Sets the url used for the API request
     * @param url url to be used for the API request
     * Supply the parameter like one of the following
     * - only the location (eg: /test/page) for appending to the base url of the test
     * - pass full url including http(s)://
     * - global or local variable. For using those, use the function "fromVariable(variableName)" in the parameter
     * @return ApiStep object with the method set
     */
    fun url(url: String) = apply {
        withParamType<RequestParams> {
            apply {
                val target = if (url.isDatadogVariable()) url else {
                    runCatching { URL(url) }
                        .recover { URL(request.config.request.url + url) }
                        .getOrThrow()
                        .toString()
                }
                request.config.request.url = target
            }
        }
    }

    /**
     * Extracts the header value from the API response
     * @param name Name of the variable to extract the value to
     * @param field Header field that need to be extracted from the response
     * @param regex Regular expression to extract the value in the header field (Optional parameter)
     * @return ApiStep object with the extract variable step added
     */
    fun extractHeaderValue(name: String, field: String, regex: String? = null) = apply {
        val extractValue = ExtractValue(name)
        val parserType =
            if (regex == null) SyntheticsGlobalVariableParserType.RAW else SyntheticsGlobalVariableParserType.REGEX

        extractValue
            .field(field)
            .parser(
                SyntheticsVariableParser()
                    .type(parserType)
                    .value(regex)
            )
            .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
        params = withParamType<RequestParams> {
            setExtractValue(extractValue)
        }
    }

    /**
     * Extracts value from the API response
     * @param name Name of the variable to extract the value to
     * @param parserType Type of parser to use
     * @param parserValue Parser value (Optional parameter)
     * For REGEX type, pass the regular expression
     * For JSON_PATH type, pass the json path
     * For X_PATH type, pass the xpath
     * For RAW type, this parameter is not required
     * @return ApiStep object with the extract variable step added
     */
    fun extractBodyValue(
        name: String,
        parserType: SyntheticsGlobalVariableParserType,
        parserValue: String? = null
    ) = apply {
        val parser = SyntheticsVariableParser()
            .type(parserType)
            .value(parserValue)

        val newExtractValue = ExtractValue(name)
        newExtractValue
            .parser(parser)
            .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
        params = withParamType<RequestParams> {
            setExtractValue(newExtractValue)
        }
    }

    /**
     * Sets extract value object
     * @param extractValue Extract value object that need to be set
     * @return RequestParams object with the extract value object added
     */
    private fun RequestParams.setExtractValue(extractValue: ExtractValue): RequestParams {
        val currentExtractValues = request.options.extract_values.orEmpty()
        return copy(request = request.copy(options = request.options.copy(extract_values = currentExtractValues + extractValue)))
    }
}
