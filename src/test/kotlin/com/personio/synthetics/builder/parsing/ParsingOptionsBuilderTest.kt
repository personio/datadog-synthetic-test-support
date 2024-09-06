package com.personio.synthetics.builder.parsing

import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParseTestOptionsType
import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParserType
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
import com.datadog.api.client.v1.model.SyntheticsVariableParser
import com.personio.synthetics.TEST_STEP_NAME
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ParsingOptionsBuilderTest {
    private val parsingOptionsBuilder = ParsingOptionsBuilder()

    @Test
    fun `variable sets variable name`() {
        parsingOptionsBuilder.bodyJsonPath("any")
        parsingOptionsBuilder.variable(TEST_STEP_NAME)

        assertEquals(TEST_STEP_NAME, parsingOptionsBuilder.build()!!.name)
    }

    @Test
    fun `build throws IllegalArgumentException when no parsing options set`() {
        val parsingOptionsBuilder = ParsingOptionsBuilder()
        parsingOptionsBuilder.variable(TEST_STEP_NAME)

        assertThrows<IllegalStateException> {
            parsingOptionsBuilder.build()
        }
    }

    @Test
    fun `build throws IllegalArgumentException when no variable name set`() {
        assertThrows<IllegalStateException> {
            parsingOptionsBuilder.build()
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `bodyJsonPath returns parsing options with JSON_PATH parser type and HTTP_BODY type`(secure: Boolean) {
        parsingOptionsBuilder.variable(TEST_STEP_NAME)
        parsingOptionsBuilder.bodyJsonPath("any_json_path", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name(TEST_STEP_NAME)
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.JSON_PATH)
                        .value("any_json_path"),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure),
            parsingOptionsBuilder.build(),
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `bodyRegex returns parsing options with REGEX parser type and HTTP_BODY type`(secure: Boolean) {
        parsingOptionsBuilder.variable(TEST_STEP_NAME)
        parsingOptionsBuilder.bodyRegex("any_regex", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name(TEST_STEP_NAME)
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.REGEX)
                        .value("any_regex"),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure),
            parsingOptionsBuilder.build(),
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `bodyRaw returns parsing options with RAW parser type and HTTP_BODY type`(secure: Boolean) {
        parsingOptionsBuilder.variable(TEST_STEP_NAME)
        parsingOptionsBuilder.bodyRaw(secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name(TEST_STEP_NAME)
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.RAW),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure),
            parsingOptionsBuilder.build(),
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `header returns parsing options with RAW parser type and HTTP_HEADER type`(secure: Boolean) {
        parsingOptionsBuilder.variable(TEST_STEP_NAME)
        parsingOptionsBuilder.header("any_header_name", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name(TEST_STEP_NAME)
                .field("any_header_name")
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.RAW),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
                .secure(secure),
            parsingOptionsBuilder.build(),
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `headerRegex returns parsing options with REGEX parser type and HTTP_HEADER type`(secure: Boolean) {
        parsingOptionsBuilder.variable(TEST_STEP_NAME)
        parsingOptionsBuilder.headerRegex("any_header_name", "any_regex", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name(TEST_STEP_NAME)
                .field("any_header_name")
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.REGEX)
                        .value("any_regex"),
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
                .secure(secure),
            parsingOptionsBuilder.build(),
        )
    }
}
