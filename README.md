<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss

[![Gradle CI](https://github.com/oggvik/stop-minimizing-on-focus-loss-mc/actions/workflows/build.yml/badge.svg)](https://github.com/oggvik/stop-minimizing-on-focus-loss-mc/actions/workflows/build.yml)
[![License: AGPL-3.0-or-later](https://img.shields.io/badge/license-AGPL--3.0--or--later-blue.svg)](LICENSE.md)

A client-side Minecraft mod that keeps fullscreen Minecraft visible when you alt-tab, click another monitor, or move focus to another application. Version 1.0.0 supports native and borderless fullscreen across a large range of Minecraft and loader versions.

Install the jar that matches both your Minecraft version and mod loader. No server installation is required.

## Features

- Prevents supported fullscreen windows from minimizing when focus is lost.
- Lets you enable or disable minimization prevention independently of fullscreen mode.
- Provides native and borderless fullscreen modes.
- Defaults to borderless on Windows and native fullscreen with auto-iconification disabled elsewhere.
- Follows the monitor containing most of the Minecraft window.
- Stores two portable settings in `config/stop-minimizing-on-focus-loss.properties`.
- Adds a **Fullscreen settings** entry to Minecraft's main Options screen on supported GUI targets.

## Configuration

Open **Options → Fullscreen settings** in Minecraft. The page controls:

- **Fullscreen:** Minecraft's normal fullscreen setting. F11 and other screens use the same value.
- **Prevent minimizing on focus loss:** enables or disables this mod's focus-loss policy.
- **Fullscreen mode:** chooses native or borderless fullscreen.

The Options entry searches for a free position without moving other mods' controls. On crowded screens it can shrink to `...` or hide until a slot becomes available.

## Compatibility

See [Supported targets](docs/SUPPORTED_TARGETS.md) for the complete build matrix, Java requirements, declared Minecraft ranges, and runtime notes. Each jar is version- and loader-specific; do not reuse a jar on another row of the matrix.

## Development

The main project uses [Stonecutter](https://stonecutter.kikugie.dev/) and [Modstitch](https://modstitch.dev/) to generate Fabric, Forge, and NeoForge targets from shared sources. Quilt and legacy/Babric builds live under `ports/` as standalone Gradle projects.

```bash
./gradlew build
scripts/build-all-targets.sh
```

Read [Building and running](docs/BUILDING.md) for prerequisites, source synchronization, and artifact locations. The [target command catalog](docs/commands/TARGETS.md) contains every build and run command in Minecraft version order. [Architecture](docs/ARCHITECTURE.md) explains the window hooks, configuration model, and platform behavior.

## Contributing

Bug reports and pull requests are welcome. Start with [CONTRIBUTING.md](CONTRIBUTING.md), which explains target selection, validation, and the generated-source workflow.

This project is licensed under the [GNU Affero General Public License v3.0 or later](LICENSE.md).
