package com.personio.synthetics.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.apache.commons.text.StringSubstitutor
import org.apache.commons.text.lookup.StringLookupFactory
import java.io.File
import java.nio.file.Files

fun getConfigFromFile(path: String): Configuration {
    val stringSubstitutor =
        StringSubstitutor(StringLookupFactory.INSTANCE.environmentVariableStringLookup())
            .setEnableUndefinedVariableException(true)

    ObjectMapper(YAMLFactory()).apply {
        registerModule(kotlinModule())
        return readValue(
            stringSubstitutor.replace(
                String(
                    Files.readAllBytes(
                        File(
                            this::class.java.classLoader
                                .getResource(path)!!
                                .file,
                        ).toPath(),
                    ),
                ),
            ),
            Configuration::class.java,
        )
    }
}

fun loadConfiguration(configurationFile: String) {
    Config.testConfig = getConfigFromFile(configurationFile)
}

internal object Config {
    lateinit var testConfig: Configuration
}
