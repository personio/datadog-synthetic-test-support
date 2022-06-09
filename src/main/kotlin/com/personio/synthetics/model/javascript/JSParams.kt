package com.personio.synthetics.model.javascript

import com.personio.synthetics.model.Params

internal data class JSParams(val code: String = "", val variable: Variable = Variable()) : Params()
