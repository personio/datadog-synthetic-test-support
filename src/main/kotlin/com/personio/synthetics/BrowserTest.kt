package com.personio.synthetics

import com.datadog.api.v1.client.api.SyntheticsApi
import com.datadog.api.v1.client.model.SyntheticsBrowserTest

class BrowserTest(private val syntheticsApi: SyntheticsApi) : SyntheticsBrowserTest() {
    /**
     * Creates / Updates the synthetic browser test. If the passed test name exists, it will update
     * the existing test else it will create a new test with the passed name
     * @param testName Name of the test to create or update
     * @return SyntheticsBrowserTest object
     */
    fun createBrowserTest(testName: String): SyntheticsBrowserTest {
        if (testName.isNullOrBlank()) {
            throw Exception("Please pass test name")
        }
        val testId = getTestId(testName)
        return if (testId != null) {
            syntheticsApi.updateBrowserTest(testId, this)
        } else {
            syntheticsApi.createSyntheticsBrowserTest(this)
        }
    }

    internal fun getGlobalVariableId(variableName: String): String? {
        val variables = syntheticsApi.listGlobalVariables()
        for (variable in variables.variables!!) {
            if (variableName == variable.name) {
                return variable.id
            }
        }
        return null
    }

    private fun getTestId(testName: String): String? {
        val tests = syntheticsApi.listTests()
        for (test in tests.tests!!) {
            if (test.name == testName) {
                return test.publicId
            }
        }
        return null
    }
}
