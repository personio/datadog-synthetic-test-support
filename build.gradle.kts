import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.9.0"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.dokka") version "1.8.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    val awsSdkVersion = "2.20.116"
    val jacksonVersion = "2.15.2"
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.apache.commons:commons-text:1.10.0")
    api("software.amazon.awssdk:secretsmanager:$awsSdkVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    api("javax.activation:activation:1.1.1")
    api("com.datadoghq:datadog-api-client:2.14.0")
    testRuntimeOnly("software.amazon.awssdk:sso:$awsSdkVersion")
    testRuntimeOnly("software.amazon.awssdk:sts:$awsSdkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
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
        kotlinOptions {
            jvmTarget = "17"
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
