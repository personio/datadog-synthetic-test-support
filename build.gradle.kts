plugins {
    val kotlinVersion = "1.7.20"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.dokka") version kotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    kotlin("plugin.serialization") version kotlinVersion
    `maven-publish`
    jacoco
}

group = "com.personio"

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    val awsSdkVersion = "2.19.31"
    val jacksonVersion = "2.14.2"
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.apache.commons:commons-text:1.10.0")
    api("software.amazon.awssdk:secretsmanager:$awsSdkVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    api("javax.activation:activation:1.1.1")
    api("com.datadoghq:datadog-api-client:2.7.0")
    testRuntimeOnly("software.amazon.awssdk:sso:$awsSdkVersion")
    testRuntimeOnly("software.amazon.awssdk:sts:$awsSdkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.mockito:mockito-inline:5.1.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
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

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(components["kotlin"])
            artifact(javadocJar)
            artifact(sourcesJar)
            version =
                if (project.hasProperty("release")) {
                    "${project.version}"
                } else {
                    "${project.version}-SNAPSHOT"
                }
        }
    }
}
