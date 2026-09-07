<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Building and running

## Prerequisites

- Git and a working internet connection for the first dependency download.
- JDK 8, 16, 17, 21, and 25 when building the complete matrix.
- A graphical desktop and compatible OpenGL drivers for client runs.

Gradle toolchains select the compiler required by each target. The JVM that launches Gradle can differ from that compiler. See [JVM build groups](commands/JVM_BUILDS.md) for the exact split used by the repository scripts and CI.

## Common commands

Build and test every Stonecutter target:

```bash
./gradlew build
./gradlew test
```

Build a single Stonecutter target:

```bash
./gradlew :1.21.1-neoforge:build
```

Build a standalone port:

```bash
ports/1.8.9-forge/gradlew -p ports/1.8.9-forge build
```

The chronological [target command catalog](commands/TARGETS.md) lists build and client-run commands for every supported target.

Build the complete Stonecutter and standalone-port matrix:

```bash
scripts/build-all-targets.sh
```

Pass Gradle arguments through the all-target script when needed:

```bash
scripts/build-all-targets.sh --no-daemon --stacktrace
```

## Shared and generated sources

Edit shared Modstitch/Stonecutter code under `src/main`. The `versions/` projects receive processed sources during their Gradle build. After a shared-source change, rebuild and then synchronize those processed sources into Quilt ports:

```bash
./gradlew build
scripts/sync-quilt-sources.sh
```

Quilt ports are checked-in standalone projects, so include their synchronized changes in the same commit.

## Running a client

Use the same project path and replace `build` with `runClient`:

```bash
./gradlew :1.21.1-neoforge:runClient
ports/1.8.9-forge/gradlew -p ports/1.8.9-forge runClient
```

Fabric and Quilt development clients include Mod Menu where a compatible version exists. It remains a development-only dependency and is not bundled into release jars.

## Artifacts

- Stonecutter jars: `versions/<minecraft-version>-<loader>/build/libs/`
- Standalone-port jars: `ports/<port>/build/libs/`

Collect release artifacts by mod version:

```bash
scripts/collect-release-jars.sh
```

The collector groups jars under `release-jars/<mod-version>/` using the version in each filename and rejects filenames that do not contain a semantic mod version.

## Legacy Forge notes

The 1.7.10, 1.8.9, and 1.12.2 builds require Java 8 and old ForgeGradle wrappers. The 1.7.10 build overrides ForgeGradle 1.2's retired Mojang HTTP endpoints with immutable Mojang object URLs. The 1.12.2 port stays on ForgeGradle 2.3 so Forge's development runtime does not scan newer build-tool libraries as potential mods.
