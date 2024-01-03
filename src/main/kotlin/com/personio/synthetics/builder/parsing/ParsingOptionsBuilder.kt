package com.personio.synthetics.builder.parsing

import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParseTestOptionsType
import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParserType
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
import com.datadog.api.client.v1.model.SyntheticsVariableParser

class ParsingOptionsBuilder {
    private var variableName: String? = null
    private var parsingOptions: SyntheticsParsingOptions? = null

    fun build(): SyntheticsParsingOptions? {
        checkNotNull(variableName) { "Variable name must be provided." }
        checkNotNull(parsingOptions) { "Parsing options must be provided." }
        parsingOptions!!.name(variableName)

        return parsingOptions
    }

    /**
     * Sets the variable
     * @param name Name of the variable
     */
    fun variable(name: String) {
        variableName = name
    }

    /**
     * Extracts the value from the response body using JSON path
     * @param jsonPath The JSON path to extract the value from
     * @param secure Set to true to disallow the extracted value to be read from DataDog UI
     * By default secure is set to false allowing the extracted value to be available for reading in Datadog UI
     */
    fun bodyJsonPath(
        jsonPath: String,
        secure: Boolean = false,
    ) {
        parsingOptions =
            SyntheticsParsingOptions()
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.JSON_PATH)
                        .value(jsonPath),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure)
    }

    /**
     * Extracts the value from the response body using regular expression
     * @param regex Regular expression
     * @param secure Set to true to disallow the extracted value to be read from DataDog UI
     * By default secure is set to false allowing the extracted value to be available for reading in Datadog UI
     */
    fun bodyRegex(
        regex: String,
        secure: Boolean = false,
    ) {
        parsingOptions =
            SyntheticsParsingOptions()
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.REGEX)
                        .value(regex),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure)
    }

    /**
     * Extracts the value from a response header
     * @param name Header name
     * @param secure Set to true to disallow the extracted value to be read from DataDog UI
     * By default secure is set to false allowing the extracted value to be available for reading in Datadog UI
     */
    fun header(
        name: String,
        secure: Boolean = false,
    ) {
        parsingOptions =
            SyntheticsParsingOptions()
                .field(name)
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.RAW),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
                .secure(secure)
    }

    /**
     * Extracts the value from a response header using regular expression
     * @param name Header name
     * @param regex Regular expression
     * @param secure Set to true to disallow the extracted value to be read from DataDog UI
     * By default secure is set to false allowing the extracted value to be available for reading in Datadog UI
     */
    fun headerRegex(
        name: String,
        regex: String,
        secure: Boolean = false,
    ) {
        parsingOptions =
            SyntheticsParsingOptions()
                .field(name)
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.REGEX)
                        .value(regex),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
                .secure(secure)
    }
}
