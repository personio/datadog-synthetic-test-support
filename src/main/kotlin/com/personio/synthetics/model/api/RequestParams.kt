package com.personio.synthetics.model.api

import com.personio.synthetics.model.Params

internal data class RequestParams(
    val request: ApiRequest = ApiRequest()
) : Params()
