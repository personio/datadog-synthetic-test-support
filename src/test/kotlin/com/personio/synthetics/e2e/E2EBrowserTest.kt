package com.personio.synthetics.e2e

import com.personio.synthetics.dsl.syntheticBrowserTest
import com.personio.synthetics.step.ui.model.TargetElement
import org.junit.jupiter.api.Test

/**
 * Since the builder is still in progress, don't use it for the browser test creation
 */
class E2EBrowserTest {
    /**
     * This test creates a Synthetic Browser test in Datadog.
     */
    @Test
    fun `create synthetic browser test`() {
        syntheticBrowserTest("[Browser] Synthetic-Test-As-Code") {
            steps {
                input("This element", "new_text", TargetElement("#test"))
                click("Click step", TargetElement("#test"))
                hover("hover!", TargetElement("#test"))
            }
        }
    }
}
