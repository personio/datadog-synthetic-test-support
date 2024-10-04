# Changelog
All notable changes to this project will be documented in this file.

This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) and the format is partially based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) with customised sections:
- Breaking changes: any changes that could break the existing implementation and require adjustments from library user's side
- New features & improvements: any changes which add new functionality or improvements to the existing code
- Bug fixes: any changes that fix issues or bug in the existing code
- Dependencies: updates of dependency versions

## [Unreleased] - yyyy-mm-dd

### Breaking changes

### New features & improvements

### Bug fixes

### Dependencies
- Update datadog-api-client dependency from 2.27.0 to 2.28.0 ([#246](https://github.com/personio/datadog-synthetic-test-support/pull/246))
- Update jackson-dataformat-yaml dependency from 2.17.2 to 2.18.0 ([#245](https://github.com/personio/datadog-synthetic-test-support/pull/245))
- Update jackson-module-kotlin dependency from 2.17.2 to 2.18.0 ([#245](https://github.com/personio/datadog-synthetic-test-support/pull/245))
- Update kotlinx-serialization-json dependency from 1.7.2 to 1.7.3 ([#243](https://github.com/personio/datadog-synthetic-test-support/pull/243))
- Update secretsmanager dependency from 2.27.17 to 2.28.12 ([#241](https://github.com/personio/datadog-synthetic-test-support/pull/241))

## [2.6.0] - 2024-09-09
### New features & improvements
- Add `bodyRaw()` extraction function for API Tests to extract entire response body ([#238](https://github.com/personio/datadog-synthetic-test-support/pull/238))
- Add a wrapper to `createBrowserTest()` to avoid unwanted parallel executions during Datadog test updates ([#223](https://github.com/personio/datadog-synthetic-test-support/pull/223))

### Dependencies
- Update datadog-api-client dependency from 2.26.0 to 2.27.0 ([#233](https://github.com/personio/datadog-synthetic-test-support/pull/233))
- Update kotlin-stdlib dependency from 2.0.0 to 2.0.20 ([#234](https://github.com/personio/datadog-synthetic-test-support/pull/234))
- Update kotlinx-serialization-json dependency from 1.7.1 to 1.7.2 ([#231](https://github.com/personio/datadog-synthetic-test-support/pull/231))
- Update secretsmanager dependency from 2.26.27 to 2.27.17 ([#230](https://github.com/personio/datadog-synthetic-test-support/pull/230))

## [2.5.0] - 2024-08-08
### New features & improvements
- Refactor the Browser tests to use the builder approach ([#188](https://github.com/personio/datadog-synthetic-test-support/pull/188), [#189](https://github.com/personio/datadog-synthetic-test-support/pull/189), [#190](https://github.com/personio/datadog-synthetic-test-support/pull/190), [#197](https://github.com/personio/datadog-synthetic-test-support/pull/197))
- Add `bodyContainsJsonPathRegex()` assertion function to check if a json path element in a response body matches given regex [#228](https://github.com/personio/datadog-synthetic-test-support/pull/228)

### Dependencies
- Update commons-text dependency from 1.11.0 to 1.12.0 ([#207](https://github.com/personio/datadog-synthetic-test-support/pull/207))
- Update datadog-api-client dependency from 2.19.0 to 2.26.0 ([#185](https://github.com/personio/datadog-synthetic-test-support/pull/185), [#191](https://github.com/personio/datadog-synthetic-test-support/pull/191), [#203](https://github.com/personio/datadog-synthetic-test-support/pull/203), [#208](https://github.com/personio/datadog-synthetic-test-support/pull/208), [#216](https://github.com/personio/datadog-synthetic-test-support/pull/216), [#224](https://github.com/personio/datadog-synthetic-test-support/pull/224))
- Update kotlin-stdlib dependency from 1.9.22 to 2.0.0 ([#201](https://github.com/personio/datadog-synthetic-test-support/pull/201), [#210](https://github.com/personio/datadog-synthetic-test-support/pull/210))
- Update kotlinx-serialization-json dependency from 1.6.2 to 1.7.1 ([#195](https://github.com/personio/datadog-synthetic-test-support/pull/195), [#221](https://github.com/personio/datadog-synthetic-test-support/pull/221))
- Update jackson-dataformat-yaml dependency from 2.16.1 to 2.17.2 ([#202](https://github.com/personio/datadog-synthetic-test-support/pull/202), [#211](https://github.com/personio/datadog-synthetic-test-support/pull/211), [#226](https://github.com/personio/datadog-synthetic-test-support/pull/226))
- Update jackson-module-kotlin dependency from 2.16.1 to 2.17.2 ([#202](https://github.com/personio/datadog-synthetic-test-support/pull/202), [#211](https://github.com/personio/datadog-synthetic-test-support/pull/211), [#226](https://github.com/personio/datadog-synthetic-test-support/pull/226))
- Update secretsmanager dependency from 2.22.9 to 2.26.27 ([#187](https://github.com/personio/datadog-synthetic-test-support/pull/187), [#194](https://github.com/personio/datadog-synthetic-test-support/pull/194), [#198](https://github.com/personio/datadog-synthetic-test-support/pull/198), [#206](https://github.com/personio/datadog-synthetic-test-support/pull/206), [#212](https://github.com/personio/datadog-synthetic-test-support/pull/212), [#217](https://github.com/personio/datadog-synthetic-test-support/pull/217), [#227](https://github.com/personio/datadog-synthetic-test-support/pull/227))

## [2.4.0] - 2024-01-19
### New features & improvements
- Add `bodyDoesNotContain()` assertion function to check if a response body doesn't contain specific values ([#181](https://github.com/personio/datadog-synthetic-test-support/pull/181))
- Add `retry()` function to configure retry behavior of a single step in a multistep API test ([#182](https://github.com/personio/datadog-synthetic-test-support/pull/182))

### Dependencies
- Update commons-text dependency from 1.10.0 to 1.11.0 ([#151](https://github.com/personio/datadog-synthetic-test-support/pull/151))
- Update datadog-api-client dependency from 2.16.0 to 2.19.0 ([#164](https://github.com/personio/datadog-synthetic-test-support/pull/164), [#172](https://github.com/personio/datadog-synthetic-test-support/pull/172))
- Update kotlin-stdlib dependency from 1.9.10 to 1.9.22 ([#149](https://github.com/personio/datadog-synthetic-test-support/pull/149), [#163](https://github.com/personio/datadog-synthetic-test-support/pull/163), [#178](https://github.com/personio/datadog-synthetic-test-support/pull/178))
- Update kotlinx-serialization-json dependency from 1.6.0 to 1.6.2 ([#166](https://github.com/personio/datadog-synthetic-test-support/pull/166))
- Update jackson-dataformat-yaml dependency from 2.15.2 to 2.16.1 ([#153](https://github.com/personio/datadog-synthetic-test-support/pull/153), [#167](https://github.com/personio/datadog-synthetic-test-support/pull/167), [#176](https://github.com/personio/datadog-synthetic-test-support/pull/176))
- Update jackson-module-kotlin dependency from 2.15.2 to 2.16.1 ([#153](https://github.com/personio/datadog-synthetic-test-support/pull/153), [#167](https://github.com/personio/datadog-synthetic-test-support/pull/167), [#176](https://github.com/personio/datadog-synthetic-test-support/pull/176))
- Update secretsmanager dependency from 2.20.157 to 2.22.9 ([#157](https://github.com/personio/datadog-synthetic-test-support/pull/157), [#170](https://github.com/personio/datadog-synthetic-test-support/pull/170), [#173](https://github.com/personio/datadog-synthetic-test-support/pull/173))

## [2.3.0] - 2023-10-23
### New features & improvements
- Make the test `status` not set by default ([#144](https://github.com/personio/datadog-synthetic-test-support/pull/144))

## [2.2.0] - 2023-10-20
### New features & improvements
- Add handy `env()` and `team()` functions for adding corresponding Datadog `key:value` tags ([#126](https://github.com/personio/datadog-synthetic-test-support/pull/126))
- Add minimum working implementation of a refactored browser test support ([#129](https://github.com/personio/datadog-synthetic-test-support/pull/129), [#141](https://github.com/personio/datadog-synthetic-test-support/pull/141))
- Add `status()` function to set the SyntheticTestPauseStatus property ([#127](https://github.com/personio/datadog-synthetic-test-support/pull/127))
- Add `testFrequency()` function to set the test execution frequency in browser tests ([#137](https://github.com/personio/datadog-synthetic-test-support/pull/137))
- Add `browsersAndDevices()` function to set the device ids for browser tests ([#139](https://github.com/personio/datadog-synthetic-test-support/pull/139))
- Add `tags()` function to set the tags for browser tests ([#138](https://github.com/personio/datadog-synthetic-test-support/pull/138))

### Bug fixes
- Fix scroll parameters check to include values from -9999 to 9999 ([#140](https://github.com/personio/datadog-synthetic-test-support/pull/140))

### Dependencies
- Update datadog-api-client dependency from 2.15.0 to 2.16.0 ([#130](https://github.com/personio/datadog-synthetic-test-support/pull/130))
- Update secretsmanager dependency from 2.20.139 to 2.20.157 ([#132](https://github.com/personio/datadog-synthetic-test-support/pull/132))

## [2.1.0] - 2023-09-22
### Announcements
- In the future releases some BrowserTest-related methods (test config, variables, etc) will be replaced by the common methods from SyntheticTestBuilder.

### New features & improvements
- Add support for Multi-Step API Synthetic Tests ([#123](https://github.com/personio/datadog-synthetic-test-support/pull/123))
- Add deprecation warnings for BrowserTest ([#124](https://github.com/personio/datadog-synthetic-test-support/pull/124))

## [2.0.0] - 2023-09-01
### Breaking changes
- Set target JVM version to 17 ([#106](https://github.com/personio/datadog-synthetic-test-support/pull/106))

### Dependencies
- Update datadog-api-client dependency from 2.12.0 to 2.15.0 ([#105](https://github.com/personio/datadog-synthetic-test-support/pull/105), [#113](https://github.com/personio/datadog-synthetic-test-support/pull/113), [#117](https://github.com/personio/datadog-synthetic-test-support/pull/117))
- Update kotlin-stdlib dependency from 1.8.22 to 1.9.10 ([#110](https://github.com/personio/datadog-synthetic-test-support/pull/110), [#118](https://github.com/personio/datadog-synthetic-test-support/pull/118))
- Update kotlinx-serialization-json dependency from 1.5.1 to 1.6.0 ([#120](https://github.com/personio/datadog-synthetic-test-support/pull/120))
- Update secretsmanager dependency from 2.20.88 to 2.20.139 ([#103](https://github.com/personio/datadog-synthetic-test-support/pull/103), [#109](https://github.com/personio/datadog-synthetic-test-support/pull/109), [#115](https://github.com/personio/datadog-synthetic-test-support/pull/115))

## [1.2.0] - 2023-06-21
### New features & improvements
- Add advanced scheduling option to the test configuration ([#101](https://github.com/personio/datadog-synthetic-test-support/pull/101))

### Dependencies
- Update datadog-api-client dependency from 2.9.0 to 2.12.0 ([#80](https://github.com/personio/datadog-synthetic-test-support/pull/80), [#89](https://github.com/personio/datadog-synthetic-test-support/pull/89), [#98](https://github.com/personio/datadog-synthetic-test-support/pull/98))
- Update jackson-dataformat-yaml dependency from 2.15.0 to 2.15.2 ([#96](https://github.com/personio/datadog-synthetic-test-support/pull/96))
- Update jackson-module-kotlin dependency from 2.15.0 to 2.15.2 ([#96](https://github.com/personio/datadog-synthetic-test-support/pull/96))
- Update kotlin-stdlib dependency from 1.8.10 to 1.8.22 ([#83](https://github.com/personio/datadog-synthetic-test-support/pull/83), [#91](https://github.com/personio/datadog-synthetic-test-support/pull/91), [#102](https://github.com/personio/datadog-synthetic-test-support/pull/102))
- Update kotlinx-serialization-json dependency from 1.4.1 to 1.5.1 ([#78](https://github.com/personio/datadog-synthetic-test-support/pull/78), [#99](https://github.com/personio/datadog-synthetic-test-support/pull/99))
- Update secretsmanager dependency from 2.20.8 to 2.20.88 ([#77](https://github.com/personio/datadog-synthetic-test-support/pull/77), [#81](https://github.com/personio/datadog-synthetic-test-support/pull/81), [#94](https://github.com/personio/datadog-synthetic-test-support/pull/94), [#95](https://github.com/personio/datadog-synthetic-test-support/pull/95), [#102](https://github.com/personio/datadog-synthetic-test-support/pull/102))

## [1.1.0] - 2023-02-21
### New features & improvements
- Improve the configuration setup code ([#36](https://github.com/personio/datadog-synthetic-test-support/pull/36))
- Upgrade Java to 17 version ([#12](https://github.com/personio/datadog-synthetic-test-support/pull/12))
- Get configurations from yaml file ([16cbb0f](https://github.com/personio/datadog-synthetic-test-support/commit/16cbb0fb40d24e98d1fb1d20da5786004cdeb2bf))

### Dependencies
- Update datadog-api-client dependency from 2.2.0 to 2.9.0 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20), [#48](https://github.com/personio/datadog-synthetic-test-support/pull/48), [#67](https://github.com/personio/datadog-synthetic-test-support/pull/67), [#71](https://github.com/personio/datadog-synthetic-test-support/pull/71))
- Update kotlin-stdlib dependency from 1.7.10 to 1.8.10 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20), [#22](https://github.com/personio/datadog-synthetic-test-support/pull/22), [#48](https://github.com/personio/datadog-synthetic-test-support/pull/48), [#64](https://github.com/personio/datadog-synthetic-test-support/pull/64))
- Update kotlinx-serialization-json dependency from 1.4.0 to 1.4.1 ([#20](https://github.com/personio/datadog-synthetic-test-support/pull/20))
- Update secretsmanager dependency from 2.17.255 to 2.20.8 ([d84e121](https://github.com/personio/datadog-synthetic-test-support/commit/d84e121e0cab5e6f9e36514055f7979812ed6337), [0f24790](https://github.com/personio/datadog-synthetic-test-support/commit/0f24790f20bc2d1758c8ee963e643d8fee7e50d3), [03fa798](https://github.com/personio/datadog-synthetic-test-support/commit/03fa798ffdc4821f71984ea32c1564183045a18e), [1b800da](https://github.com/personio/datadog-synthetic-test-support/commit/1b800da4145dd55c7d4a3719b8496322d83018be), [#20](https://github.com/personio/datadog-synthetic-test-support/pull/20), [#21](https://github.com/personio/datadog-synthetic-test-support/pull/21), [#23](https://github.com/personio/datadog-synthetic-test-support/pull/23), [#24](https://github.com/personio/datadog-synthetic-test-support/pull/24), [#48](https://github.com/personio/datadog-synthetic-test-support/pull/48), [#57](https://github.com/personio/datadog-synthetic-test-support/pull/57), [#62](https://github.com/personio/datadog-synthetic-test-support/pull/62), [#66](https://github.com/personio/datadog-synthetic-test-support/pull/66), [#69](https://github.com/personio/datadog-synthetic-test-support/pull/69), [#74](https://github.com/personio/datadog-synthetic-test-support/pull/74))

## [1.0.2] - 2022-08-19
### New features & improvements
- Support features: status, advanced options for steps (timeout, allow failure and mark test as failed) ([9ca80d0](https://github.com/personio/datadog-synthetic-test-support/commit/9ca80d08feb169e46a223f970e1a1110a61862ed))
- Implement EnvironmentVariablesCredentialsProvider to fetch Datadog API credentials from environment variables ([d720eb1](https://github.com/personio/datadog-synthetic-test-support/commit/d720eb1f9895dc0ad39ef004972d0054e0b71188))

### Dependencies
- Update datadog-api-client dependency from 2.1.0 to 2.2.0 ([d3e6a79](https://github.com/personio/datadog-synthetic-test-support/commit/d3e6a79e93746a1ed7363a7472b647751fba3aa2))
- Update kotlinx-serialization-json dependency from 1.3.3 to 1.4.0 ([2873de1](https://github.com/personio/datadog-synthetic-test-support/commit/2873de15fc94695d09e937305209692debae3b5b))
- Update secretsmanager dependency from 2.17.249 to 2.17.255 ([b47b06e](https://github.com/personio/datadog-synthetic-test-support/commit/b47b06ecaf9afdbd5e0d0e3e603aca813492196c), [5195316](https://github.com/personio/datadog-synthetic-test-support/commit/5195316a768d46d6165d60a46245598259e3d176), [efc113c](https://github.com/personio/datadog-synthetic-test-support/commit/efc113c4a800706d42714f0d3fa2e0bd6ce2c831), [238846b](https://github.com/personio/datadog-synthetic-test-support/commit/238846b7be85c75f4ea939c74ce9e880cf143ff7))

## [1.0.1] - 2022-08-09
### Bug fixes
- Fix uploadFileStep function by adding element as a parameter ([d7fd558](https://github.com/personio/datadog-synthetic-test-support/commit/d7fd5584a13640a6935f1271b34d4aa82a11b571))

### Dependencies
- Update datadog-api-client dependency from 2.0.0 to 2.1.0 ([ba7aa2e](https://github.com/personio/datadog-synthetic-test-support/commit/ba7aa2ecef460aa87b3770331c831eced716065d))
- Update secretsmanager dependency from 2.17.240 to 2.17.249 ([e6b02b9](https://github.com/personio/datadog-synthetic-test-support/commit/e6b02b9af32821dd22849e9c16c692e28566760d), [936f94b](https://github.com/personio/datadog-synthetic-test-support/commit/936f94b904d39cd8cdca5df2b8b66e499fe583ff), [abfa26e](https://github.com/personio/datadog-synthetic-test-support/commit/abfa26e3567cd949076ae7485bcc930600bbdda0), [bda910c](https://github.com/personio/datadog-synthetic-test-support/commit/bda910c6b2711e3d21e0638aa55bb85a22947a30), [f123200](https://github.com/personio/datadog-synthetic-test-support/commit/f12320026bd75d531b9b7803046ab480b63075b8), [d0dccab](https://github.com/personio/datadog-synthetic-test-support/commit/d0dccab57c2e0fd0d138005ae8b5b5b079586c43), [716f4cc](https://github.com/personio/datadog-synthetic-test-support/commit/716f4ccebb0dbaa8b13a9ad3ae6c7138f1a3fe33))

## [1.0.0] - 2022-07-22
### New features & improvements
- Initial framework for synthetic test as code ([06632f2](https://github.com/personio/datadog-synthetic-test-support/commit/06632f2d77acb7276fce0b7d6755eba0a6efeb7c))
- Use immutable members for model classes and implement additional conditions for the steps ([2927b65](https://github.com/personio/datadog-synthetic-test-support/commit/2927b6556f7c26753f7001b6a7cb151a8d516e83))
- Add missing steps for navigation and special actions ([4a63cfd](https://github.com/personio/datadog-synthetic-test-support/commit/4a63cfdce7486f41102dc48a8a433a06ae7176e0))
- Refactor the code for adding steps and parameters ([e939dae](https://github.com/personio/datadog-synthetic-test-support/commit/e939dae3d4f628b070a7217bf2e9f5088a8e8870))
- Use DD provided assertions instead of custom enum and implement commonly used assertion steps ([e316cb0](https://github.com/personio/datadog-synthetic-test-support/commit/e316cb0d251e077a5795234b11cd6c0a4e5a36db))
- Add custom DSL syntax for the browser test creation and refactor steps ([5bf820b](https://github.com/personio/datadog-synthetic-test-support/commit/5bf820b2c453af234dbee12d49367179d56c427e))
- Make the test details configurable ([1520147](https://github.com/personio/datadog-synthetic-test-support/commit/1520147d5f20f3ba8de56ef438cb87ea5d633ce9))
- Add upload file and extract text steps ([4da1264](https://github.com/personio/datadog-synthetic-test-support/commit/4da126455e5e0fa99fdbe83a7f037e4f78bd9e4b))
- Add JS and api step validations ([638cbb7](https://github.com/personio/datadog-synthetic-test-support/commit/638cbb75f95e7953522fd405ca73708733d49115))
- Refactor steps to supply required parameters in constructor ([6025703](https://github.com/personio/datadog-synthetic-test-support/commit/60257035cd9a9b97c40a4ea07afb8f5f36a08bfe))
- Implement downloadedFileAssertion step ([9a90d5b](https://github.com/personio/datadog-synthetic-test-support/commit/9a90d5b067c418aa8bd057956a47d098e4f09756))
- Fetch DD API credentials from AWS secrets manager ([91bee52](https://github.com/personio/datadog-synthetic-test-support/commit/91bee520f532f90e0d19da94d427fcc3ed99a462))
- Add functions for alert and recovery messages ([b957348](https://github.com/personio/datadog-synthetic-test-support/commit/b9573484a18d9f7ab8afd83a4717f8e22050e9e8))

### Dependencies
- Update datadog-api-client dependency from 1.3.0 to 2.0.0 ([6439414](https://github.com/personio/datadog-synthetic-test-support/commit/643941490c665b45ac3cf559ce5dcd2a2f407550), [b5f4ece](https://github.com/personio/datadog-synthetic-test-support/commit/b5f4eceadd8f2513ab8b94021be90345bf9e77eb), [9013c2c](https://github.com/personio/datadog-synthetic-test-support/commit/9013c2cdd1ae394294914eb9ce990d44a0e895a8), [0ccd3d5](https://github.com/personio/datadog-synthetic-test-support/commit/0ccd3d5fe7555bd401ed9df6d07f5c1459600050))
- Update kotlin-stdlib dependency from 1.6.10 to 1.7.10 ([6439414](https://github.com/personio/datadog-synthetic-test-support/commit/643941490c665b45ac3cf559ce5dcd2a2f407550), [b5f4ece](https://github.com/personio/datadog-synthetic-test-support/commit/b5f4eceadd8f2513ab8b94021be90345bf9e77eb), [9013c2c](https://github.com/personio/datadog-synthetic-test-support/commit/9013c2cdd1ae394294914eb9ce990d44a0e895a8), [26988aa](https://github.com/personio/datadog-synthetic-test-support/commit/26988aa2541006b2edcd9738b801718f1aec9bdc))
- Update secretsmanager dependency from 2.17.220 to 2.17.240 ([bd2468c](https://github.com/personio/datadog-synthetic-test-support/commit/bd2468c8ce352249244e19fafa6c5fee05ff7c5e), [a303e86](https://github.com/personio/datadog-synthetic-test-support/commit/a303e86c84deaca558a9c560f26c9e68026634a8))
