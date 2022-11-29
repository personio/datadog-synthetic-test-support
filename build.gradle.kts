plugins {
    val kotlinVersion = "1.7.20"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.dokka") version kotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
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
        shouldRunAfter("test")
    }

tasks.check { dependsOn(e2eTest) }

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.21")
    val awsSdkVersion = "2.18.26"
    api("software.amazon.awssdk:secretsmanager:$awsSdkVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    api("javax.activation:activation:1.1.1")
    api("com.datadoghq:datadog-api-client:2.5.0")
    testRuntimeOnly("software.amazon.awssdk:sso:$awsSdkVersion")
    testRuntimeOnly("software.amazon.awssdk:sts:$awsSdkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.mockito:mockito-inline:4.9.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
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
            xml.isEnabled = true
            html.isEnabled = false
            xml.destination = file("$buildDir/test-results/test/xml/jacocoReport.xml")
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
