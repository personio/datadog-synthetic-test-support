plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.dokka") version "1.6.10"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    `maven-publish`
}

group = "com.personio"
version = "1.0"

apply(plugin = "org.jetbrains.dokka")

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.20")
    api("javax.activation:activation:1.1.1")
    api("com.datadoghq:datadog-api-client:1.5.0")
    api("org.junit.jupiter:junit-jupiter-api:5.8.2")
    api("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.mockito:mockito-core:4.4.0")
    testImplementation("org.mockito:mockito-inline:4.4.0")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
}

tasks {
    test {
        useJUnitPlatform()
        filter {
            excludeTestsMatching("*.e2e.*")
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
    repositories {
        maven {
            name = "nexus-releases"
            val releasesPath = "maven-releases/"
            val snapshotsPath = "maven-snapshots/"
            val finalUrl = "***REMOVED***" +
                if (project.hasProperty("release")) releasesPath else snapshotsPath
            url = uri(finalUrl)
            credentials {
                username
                password
            }
        }
    }
}
