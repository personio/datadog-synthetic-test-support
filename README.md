# Synthetic Test Support
This library is used for creating Datadog Synthetic Browser Test as code

## Installation in your project
Add the Personio nexus repository to your `build.gradle` file
```kotlin
repositories { 
    maven("https://nexus.tools.personio-internal.de/repository/maven/") 
}
```
Add the dependency to the project
```kotlin
dependencies {
    testImplementation("com.personio:synthetic-test-support:x.x.x")
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
      SECRETS_FILE="ops/env/dev/secrets.yaml"
      echo "DD_API_KEY=$(sops -d --extract '["dd_api_key"]' $SECRETS_FILE)" > dd.env
      echo "DD_APP_KEY=$(sops -d --extract '["dd_app_key"]' $SECRETS_FILE)" >> dd.env
  artifacts:
    reports:
      dotenv: dd.env
  variables:
    AWS_PROFILE: dev

create:test:
  image: 824725208937.dkr.ecr.eu-central-1.amazonaws.com/personio-jdk:1.3.0
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
            .message("{{#is_alert}} @slack-not_gitlab_qa Test Failed {{/is_alert}}")
            .addTagsItem("synthetics-api")
            .setUrl("https://synthetics-monitoring-hrm.personio.de/")
        syntheticsTest.inputTextStep()
            .name("Enter username")
            .targetElement("#email")
            .text("qa-engineering@personio.de")
        syntheticsTest.clickStep()
            .name("Click login button")
            .targetElement("button[type=\"submit\"]")
        syntheticsTest.assertionStep(AssertionType.ELEMENTPRESENT)
            .targetElement("[data-test-id=\"settings-listitem-cost-centers\"]")
    }
}
```
## Changelog

This repository maintains a separate log for each change. Refer to `CHANGELOG.md` for changes to the `synthetic-test-support` library.
