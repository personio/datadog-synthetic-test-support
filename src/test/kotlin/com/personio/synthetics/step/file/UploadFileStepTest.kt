package com.personio.synthetics.step.file

import com.datadog.api.v1.client.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.model.file.FileParams
import com.personio.synthetics.model.file.UploadFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.io.File
import java.util.Base64

internal class UploadFileStepTest {
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi)

    @Test
    fun `uploadFileStep adds new step to the browser test`() {
        browserTest.uploadFileStep(
            stepName = "Step",
            uploadFile = getFile()
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `uploadFileStep creates step with type Upload files and params of type FileParams`() {
        browserTest.uploadFileStep(
            stepName = "Step",
            uploadFile = getFile()
        )
        val step = browserTest.steps?.get(0)

        assertEquals(SyntheticsStepType.UPLOAD_FILES, step?.type)
        assertInstanceOf(FileParams::class.java, step?.params)
    }

    @Test
    fun `uploadFileStep adds the passed file to the FileParams object`() {
        val uploadFileContent = "text"
        val fileToBeUploaded = getFile(uploadFileContent).apply { writeText(uploadFileContent) }
        val expectedFile = UploadFile(
            name = fileToBeUploaded.name,
            size = fileToBeUploaded.length(),
            content = Base64.getEncoder().encodeToString(uploadFileContent.toByteArray())
        )

        browserTest.uploadFileStep(
            stepName = "Step",
            uploadFile = fileToBeUploaded
        )
        val params = browserTest.steps?.get(0)?.params as FileParams

        assertEquals(expectedFile, params.files[0])
    }

    @Test
    fun `uploadFileStep accepts additional configuration changes to the test step`() {
        browserTest.uploadFileStep(
            stepName = "Step",
            uploadFile = getFile()
        ) { timeout = 10 }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    private fun getFile(content: String = "text"): File =
        File.createTempFile("upload", ".pdf").apply { writeText(content) }
}
