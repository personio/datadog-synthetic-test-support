package com.personio.synthetics.step.file

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.file.FileParams
import com.personio.synthetics.model.file.UploadFile
import com.personio.synthetics.step.addStep
import java.io.File
import java.util.Base64

/**
 * Adds a new Upload file step to the synthetic browser test
 * @param stepName Name of the step
 * @param uploadFile The file to be uploaded
 * @param f Additional configurations that need to be added to the step like timeout, allowFailure etc.
 * @return Upload files type synthetic step object
 */
fun BrowserTest.uploadFileStep(
    stepName: String,
    uploadFile: File,
    f: (SyntheticsStep.() -> Unit)? = null
) = addStep(stepName) {
    type = SyntheticsStepType.UPLOAD_FILES
    params = FileParams(
        files = listOf(UploadFile(name = uploadFile.name, content = convertToBase64(uploadFile), size = uploadFile.length()))
    )
    if (f != null) f()
}

private fun convertToBase64(file: File): String {
    return Base64.getEncoder().encodeToString(file.readBytes())
}
