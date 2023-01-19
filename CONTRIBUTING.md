# Contributing Guidelines

Thank you for your interest and contributing to our project!

Please read through this document before submitting any issues or sending pull requests to ensure we
have all the necessary information to effectively respond to your bug report or contribution.

## Getting Help

If you have a question about the library or have encountered any problems using it, start a
discussion using the [Question][question] form.

## Reporting Bugs/Feature Requests

Use the Github [Issue tracker][issue] to report a bug, documentation issue or request a new feature by filling in the respective form. 

Before submitting an issue, please search for similar issues among the open and recently closed ones
to make sure it was not reported already.

While filling in the form, add all relevant information, including:

* Detailed test/use case that would help to reproduce and understand the issue
* The version of our library being used
* Any modifications you've made relevant to the issue
* Anything unusual about your environment or deployment

## Contributions via Pull Requests

Contributions via pull requests are much appreciated.

Dependency: You should have JDK 17 installed.

Before submitting a pull request, please ensure that:

* You are working against the latest source on the `master` branch.
* You checked the existing open and recently merged pull requests to make sure the problem is not addressed already.
* You opened an issue to discuss the proposed change. Avoid changing too many things at once. Make sure the issue covers one problem/improvement at a time.

In order to contribute via pull requests, please follow the steps below:

1. Clone the repository.
2. Create a new branch from `master`.
3. Develop and test your code changes. See the project's [README](README.md) for further information about working in this repository.
4. Add necessary unit tests for the code changes and make sure that all tests pass locally.
5. Run `./gradlew ktlintFormat` to autoformat your code.
6. Commit to your branch using clear commit messages. Make sure to add brief descriptions of the changes to "Unreleased" section in [CHANGELOG](CHANGELOG.md).
7. Send us a pull request against the project's `master` branch.
8. Your branch may be merged once all configured checks pass, including:
    - The branch has passed the tests in CI.
    - An approval from code owners is received after the review.

GitHub provides additional documentation on [forking a repository](https://help.github.com/articles/fork-a-repo/)
and [creating a pull request](https://help.github.com/articles/creating-a-pull-request/).

## Committing

We prefer squashed or rebased commits so that all changes from a branch are committed to `master` as a single commit.
All pull requests are squashed when merged, but rebasing prior to merge gives you better control over the commit
message.

### Commit messages

Take a moment to write meaningful commit messages using the following format:

```text
Subject

Problem

Solution

Fixes #[GitHub issue ID]
```

* Subject - one line commit title, describing what is done, not the result, with a reference to the GitHub issue by ID.
* Problem - explain the context why you're making the change and what problem it solves.
* Solution - describe the modifications you've made.

## Coding conventions

Please follow the rules below to keep the code clean and precise.

### Naming Conventions

Whenever an acronym is included as part of a field name or parameter name:

* If the acronym comes at the start of the field or parameter name, use lowercase for the
  entire acronym, e.g `String url`.
* Otherwise, keep the first letter of the acronym uppercase and use lowercase for the
  rest of the acronym, e.g. `String baseUrl`.

### Formatting

For code formatting we use ktlint plugin. Run `./gradlew ktlintFormat` to autoformat your code.

### Spelling

Use American English spelling rules when writing documentation as well as for code - class names, method names, variable names, etc.

### Documentation

* Explain the purpose of added functions in a concise manner.
* Explain parameters if applicable using `@param` tag, provide examples of usage and default state.
* Explain what the function returns if applicable using `@return` tag.
* Do not use @author tags. Instead, contributors are listed on [GitHub](https://github.com/personio/datadog-synthetic-test-support/graphs/contributors).

### Tests

#### Naming

All test classes must end with a `Test` suffix.

#### Assertions

Use assertions from `org.junit.jupiter.api`.

#### Mocking

Use `org.mockito.kotlin.mock` library.

## Releases
Details about release process can be found in the project's [`README`](https://github.com/personio/datadog-synthetic-test-support#release-process).

[question]: https://github.com/personio/datadog-synthetic-test-support/discussions/new
[issue]: https://github.com/personio/datadog-synthetic-test-support/issues/new/choose
