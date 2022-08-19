package com.personio.synthetics.client

class EnvironmentVariablesCredentialsProvider() : CredentialsProvider {
    override fun getCredentials() = ApiCredentials(getValueFromEnvVar(DD_API_KEY), getValueFromEnvVar(DD_APP_KEY))

    private fun getValueFromEnvVar(name: String) = requireNotNull(System.getenv(name).takeUnless(String::isNullOrBlank)) { "Environment variable $name not set" }

    companion object {
        internal const val DD_API_KEY = "DD_API_KEY"
        internal const val DD_APP_KEY = "DD_APP_KEY"
    }
}
