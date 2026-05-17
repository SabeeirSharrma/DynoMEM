# Contributing to DynoMEM

First off, thank you for considering contributing to DynoMEM! It's people like you that make DynoMEM such a great optimization mod for Minecraft.

## Getting Started

1. **Prerequisites:**
   - Java Development Kit (JDK) 17 or higher.
   - A Git client.
   - Familiarity with Fabric Modding and Mixins.

2. **Fork and Clone:**
   - Fork the repository on GitHub.
   - Clone your fork locally.
   - Run `./gradlew build` to ensure the project compiles out-of-the-box.

## Development Workflow

1. **Branching:**
   - Create a new branch for each feature or bug fix: `git checkout -b feature/your-feature-name` or `bugfix/issue-number`.
   
2. **Coding Standards:**
   - Follow the existing code style. We use standard Java conventions.
   - Keep your mixins focused and specific. Document `@Overwrite` or complex `@Inject` mixins with a `@reason` and `@author`.
   - Avoid adding unnecessary dependencies unless absolutely critical for performance or compatibility.
   - Ensure the thread-safety of any caching mechanisms you modify. Memory optimization should not compromise stability.

3. **Testing:**
   - Test your changes using `./gradlew runClient` to spawn a test client.
   - Verify that there are no memory leaks or threading crashes in the updated logic (especially `SmallThreadingDetector` and `FastMap`).

## Pull Request Process

1. **Commit Messages:**
   - Write clear, concise commit messages.

2. **Submitting a PR:**
   - Push your branch to your fork.
   - Open a Pull Request against the `main` branch.
   - Describe the changes you made, the rationale behind them, and how you tested them.
   - Link any relevant issues.

3. **Review:**
   - The maintainers will review your PR. Be prepared to make requested changes. Once approved, your PR will be merged.

## Reporting Bugs

If you find a bug, please create an issue on our GitHub repository. Provide:
- Your Minecraft version and Fabric Loader version.
- The version of DynoMEM you are using.
- A clear description of the bug.
- Crash reports or logs (`latest.log`), if applicable.
- Steps to reproduce the issue.

## Contact

For further discussions or questions, reach out to the maintainers on GitHub or via the DynoClient community.
