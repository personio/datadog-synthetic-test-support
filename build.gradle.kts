import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.0.20"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    kotlin("plugin.serialization") version kotlinVersion
    `maven-publish`
    jacoco
    signing
}

group = "com.personio"
version = System.getenv("VERSION")

jacoco {
    toolVersion = "0.8.8"
}

repositories {
    mavenCentral()
}

val e2eTest =
    task<Test>("e2eTest") {
        description = "Runs end to end test"
        group = "verification"
        filter {
            includeTestsMatching("*.e2e.*")
        }
        useJUnitPlatform()
    }

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
    val awsSdkVersion = "2.28.12"
    val jacksonVersion = "2.18.0"
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.apache.commons:commons-text:1.12.0")
    api("software.amazon.awssdk:secretsmanager:$awsSdkVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    api("javax.activation:activation:1.1.1")
    api("com.datadoghq:datadog-api-client:2.28.0")
    testRuntimeOnly("software.amazon.awssdk:sso:$awsSdkVersion")
    testRuntimeOnly("software.amazon.awssdk:sts:$awsSdkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.1")
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
            xml.required.set(true)
            html.required.set(false)
            xml.outputLocation.set(buildDir.resolve("test-results/test/xml/jacocoReport.xml"))
        }
    }
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    from(File(buildDir, "javadoc"))
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

java {
    withJavadocJar()
    withSourcesJar()
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
