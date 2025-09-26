package com.personio.synthetics.builder

import com.datadog.api.client.v1.model.SyntheticsTestRequest
import com.datadog.api.client.v1.model.SyntheticsTestRequestBodyType

class RequestBuilder {
    private var url = ""
    private var method = RequestMethod.GET
    private var bodyType: SyntheticsTestRequestBodyType = SyntheticsTestRequestBodyType.APPLICATION_JSON
    private var body = ""
    private var followRedirects = false
    private var headers: Map<String, String> = mapOf()
    private var ignoreServerCertificateError = false

    fun build(): SyntheticsTestRequest =
        SyntheticsTestRequest()
            .url(url)
            .method(method.name)
            .bodyType(bodyType)
            .body(body)
            .followRedirects(followRedirects)
            .allowInsecure(ignoreServerCertificateError)
            .headers(headers)

    /**
     * Appends the cookie header
     * @param value Cookie value
     */
    fun cookies(value: String) {
        headers += mapOf("Cookie" to value)
    }

    /**
     * Sets the request URL
     * @param value URL
     */
    fun url(value: String) {
        url = value
    }

    /**
     * Sets the request method
     * @param value Request method
     */
    fun method(value: RequestMethod) {
        method = value
    }

    /**
     * Sets the request body type
     * @param value Request body type
     */
    fun bodyType(value: SyntheticsTestRequestBodyType) {
        bodyType = value
    }

    /**
     * Sets the request body
     * @param value Request body
     */
    fun body(value: String) {
        body = value
    }

    /**
     * Sets the need to follow any redirects
     * @param value Whether Datadog should follow any redirects
     */
    fun followRedirects(value: Boolean) {
        followRedirects = value
    }

    /**
     * Sets the need to ignore server certificate error
     * @param value Whether Datadog should ignore service certificate error during request
     */
    fun ignoreServerCertificateError(value: Boolean) {
        ignoreServerCertificateError = value
    }

    /**
     * Appends the request headers
     * @param headers Headers
     */
    fun headers(headers: Map<String, String>) {
        this.headers += headers
    }
}
