# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - yyyy-mm-dd

### Added
- Add License ([#2](https://github.com/personio/datadog-synthetic-test-support/pull/2))
- Add Github workflows for unit test and lint ([#3](https://github.com/personio/datadog-synthetic-test-support/pull/3))
- Add CONTRIBUTING file ([#4](https://github.com/personio/datadog-synthetic-test-support/pull/4))
- Add CODEOWNERS file ([#13](https://github.com/personio/datadog-synthetic-test-support/pull/13))
- Setup automatic dependencies updates via Dependabot ([#14](https://github.com/personio/datadog-synthetic-test-support/pull/14))


### Changed
- Update README file with dependencies, getting started, changelog and license sections ([#5](https://github.com/personio/datadog-synthetic-test-support/pull/5))
- Upgrade Java to 17 version ([#12](https://github.com/personio/datadog-synthetic-test-support/pull/12))
- Upgrade Gradle to 7.4.2 version ([#8](https://github.com/personio/datadog-synthetic-test-support/pull/8))

### Fixed

### Removed

### Security

### Dependencies
- Bump datadog-api-client from 2.2.0 to 2.5.0 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Bump jacoco from 0.8.7 to 0.8.8 ([#8](https://github.com/personio/datadog-synthetic-test-support/pull/8))
- Bump kotlin-stdlib from 1.7.10 to 1.7.20 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Bump kotlinx-serialization-json from 1.4.0 to 1.4.1 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Bump org.jetbrains.dokka from 1.7.10 to 1.7.20 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Bump org.jetbrains.kotlin.jvm from 1.7.10 to 1.7.20 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Bump plugin.serialization from 1.7.10 to 1.7.20 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Bump secretsmanager from 2.17.261 to 2.18.26 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))

## [1.0.2] - 2022-08-19
### Added
- Support features: status, advanced options for steps (timeout, allow failure and mark test as failed)
- EnvironmentVariablesCredentialsProvider to fetch Datadog API credentials from environment variables

## [1.0.1] - 2022-08-09
### Fixed
- Fixing uploadFileStep function by adding element as a parameter 

## [1.0.0] - 2022-07-22
### Added
- Framework for synthetic test as code ([#1](https://github.com/personio/datadog-synthetic-test-support/pull/1))

[Unreleased]: https://github.com/personio/datadog-synthetic-test-support/compare/v1.0.2...HEAD
[1.0.2]: https://github.com/personio/datadog-synthetic-test-support/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/personio/datadog-synthetic-test-support/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/personio/datadog-synthetic-test-support/releases/tag/v1.0.0
