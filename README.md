# Synthetic Test Support

This library provides a programmatic way to manage Datadog synthetic browser tests. This library contains all the necessary functions that are required to manage the synthetic browser tests with different steps and configurations.

## Dependencies

Synthetic Test Support library builds up on top of the Datadog's API [Java client library](https://github.com/DataDog/datadog-api-client-java/) and provides Kotlin implementation of functions to create and edit synthetic browser tests.

There are 2 ways to provide the API credentials in order to communicate with Datadog API:

* fetching from the environment variables: DD_API_KEY and DD_APP_KEY
* fetching from [AWS Secrets Manager](https://aws.amazon.com/secrets-manager/)

## Getting started

This library is compiled with JDK 17. Your project should be on JDK 17+ in order to use it. 

Start using the library in a gradle project by following the steps below:

1. In the gradle project, add a new source set and Gradle task to run test generation in `build.gradle.kts` file.
```kotlin
    sourceSets {
        create("syntheticTest")
    }
    
    task("publishSyntheticTests", JavaExec::class) {
        group = "syntheticTest"
        mainClass.set("<yourPackageName>.<kotlinFileWithMainClass>")
        classpath = sourceSets["syntheticTest"].runtimeClasspath
    }

    val syntheticTestImplementation: Configuration by configurations
```

2. Add dependencies:
```kotlin
    dependencies {
        // check out [Releases](https://github.com/personio/datadog-synthetic-test-support/releases) to get the latest version
        syntheticTestImplementation("com.personio:datadog-synthetic-test-support:x.x.x")
        // check out [Kotlin Releases](https://github.com/JetBrains/kotlin/releases) to get the latest version
        syntheticTestImplementation("org.jetbrains.kotlin:kotlin-stdlib:x.x.x")
    }
```

3. Implement your synthetic browser tests, for example like this:
```kotlin
    fun `add a synthetic test`() {
       syntheticBrowserTest("Test Login to the app") {
          tags(listOf("env:qa"))
          baseUrl(URL("https://synthetic-test.personio.de"))
          browserAndDevice(SyntheticsDeviceID.CHROME_LAPTOP_LARGE)
          publicLocation(Location.FRANKFURT_AWS)
          testFrequency(5.minutes)
          retry(1, 600.milliseconds)
          useGlobalVariable("PASSWORD")
          inputTextStep(
             stepName = "Enter username",
             targetElement = TargetElement("[name='email']"),
             text = "test@personio.de"
          )
          inputTextStep(
             stepName = "Enter password",
             targetElement = TargetElement("[name='password']"),
             text = "{{ PASSWORD }}"
          )
          clickStep(
             stepName = "Click login button",
             targetElement = TargetElement("[name='login']")
          ) {
             waitBeforeDeclaringStepAsFailed(30.seconds)
          }
          pageContainsTextAssertion(
             stepName = "Check Welcome title",
             expectedText = "Welcome"
          )
       }
    }
```
   
4. Create a new kotlin file with the `main` function and call the function created in Step 3 to publish the tests, for example like this:
```kotlin
    fun main() {
        `add a synthetic test`()
    }
```

5. To publish the browser tests to Datadog, use the following command:
```bash
   ./gradlew publishSyntheticTests`
```

Notes:
- Test name is a unique identifier which is used during test publishing. If a test with the given name already exists in Datadog, it will be overridden.

### Configuring credentials provider

You can use AWS Secrets Manager to store and fetch Datadog API credentials to enable the library to publish test changes to Datadog.

You need to include an additional AWS package dependency, depending on how you login to AWS:
- if you use [AWS SSO](https://docs.aws.amazon.com/sdkref/latest/guide/access-sso.html), please add [`software.amazon.awssdk:sso`](https://mvnrepository.com/artifact/software.amazon.awssdk/sso) dependency
- if you use [AWS STS](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html), please add [`software.amazon.awssdk:sts`](https://mvnrepository.com/artifact/software.amazon.awssdk/sts) dependency

Please check the currently supported AWS SDK version in `build.gradle.kts`.

## Build the library

The following are some useful commands for working with the project.

- Run `./gradlew build` to build the whole project.
- Run `./gradlew ktlintFormat` to format the project.
- Run `./gradlew test` to execute all the unit tests.
- Run `./gradlew e2eTest` to execute the e2e test.
- Run `./gradlew publishToMavenLocal` to publish to your local .m2 repository.

## Creating a new release

This section to be updated with information about publishing releases.

## Changelog

This repository maintains a separate log for each change. Refer to `CHANGELOG.md` for changes to the `synthetic-test-support` library. Ensure that the `CHANGELOG.md` file has been updated to include the new version's changes & additions.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and the project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## How to contribute and report issues

Feel free to contribute! Just create a new issue or a new pull request following the [Contributing Guidelines](https://github.com/personio/datadog-synthetic-test-support/blob/master/CONTRIBUTING.md).

## License

The library is licensed under the [MIT License](https://github.com/personio/datadog-synthetic-test-support/blob/master/LICENSE), copyright 2022 Personio GmbH.
