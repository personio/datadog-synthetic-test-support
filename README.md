# Synthetic Test Support
This library is used for creating Datadog Synthetic Browser Test as code

## Installation in your project
Add the Personio nexus repository to your `build.gradle` file
```kotlin
repositories { 
    mavenCentral() 
}
```
Add the dependency to the project
```kotlin
dependencies {
    testImplementation("com.personio:datadog-synthetic-test-support:x.x.x")
}
```
For running the tests, the API and APP keys of Datadog has to be added to the project. Add the API and APP keys to `secrets.yaml`
To run the test creation scripts in Gitlab CI, add the following configuration to `gitlab-ci.yaml`
```yaml
dd:keys:
  stage: get-keys
  tags:
    - dev
  script:
    - |
      ***REMOVED***
      ***REMOVED***
      ***REMOVED***
  artifacts:
    reports:
      dotenv: dd.env
  variables:
    AWS_PROFILE: dev

create:test:
  image: ***REMOVED***
  stage: test-create
  script:
    - ./gradlew test
  dependencies:
    - dd:keys
```

## Creating synthetic browser test
To create a synthetic test, annotate the class with `@SyntheticUITest`. This library provides the class `BrowserTest` which has to be added as parameter to the constructor.
For example
```kotlin
@SyntheticUITest
class SyntheticTest(private val syntheticsTest: BrowserTest) {
    @Test
    fun `create synthetic test`() {
        syntheticsTest
            .name("Synthetic Test")
            .message("{{#is_alert}} @slack-test_slack_channel Test Failed {{/is_alert}}")
            .addTagsItem("synthetics-api")
            .setUrl("https://synthetic-test.personio.de/")
        syntheticsTest.inputTextStep()
            .name("Enter username")
            .targetElement("[name='email']")
            .text("test@personio.de")
        syntheticsTest.clickStep()
            .name("Click login button")
            .targetElement("[name='login']")
        syntheticsTest.assertionStep(AssertionType.ELEMENTPRESENT)
            .targetElement("[name='link-name']")
    }
}
```
