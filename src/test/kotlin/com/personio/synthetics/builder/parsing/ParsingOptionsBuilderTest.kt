package com.personio.synthetics.builder.parsing

import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParseTestOptionsType
import com.datadog.api.client.v1.model.SyntheticsGlobalVariableParserType
import com.datadog.api.client.v1.model.SyntheticsParsingOptions
import com.datadog.api.client.v1.model.SyntheticsVariableParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ParsingOptionsBuilderTest {
    @Test
    fun `variable sets variable name`() {
        val sut = ParsingOptionsBuilder()
        sut.bodyJsonPath("any")
        sut.variable("any_name")

        assertEquals("any_name", sut.build()!!.name)
    }

    @Test
    fun `build throws IllegalArgumentException when no parsing options set`() {
        val sut = ParsingOptionsBuilder()
        sut.variable("any_name")

        assertThrows<IllegalStateException> {
            sut.build()
        }
    }

    @Test
    fun `build throws IllegalArgumentException when no variable name set`() {
        val sut = ParsingOptionsBuilder()

        assertThrows<IllegalStateException> {
            sut.build()
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `bodyJsonPath returns parsing options with JSON_PATH parser type and HTTP_BODY type`(secure: Boolean) {
        val sut = ParsingOptionsBuilder()
        sut.variable("any_name")
        sut.bodyJsonPath("any_json_path", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name("any_name")
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.JSON_PATH)
                        .value("any_json_path")
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure),
            sut.build()
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `bodyRegex returns parsing options with REGEX parser type and HTTP_BODY type`(secure: Boolean) {
        val sut = ParsingOptionsBuilder()
        sut.variable("any_name")
        sut.bodyRegex("any_regex", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name("any_name")
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.REGEX)
                        .value("any_regex")
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_BODY)
                .secure(secure),
            sut.build()
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `header returns parsing options with RAW parser type and HTTP_HEADER type`(secure: Boolean) {
        val sut = ParsingOptionsBuilder()
        sut.variable("any_name")
        sut.header("any_header_name", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name("any_name")
                .field("any_header_name")
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.RAW)
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
                .secure(secure),
            sut.build()
        )
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `headerRegex returns parsing options with REGEX parser type and HTTP_HEADER type`(secure: Boolean) {
        val sut = ParsingOptionsBuilder()
        sut.variable("any_name")
        sut.headerRegex("any_header_name", "any_regex", secure)

        assertEquals(
            SyntheticsParsingOptions()
                .name("any_name")
                .field("any_header_name")
                .parser(
                    SyntheticsVariableParser()
                        .type(SyntheticsGlobalVariableParserType.REGEX)
                        .value("any_regex")
                )
                .type(SyntheticsGlobalVariableParseTestOptionsType.HTTP_HEADER)
                .secure(secure),
            sut.build()
        )
    }
}