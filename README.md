# Synthetic Test Support
This library provides a programmatic way to manage Datadog synthetic browser tests. This library contains all the necessary functions that are required to manage the synthetic browser tests with different steps and configurations.

## How to use the library
You can find out more details and usage of this library in documentation.

### Using AWS Secrets Manager as credentials provider
You can use AWS Secrets Manager to store and fetch Datadog API credentials to enable the library to publish test changes to Datadog.

You need to include an additional AWS package dependency, depending on how you login to AWS:
- if you use [AWS SSO](https://docs.aws.amazon.com/sdkref/latest/guide/access-sso.html), please add [`software.amazon.awssdk:sso`](https://mvnrepository.com/artifact/software.amazon.awssdk/sso) dependency
- if you use [AWS STS](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html), please add [`software.amazon.awssdk:sts`](https://mvnrepository.com/artifact/software.amazon.awssdk/sts) dependency

Currently supported AWS SDK version is `2.17.248`.

## Build the library
The following are some useful commands for working with the project.

- Run `./gradlew build` to build the whole project.
- Run `./gradlew ktlintFormat` to format the project.
- Run `./gradlew test` to execute all the unit tests.
- Run `./gradlew e2eTest` to execute the e2e test, you need to be logged in to `AWS` with `dev` profile and set the environment variable `AWS_PROFILE` to `dev` profile.
- Run `./gradlew publishToMavenLocal` to publish to your local .m2 repository.

## Creating a new release
The process of creating a new release of the library is as follows:

- The pipeline step `tagRelease` will automatically create a Gitlab release and a tag from changes to CHANGELOG.md. The tag name is `v<version number>` where `<version number>` should follow [semantic versioning](https://semver.org/). Please use a meaningful release title and document all relevant changes as part of the release notes.
- Ensure that the CHANGELOG.md file has been updated to include the new version's changes & additions following the [keepachangelog.com](https://keepachangelog.com/en/1.0.0/) format.
- Once a new release has been created, trigger a pipeline for the master branch. Alternatively, the master pipeline can also be triggered automatically by merging a merge request.
- Publishing a release of the new version to Nexus is manual, so make sure you trigger it as part of the last stage.

## Changelog
This repository maintains a separate log for each change. Refer to `CHANGELOG.md` for changes to the `synthetic-test-support` library.
