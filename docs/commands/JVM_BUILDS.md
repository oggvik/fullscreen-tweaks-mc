<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# JVM build groups

The JVM in these script names is the Java runtime that launches Gradle. It is distinct from the Java bytecode target of a mod jar. Gradle toolchains may compile a target with another installed JDK.

Each script selects its matching `JAVA_HOME_<version>_X64` variable first. If unset, it uses `JAVA_HOME` when that points to the right major version, then searches installed JDKs under `~/.jdks`, `~/.gradle/jdks`, and `/usr/lib/jvm`.

| Gradle JVM | Environment variable | Command | Contents |
| ---: | --- | --- | --- |
| 8 | `JAVA_HOME_8_X64` | `scripts/build-jvm-8.sh` | Forge 1.7.10, 1.8.9, and 1.12.2 |
| 17 | `JAVA_HOME_17_X64` | `scripts/build-jvm-17.sh` | Forge 1.16.5 and Babric b1.7.3 |
| 21 | `JAVA_HOME_21_X64` | `scripts/build-jvm-21.sh` | BTA 8.0.1 |
| 25 | `JAVA_HOME_25_X64` | `scripts/build-jvm-25.sh` | Stonecutter, NeoForge 1.20.1, and BTA 7.3 builds |

The JDK 8 group skips the incomplete legacy Forge ports when their project directories are absent from the checkout.

For example:

```bash
JAVA_HOME_8_X64=/path/to/jdk8 scripts/build-jvm-8.sh --no-daemon
```

Each script verifies the selected JVM before invoking Gradle and forwards all arguments to every build. Java 16 remains necessary as a compiler toolchain for Minecraft 1.17.1, but no Gradle wrapper in this repository is launched with Java 16.

Build every group in the supported order:

```bash
scripts/build-all-targets.sh --no-daemon
```

The all-target script uses the same selection for each group, so it works from a shell with all four JDKs installed without manually changing `JAVA_HOME` between builds. Set the version-specific variables when you need to override the detected paths. Running the group scripts separately is useful for local troubleshooting and parallel CI jobs because a failure is isolated to one Gradle-runtime family.
