<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss

[![Gradle CI](https://github.com/oggvik/stop-minimizing-on-focus-loss-mc/actions/workflows/build.yml/badge.svg)](https://github.com/oggvik/stop-minimizing-on-focus-loss-mc/actions/workflows/build.yml)
[![License: AGPL-3.0-or-later](https://img.shields.io/badge/license-AGPL--3.0--or--later-blue.svg)](LICENSE.md)

A client-side Minecraft mod that keeps fullscreen Minecraft visible when you alt-tab, click another monitor, or move focus to another application. Version 1.1.0 also lets the loading screen use a window mode and minimized state independently from the rest of the game.

Install the jar that matches both your Minecraft version and mod loader. No server installation is required.

## Features

- Prevents supported fullscreen windows from minimizing when focus is lost.
- Lets you enable or disable minimization prevention independently of fullscreen mode.
- Provides native and borderless fullscreen modes.
- Lets the loading screen follow the game, stay windowed, or use fullscreen.
- Can start the loading screen minimized and restore the selected game window mode when loading finishes.
- Defaults to borderless on Windows and native fullscreen with auto-iconification disabled elsewhere.
- Follows the monitor containing most of the Minecraft window.
- Stores four portable settings in `config/stop-minimizing-on-focus-loss.properties`.
- Adds a **Fullscreen settings** entry to Minecraft's main Options screen on supported GUI targets.

## Configuration

Open **Options → Fullscreen settings** in Minecraft. The page controls:

- **Fullscreen:** Minecraft's normal fullscreen setting. F11 and other screens use the same value.
- **Prevent minimizing on focus loss:** enables or disables this mod's focus-loss policy.
- **Fullscreen mode:** chooses native or borderless fullscreen.
- **Loading screen:** follows the game's fullscreen setting or temporarily forces the loading screen to be windowed or fullscreen.
- **Start loading screen minimized:** starts Minecraft minimized in the operating system's taskbar or dock and keeps it minimized while its regular mode is restored after loading.

The Options entry searches for a free position without moving other mods' controls. On crowded screens it can shrink to `...` or hide until a slot becomes available.

## Compatibility

See [Supported targets](docs/SUPPORTED_TARGETS.md) for the complete build matrix, Java requirements, declared Minecraft ranges, and runtime notes. Each jar is version- and loader-specific; do not reuse a jar on another row of the matrix.

The loading-window override is applied before Minecraft creates its native window and uses Minecraft's GLFW or SDL3 backend. This keeps the same code path portable across Windows, macOS, Linux on X11 or Wayland, and other Unix-like systems supported by the game's native libraries. Window managers and Wayland compositors may still decide how a minimize request is presented.

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
