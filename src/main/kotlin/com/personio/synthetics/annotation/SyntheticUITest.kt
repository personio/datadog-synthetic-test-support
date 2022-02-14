package com.personio.synthetics.annotation

import com.personio.synthetics.extension.SyntheticsTestExtension
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Annotation for creating Synthetic browser tests
 * It creates the BrowserTest object with Synthetics API client
 * and default configurations set
 *
 * To use this, annotate the test class with this annotation
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SyntheticsTestExtension::class)
annotation class SyntheticUITest
