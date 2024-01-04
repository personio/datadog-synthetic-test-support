package com.personio.synthetics.step.file

import com.datadog.api.client.v1.model.SyntheticsStep
import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.file.FileParams
import com.personio.synthetics.model.file.UploadFile
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.ui.model.TargetElement
import java.io.File
import java.util.Base64

/**
 * Adds a new step for uploading a file to the synthetic browser test
 * @param stepName Name of the step
 * @param uploadFile The file to be uploaded
 * @param element The target element details where the file needs to be uploaded to
 * @param f Additional configurations that need to be added to the step such as timeout, allowFailure and so on
 * @return Synthetic step object with uploadFileStep added
 */
fun BrowserTest.uploadFileStep(
    stepName: String,
    uploadFile: File,
    element: TargetElement,
    f: (SyntheticsStep.() -> Unit)? = null,
) = addStep(stepName) {
    type = SyntheticsStepType.UPLOAD_FILES
    params =
        FileParams(
            files =
                listOf(
                    UploadFile(
                        name = uploadFile.name,
                        content = convertToBase64(uploadFile),
                        size = uploadFile.length(),
                    ),
                ),
            element = element.getElementObject(),
        )
    if (f != null) f()
}

private fun convertToBase64(file: File): String {
    return Base64.getEncoder().encodeToString(file.readBytes())
}
