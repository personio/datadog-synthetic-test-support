package com.personio.synthetics.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.apache.commons.text.StringSubstitutor
import org.apache.commons.text.lookup.StringLookupFactory
import java.io.File
import java.nio.file.Files

fun loadConfiguration(configurationFile: String) {
    val stringSubstitutor = StringSubstitutor(
        StringLookupFactory.INSTANCE.environmentVariableStringLookup()
    ).setEnableUndefinedVariableException(true)
    ObjectMapper(YAMLFactory()).apply {
        registerModule(kotlinModule())
        Config.testConfig = readValue(
            stringSubstitutor.replace(String(Files.readAllBytes(File(this::class.java.classLoader.getResource(configurationFile).file).toPath()))),
            Configuration::class.java
        )
    }
}

internal object Config {
    lateinit var testConfig: Configuration
}
