import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.1.10"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
    kotlin("plugin.serialization") version kotlinVersion
    `maven-publish`
    jacoco
    signing
}

group = "com.personio"
version = System.getenv("VERSION")

jacoco {
    toolVersion = "0.8.12"
}

repositories {
    mavenCentral()
}

val e2eTest =
    tasks.register<Test>("e2eTest") {
        description = "Runs end to end test"
        group = "verification"
        filter {
            includeTestsMatching("*.e2e.*")
        }
        useJUnitPlatform()
    }

dependencies {
    val awsSdkVersion = "2.30.31"
    val jacksonVersion = "2.18.3"
    val junitVersion = "5.12.1"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.10")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.apache.commons:commons-text:1.13.0")
    api("software.amazon.awssdk:secretsmanager:$awsSdkVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    api("javax.activation:activation:1.1.1")
    api("com.datadoghq:datadog-api-client:2.32.0")
    testRuntimeOnly("software.amazon.awssdk:sso:$awsSdkVersion")
    testRuntimeOnly("software.amazon.awssdk:sts:$awsSdkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")
}

tasks {
    test {
        useJUnitPlatform()
        filter {
            excludeTestsMatching("*.e2e.*")
        }
    }
    jacocoTestReport {
        reports {
            xml.required.convention(true)
            html.required.convention(false)
            xml.outputLocation.convention(layout.buildDirectory.file("test-results/test/xml/jacocoReport.xml"))
        }
    }
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget("17"))
        }
    }
}

val javadocJar by tasks.register("javadocJar", Jar::class) {
    dependsOn(tasks.named("dokkaJavadoc"))
    archiveClassifier.convention("javadoc")
    from(layout.buildDirectory.dir("javadoc"))
}

val sourcesJar =
    tasks.register("sourcesJar", Jar::class) {
        archiveClassifier.convention("sources")
        from(sourceSets.main.get().allSource)
    }

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
    publications {
        register("maven", MavenPublication::class) {
            from(components["java"])
            pom {
                groupId = "com.personio"
                name.set("Datadog Synthetic test Support")
                description.set("Kotlin library for managing Datadog Synthetic test as code")
                url.set("https://github.com/personio/datadog-synthetic-test-support")
                packaging = "jar"
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/personio/datadog-synthetic-test-support/blob/master/LICENSE")
                    }
                }
                organization {
                    name.set("Personio SE & Co. KG")
                    url.set("https://personio.com")
                }
                developers {
                    developer {
                        id.set("personio")
                        name.set("Test Solutions Squad")
                    }
                }
                scm {
                    url.set("https://github.com/personio/datadog-synthetic-test-support")
                    connection.set("scm:git:https://github.com/personio/datadog-synthetic-test-support.git")
                    developerConnection.set("scm:git:ssh://github.com:personio/datadog-synthetic-test-support.git")
                }
                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/personio/datadog-synthetic-test-support/issues")
                }
                ciManagement {
                    system.set("GitHub Actions")
                    url.set("https://github.com/personio/datadog-synthetic-test-support/actions")
                }
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("GPG_PASSPHRASE")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["maven"])
}
