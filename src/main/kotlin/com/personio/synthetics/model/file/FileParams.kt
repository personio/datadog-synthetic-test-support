package com.personio.synthetics.model.file

import com.personio.synthetics.model.Params
import com.personio.synthetics.model.element.Element

internal data class FileParams(
    val element: Element? = null,
    val files: List<UploadFile> = emptyList(),
    val withClick: Boolean = false,
) : Params()
