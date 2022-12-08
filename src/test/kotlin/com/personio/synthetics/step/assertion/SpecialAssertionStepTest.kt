package com.personio.synthetics.step.assertion

import com.datadog.api.client.v1.model.SyntheticsStepType
import com.personio.synthetics.client.BrowserTest
import com.personio.synthetics.client.SyntheticsApiClient
import com.personio.synthetics.config.Defaults
import com.personio.synthetics.model.assertion.AssertionParams
import com.personio.synthetics.model.assertion.DownloadedFileAssertionParams
import com.personio.synthetics.model.assertion.FileNameCheckType
import com.personio.synthetics.model.assertion.FileSizeCheckType
import com.personio.synthetics.step.waitBeforeDeclaringStepAsFailed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import kotlin.time.Duration.Companion.seconds

internal class SpecialAssertionStepTest {
    private val defaults = Defaults(300, 300, 1, 1, 60.0, 10, listOf("awsregion"))
    private val syntheticsApi = mock<SyntheticsApiClient>()
    private val browserTest = BrowserTest("Test", syntheticsApi, defaults)

    @Test
    fun `customJavascriptAssertion adds the new step item to the browser test object`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        )

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `customJavascriptAssertion adds the step item of type Test custom JavaScript assertion`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        )

        assertEquals(SyntheticsStepType.ASSERT_FROM_JAVASCRIPT, browserTest.steps!![0].type)
    }

    @Test
    fun `customJavascriptAssertion adds AssertionParams to the browser test object`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        )

        assertInstanceOf(AssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `customJavascriptAssertion adds the passed code to params object`() {
        val code = "return true;"
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = code
        )

        assertEquals(code, (browserTest.steps?.get(0)?.params as AssertionParams).code)
    }

    @Test
    fun `customJavascriptAssertion accepts additional configuration changes to the test step`() {
        browserTest.customJavascriptAssertion(
            stepName = "Step",
            code = "return true;"
        ) { waitBeforeDeclaringStepAsFailed(10.seconds) }

        assertEquals(10, browserTest.steps?.get(0)?.timeout)
    }

    @Test
    fun `downloadedFileAssertion adds the new step item to the browser test object`() {
        browserTest.downloadedFileAssertion("Step") {}

        assertEquals(1, browserTest.steps?.size)
    }

    @Test
    fun `downloadedFileAssertion adds the step item of type Test downloaded file assertion`() {
        browserTest.downloadedFileAssertion("Step") {}

        assertEquals(SyntheticsStepType.ASSERT_FILE_DOWNLOAD, browserTest.steps!![0].type)
    }

    @Test
    fun `downloadedFileAssertion adds DownloadedFileAssertionParams to the browser test object`() {
        browserTest.downloadedFileAssertion("Step") {}

        assertInstanceOf(DownloadedFileAssertionParams::class.java, browserTest.steps!![0].params)
    }

    @Test
    fun `nameCheck adds the passed file name parameters to the DownloadedFileAssertionParams object`() {
        val checkType = FileNameCheckType.EQUALS
        val value = "expectedName"
        browserTest.downloadedFileAssertion("Step") {
            nameCheck(checkType, value)
        }
        val params = browserTest.steps?.get(0)?.params as DownloadedFileAssertionParams

        assertEquals(checkType.value.toString(), params.file.nameCheck?.type)
        assertEquals(value, params.file.nameCheck?.value)
    }

    @Test
    fun `nameCheck doesn't throw exception if expected value is not passed and check type is IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.downloadedFileAssertion("Step") {
                nameCheck(FileNameCheckType.IS_EMPTY)
            }
        }
    }

    @Test
    fun `nameCheck doesn't throw exception if expected value is not passed and check type is NOT_IS_EMPTY`() {
        assertDoesNotThrow {
            browserTest.downloadedFileAssertion("Step") {
                nameCheck(FileNameCheckType.NOT_IS_EMPTY)
            }
        }
    }

    @Test
    fun `nameCheck throws exception if expected value is not passed and check type is neither IS_EMPTY, nor NOT_IS_EMPTY`() {
        assertThrows<IllegalStateException> {
            browserTest.downloadedFileAssertion("Step") {
                nameCheck(FileNameCheckType.EQUALS)
            }
        }
    }

    @Test
    fun `sizeCheck adds the passed file size parameters to the DownloadedFileAssertionParams object`() {
        val checkType = FileSizeCheckType.GREATER
        val value = 1
        browserTest
            .downloadedFileAssertion("Step") {
                sizeCheck(checkType, value)
            }
        val params = browserTest.steps?.get(0)?.params as DownloadedFileAssertionParams

        assertEquals(checkType.value.toString(), params.file.sizeCheck?.type)
        assertEquals(value, params.file.sizeCheck?.value)
    }

    @Test
    fun `expectedMd5 adds the passed MD5 to the DownloadedFileAssertionParams object`() {
        browserTest
            .downloadedFileAssertion("Step") {
                expectedMd5("expectedMd5")
            }
        val params = browserTest.steps?.get(0)?.params as DownloadedFileAssertionParams

        assertEquals("expectedMd5", params.file.md5)
    }
}
