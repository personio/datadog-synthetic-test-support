package com.personio.synthetics.client

import com.personio.synthetics.config.Credentials

class ConfigCredentialsProvider(val credentials: Credentials) : CredentialsProvider {
    override fun getCredentials() = ApiCredentials(credentials.ddApiKey!!, credentials.ddAppKey!!)
}
