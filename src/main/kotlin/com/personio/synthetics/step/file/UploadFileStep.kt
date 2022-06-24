package com.personio.synthetics.step.file

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.model.file.FileParams
import com.personio.synthetics.model.file.UploadFile
import com.personio.synthetics.step.Step
import com.personio.synthetics.step.addStep
import com.personio.synthetics.step.withParamType
import java.io.File
import java.util.Base64

/**
 * Adds a new Upload file step to the synthetic browser test
 * @param stepName Name of the step
 * @param f Pass the required parameters for this test step such as file to be uploaded, target element
 * @return UploadFileStep object with this step added
 */
fun BrowserTest.uploadFileStep(stepName: String, f: UploadFileStep.() -> Unit): UploadFileStep =
    addStep(stepName, UploadFileStep()) {
        type = SyntheticsStepType.UPLOAD_FILES
        params = FileParams()
        f()
        check((params as FileParams).files.isNotEmpty()) { "The file to be uploaded has to be passed for the step:'$stepName'" }
    }

/**
 * Configure the Upload file step for the synthetic browser test
 */
class UploadFileStep : Step() {
    /**
     * Adds the file to be uploaded to the test step
     * @param file The file to be uploaded
     * All the contents of the file are held in the memory when the application runs. Trying to use a very large file
     * could cause out of memory issues
     * @return UploadFileStep object with the file added
     */
    fun uploadFile(file: File) = apply {
        params = withParamType<FileParams> {
            copy(files = files.plus(UploadFile(name = file.name, content = convertToBase64(file), size = file.length())))
        }
    }

    private fun convertToBase64(file: File): String {
        return Base64.getEncoder().encodeToString(file.readBytes())
    }
}
