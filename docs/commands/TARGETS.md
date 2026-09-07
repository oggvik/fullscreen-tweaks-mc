<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Target build and run commands

Run these commands from the repository root. Every build produces a loader- and Minecraft-specific jar; never use a jar for a different row. Client runs require a graphical desktop.

## Minecraft release targets

> [!WARNING]
> **Work in progress:** The Forge 1.7.10, Forge 1.8.9, and Forge 1.12.2 ports are currently incomplete and do not work yet. Their commands are documented for development and troubleshooting only; builds or client startup may fail.

| Minecraft | Loader | Build | Run client |
| --- | --- | --- | --- |
| Beta 1.7.3 | Babric | `ports/b1.7.3-babric/gradlew -p ports/b1.7.3-babric build` | `ports/b1.7.3-babric/gradlew -p ports/b1.7.3-babric runClient` |
| 1.7.10 | Forge | `ports/1.7.10-forge/gradlew -p ports/1.7.10-forge build` | `ports/1.7.10-forge/gradlew -p ports/1.7.10-forge runClient` |
| 1.8.9 | Forge | `ports/1.8.9-forge/gradlew -p ports/1.8.9-forge build` | `ports/1.8.9-forge/gradlew -p ports/1.8.9-forge runClient` |
| 1.12.2 | Forge | `ports/1.12.2-forge/gradlew -p ports/1.12.2-forge build` | `ports/1.12.2-forge/gradlew -p ports/1.12.2-forge runClient` |
| 1.14.4 | Fabric | `./gradlew :1.14.4-fabric:build` | `./gradlew :1.14.4-fabric:runClient` |
| 1.14.4 | Quilt | `ports/1.14.4-quilt/gradlew -p ports/1.14.4-quilt build` | `ports/1.14.4-quilt/gradlew -p ports/1.14.4-quilt runClient` |
| 1.15.2 | Fabric | `./gradlew :1.15.2-fabric:build` | `./gradlew :1.15.2-fabric:runClient` |
| 1.15.2 | Quilt | `ports/1.15.2-quilt/gradlew -p ports/1.15.2-quilt build` | `ports/1.15.2-quilt/gradlew -p ports/1.15.2-quilt runClient` |
| 1.16.5 | Fabric | `./gradlew :1.16.5-fabric:build` | `./gradlew :1.16.5-fabric:runClient` |
| 1.16.5 | Forge | `ports/1.16.5-forge/gradlew -p ports/1.16.5-forge build` | `ports/1.16.5-forge/gradlew -p ports/1.16.5-forge runClient` |
| 1.16.5 | Quilt | `ports/1.16.5-quilt/gradlew -p ports/1.16.5-quilt build` | `ports/1.16.5-quilt/gradlew -p ports/1.16.5-quilt runClient` |
| 1.17.1 | Fabric | `./gradlew :1.17.1-fabric:build` | `./gradlew :1.17.1-fabric:runClient` |
| 1.17.1 | Forge | `./gradlew :1.17.1-forge:build` | `./gradlew :1.17.1-forge:runClient` |
| 1.17.1 | Quilt | `ports/1.17.1-quilt/gradlew -p ports/1.17.1-quilt build` | `ports/1.17.1-quilt/gradlew -p ports/1.17.1-quilt runClient` |
| 1.18.2 | Fabric | `./gradlew :1.18.2-fabric:build` | `./gradlew :1.18.2-fabric:runClient` |
| 1.18.2 | Forge | `./gradlew :1.18.2-forge:build` | `./gradlew :1.18.2-forge:runClient` |
| 1.18.2 | Quilt | `ports/1.18.2-quilt/gradlew -p ports/1.18.2-quilt build` | `ports/1.18.2-quilt/gradlew -p ports/1.18.2-quilt runClient` |
| 1.19.2 | Fabric | `./gradlew :1.19.2-fabric:build` | `./gradlew :1.19.2-fabric:runClient` |
| 1.19.2 | Forge | `./gradlew :1.19.2-forge:build` | `./gradlew :1.19.2-forge:runClient` |
| 1.19.2 | Quilt | `ports/1.19.2-quilt/gradlew -p ports/1.19.2-quilt build` | `ports/1.19.2-quilt/gradlew -p ports/1.19.2-quilt runClient` |
| 1.19.4 | Fabric | `./gradlew :1.19.4-fabric:build` | `./gradlew :1.19.4-fabric:runClient` |
| 1.19.4 | Forge | `./gradlew :1.19.4-forge:build` | `./gradlew :1.19.4-forge:runClient` |
| 1.19.4 | Quilt | `ports/1.19.4-quilt/gradlew -p ports/1.19.4-quilt build` | `ports/1.19.4-quilt/gradlew -p ports/1.19.4-quilt runClient` |
| 1.20.1 | Fabric | `./gradlew :1.20.1-fabric:build` | `./gradlew :1.20.1-fabric:runClient` |
| 1.20.1 | Forge | `./gradlew :1.20.1-forge:build` | `./gradlew :1.20.1-forge:runClient` |
| 1.20.1 | NeoForge | `ports/1.20.1-neoforge/gradlew -p ports/1.20.1-neoforge build` | `ports/1.20.1-neoforge/gradlew -p ports/1.20.1-neoforge runClient` |
| 1.20.1 | Quilt | `ports/1.20.1-quilt/gradlew -p ports/1.20.1-quilt build` | `ports/1.20.1-quilt/gradlew -p ports/1.20.1-quilt runClient` |
| 1.20.6 | Fabric | `./gradlew :1.20.6-fabric:build` | `./gradlew :1.20.6-fabric:runClient` |
| 1.20.6 | Forge | `ports/1.20.6-forge/gradlew -p ports/1.20.6-forge build` | `ports/1.20.6-forge/gradlew -p ports/1.20.6-forge runClient` |
| 1.20.6 | NeoForge | `./gradlew :1.20.6-neoforge:build` | `./gradlew :1.20.6-neoforge:runClient` |
| 1.20.6 | Quilt | `ports/1.20.6-quilt/gradlew -p ports/1.20.6-quilt build` | `ports/1.20.6-quilt/gradlew -p ports/1.20.6-quilt runClient` |
| 1.21.1 | Fabric | `./gradlew :1.21.1-fabric:build` | `./gradlew :1.21.1-fabric:runClient` |
| 1.21.1 | NeoForge | `./gradlew :1.21.1-neoforge:build` | `./gradlew :1.21.1-neoforge:runClient` |
| 1.21.1 | Quilt | `ports/1.21.1-quilt/gradlew -p ports/1.21.1-quilt build` | `ports/1.21.1-quilt/gradlew -p ports/1.21.1-quilt runClient` |
| 1.21.11 | Fabric | `./gradlew :1.21.11-fabric:build` | `./gradlew :1.21.11-fabric:runClient` |
| 1.21.11 | NeoForge | `./gradlew :1.21.11-neoforge:build` | `./gradlew :1.21.11-neoforge:runClient` |
| 1.21.11 | Quilt | `ports/1.21.11-quilt/gradlew -p ports/1.21.11-quilt build` | `ports/1.21.11-quilt/gradlew -p ports/1.21.11-quilt runClient` |
| 26.1.2 | Fabric | `./gradlew :26.1.2-fabric:build` | `./gradlew :26.1.2-fabric:runClient` |
| 26.1.2 | NeoForge | `./gradlew :26.1.2-neoforge:build` | `./gradlew :26.1.2-neoforge:runClient` |
| 26.1.2 | Quilt | `ports/26.1.2-quilt/gradlew -p ports/26.1.2-quilt build` | `ports/26.1.2-quilt/gradlew -p ports/26.1.2-quilt runClient` |
| 26.2 | Fabric | `./gradlew :26.2-fabric:build` | `./gradlew :26.2-fabric:runClient` |
| 26.2 | NeoForge | `./gradlew :26.2-neoforge:build` | `./gradlew :26.2-neoforge:runClient` |
| 26.2 | Quilt | `ports/26.2-quilt/gradlew -p ports/26.2-quilt build` | `ports/26.2-quilt/gradlew -p ports/26.2-quilt runClient` |
| 26.3 snapshot 1 | Fabric | `./gradlew :26.3-snapshot-1-fabric:build` | `./gradlew :26.3-snapshot-1-fabric:runClient` |
| 26.3 snapshot 1 | Quilt | `ports/26.3-snapshot-1-quilt/gradlew -p ports/26.3-snapshot-1-quilt build` | `ports/26.3-snapshot-1-quilt/gradlew -p ports/26.3-snapshot-1-quilt runClient` |
| 26.3 snapshot 2 | Fabric | `./gradlew :26.3-snapshot-2-fabric:build` | `./gradlew :26.3-snapshot-2-fabric:runClient` |
| 26.3 snapshot 2 | Quilt | `ports/26.3-snapshot-2-quilt/gradlew -p ports/26.3-snapshot-2-quilt build` | `ports/26.3-snapshot-2-quilt/gradlew -p ports/26.3-snapshot-2-quilt runClient` |
| 26.3 snapshot 3 | Fabric | `./gradlew :26.3-snapshot-3-fabric:build` | `./gradlew :26.3-snapshot-3-fabric:runClient` |
| 26.3 snapshot 3 | Quilt | `ports/26.3-snapshot-3-quilt/gradlew -p ports/26.3-snapshot-3-quilt build` | `ports/26.3-snapshot-3-quilt/gradlew -p ports/26.3-snapshot-3-quilt runClient` |
| 26.3 snapshot 4 | Fabric | `./gradlew :26.3-snapshot-4-fabric:build` | `./gradlew :26.3-snapshot-4-fabric:runClient` |

## Better Than Adventure targets

Better Than Adventure has its own release sequence, so these ports are listed separately.

| BTA | Loader | Build | Run client |
| --- | --- | --- | --- |
| 7.3_04 | Babric | `ports/bta-babric-7.3/gradlew -p ports/bta-babric-7.3 build` | `ports/bta-babric-7.3/gradlew -p ports/bta-babric-7.3 runClient` |
| 8.0.1 | Babric | `ports/bta-babric-8.0/gradlew -p ports/bta-babric-8.0 build` | `ports/bta-babric-8.0/gradlew -p ports/bta-babric-8.0 runClient` |

See [JVM build groups](JVM_BUILDS.md) when building several targets together.
