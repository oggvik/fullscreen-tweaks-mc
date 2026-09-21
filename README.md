<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Fullscreen Tweaks

[![Gradle CI](https://github.com/oggvik/stop-minimizing-on-focus-loss-mc/actions/workflows/build.yml/badge.svg)](https://github.com/oggvik/stop-minimizing-on-focus-loss-mc/actions/workflows/build.yml)
[![License: AGPL-3.0-or-later](https://img.shields.io/badge/license-AGPL--3.0--or--later-blue.svg)](LICENSE.md)

A client-side Minecraft mod for controlling fullscreen style, focus-loss behavior, and the loading window independently from the rest of the game.

Install the jar that matches both your Minecraft version and mod loader. No server installation is required.

Fullscreen Tweaks was previously named **Stop Minimizing on Focus Loss**. The mod ID, configuration filename, and package namespace remain unchanged so existing installations and settings continue to work.

## Features

- Keeps native fullscreen visible when Minecraft loses focus, such as while alt-tabbing, clicking another monitor, or using another application. (**Added in 0.1.0**)
- Reapplies the focus-loss policy after Minecraft creates its window and whenever it changes window mode, so the behavior remains reliable across fullscreen transitions. (**Added in 0.1.0**)
- Supports a wide range of Minecraft versions and loaders, including Fabric, Forge, NeoForge, Quilt, Babric, and Better Than Adventure targets. (**Added in 0.1.1**)
- Provides a dedicated **Fullscreen settings** screen directly from Minecraft's main Options screen on supported GUI targets. (**Added in 1.0.0**)
- Lets minimization prevention be enabled or disabled independently from the selected fullscreen style. (**Added in 1.0.0**)
- Offers both Minecraft's native fullscreen mode and a borderless fullscreen mode. (**Added in 1.0.0**)
- Makes borderless fullscreen fill the monitor containing most of the Minecraft window, with a safe fallback to the primary monitor. (**Added in 1.0.0**)
- Uses practical platform defaults: borderless fullscreen on Windows and native fullscreen with minimization prevention elsewhere. (**Added in 1.0.0**)
- Stores portable settings in `config/stop-minimizing-on-focus-loss.properties` and migrates the original enable/disable setting automatically. (**Added in 1.0.0**)
- Lets the loading screen follow the game's saved fullscreen setting or independently use windowed or fullscreen mode. (**Added in 1.1.0**)
- Can keep Minecraft minimized throughout loading, then restore and show it in the regular game window mode when loading finishes. (**Added in 1.1.0**)
- Restores both the actual window mode and Minecraft's displayed fullscreen setting after temporary loading-window overrides. (**Added in 1.1.0**)
- Presents multi-choice settings as radio-style controls, with concise labels and an individual tooltip for every setting and choice. (**Added in 1.1.0**)
- Supports Minecraft's newer SDL3 window backend, including optional classic minimize-on-focus-loss behavior for native fullscreen. (**Added in 1.1.0**)

## Configuration

Open **Options → Fullscreen settings** in Minecraft. The page controls:

- **Fullscreen:** Minecraft's normal fullscreen setting. F11 and other screens use the same value.
- **Prevent native fullscreen minimization:** controls focus-loss minimization for native fullscreen; borderless fullscreen normally remains visible regardless.
- **Fullscreen mode:** chooses native or borderless fullscreen.
- **Loading screen:** follows the game's fullscreen setting or temporarily forces the loading screen to be windowed or fullscreen.
- **Start loading screen minimized:** keeps Minecraft minimized in the operating system's taskbar or dock throughout loading, then shows it in the regular game window mode when loading finishes.

The Options entry searches for a free position without moving other mods' controls. On crowded screens it can shrink to `...` or hide until a slot becomes available.

Fullscreen style and loading-screen mode use radio-style choices so every available mode is visible at once. Hover any setting for a short explanation; older Minecraft versions show the same contextual help beneath the screen title.

## Compatibility

See [Supported targets](docs/SUPPORTED_TARGETS.md) for the complete build matrix, Java requirements, declared Minecraft ranges, and runtime notes. Each jar is version- and loader-specific; do not reuse a jar on another row of the matrix.

The loading-window override is applied before Minecraft creates or adopts its native window and uses Minecraft's GLFW or SDL3 backend. This keeps the same code path portable across Windows, macOS, Linux on X11 or Wayland, and other Unix-like systems supported by the game's native libraries. Window managers and Wayland compositors may still decide how a minimize request is presented.

NeoForge's earliest loading window is created before regular mods are discovered, so a mod installed in the `mods` directory cannot control that first stage. Once NeoForge hands its native window to Minecraft, this mod applies the selected loading mode and minimized state through the vanilla loading screen, then restores the configured game mode.

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
