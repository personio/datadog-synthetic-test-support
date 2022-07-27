package com.personio.synthetics.model.assertion

import com.datadog.api.client.v1.model.SyntheticsCheckType
import com.personio.synthetics.model.Params

internal data class DownloadedFileAssertionParams(
    val file: File,
) : Params()

internal data class File(
    val nameCheck: NameCheck? = null,
    val sizeCheck: SizeCheck? = null,
    val md5: String? = null,
) : Params()

internal data class NameCheck(
    val type: String,
    val value: String?,
) : Params()

internal data class SizeCheck(
    val type: String,
    val value: Int,
) : Params()

enum class FileNameCheckType(val value: SyntheticsCheckType) {
    NOT_CONTAINS(SyntheticsCheckType.NOT_CONTAINS),
    IS_EMPTY(SyntheticsCheckType.IS_EMPTY),
    STARTS_WITH(SyntheticsCheckType.STARTS_WITH),
    NOT_EQUALS(SyntheticsCheckType.NOT_EQUALS),
    NOT_IS_EMPTY(SyntheticsCheckType.NOT_IS_EMPTY),
    CONTAINS(SyntheticsCheckType.CONTAINS),
    MATCH_REGEX(SyntheticsCheckType.MATCH_REGEX),
    EQUALS(SyntheticsCheckType.EQUALS),
    NOT_STARTS_WITH(SyntheticsCheckType.NOT_STARTS_WITH),
}

enum class FileSizeCheckType(val value: SyntheticsCheckType) {
    LOWER(SyntheticsCheckType.LOWER),
    GREATER(SyntheticsCheckType.GREATER),
    NOT_EQUALS(SyntheticsCheckType.NOT_EQUALS),
    GREATER_EQUALS(SyntheticsCheckType.GREATER_EQUALS),
    LOWER_EQUALS(SyntheticsCheckType.LOWER_EQUALS),
}
