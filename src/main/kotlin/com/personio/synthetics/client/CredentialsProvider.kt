package com.personio.synthetics.client

import kotlinx.serialization.Serializable

@Serializable
data class ApiCredentials(val apiKey: String, val appKey: String)

interface CredentialsProvider {
    fun getCredentials(): ApiCredentials
}

class CredentialsProviderException(message: String) : Exception(message)
