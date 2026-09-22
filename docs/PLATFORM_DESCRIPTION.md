<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Fullscreen Tweaks

[![License: AGPL-3.0-or-later](https://img.shields.io/badge/license-AGPL--3.0--or--later-blue.svg)](https://github.com/oggvik/fullscreen-tweaks-mc/blob/main/LICENSE.md)

A client-side Minecraft mod for controlling fullscreen style, focus-loss behavior, and the loading window independently from the rest of the game. Version 2.0.0 supports native, borderless, and SDL3 fullscreen behavior across a large range of Minecraft and loader versions.

Install the jar that matches both your Minecraft version and mod loader. No server installation is required.

Fullscreen Tweaks was previously known as **Stop Minimizing on Focus Loss** before its feature set and scope grew beyond the original single-purpose design. Existing settings are migrated automatically after the rename.

## Features

* Keeps native fullscreen visible when Minecraft loses focus, such as while alt-tabbing, clicking another monitor, or using another application. (**Added in 0.1.0**)
* Reapplies the focus-loss policy after Minecraft creates its window and whenever it changes window mode, so the behavior remains reliable across fullscreen transitions. (**Added in 0.1.0**)
* Supports a wide range of Minecraft versions and loaders, including Fabric, Forge, NeoForge, Quilt, Babric, and Better Than Adventure targets. (**Added in 0.1.1**)
* Provides a dedicated **Fullscreen Settings** screen directly from Minecraft's main Options screen on supported GUI targets. (**Added in 1.0.0**)
* Lets minimization prevention be enabled or disabled independently from the selected fullscreen style. (**Added in 1.0.0**)
* Offers both Minecraft's native fullscreen mode and a borderless fullscreen mode where vanilla lacks that choice. Minecraft 26.3 and BTA 8.0.1 use their own built-in fullscreen-style settings instead. (**Added in 1.0.0**)
* Makes borderless fullscreen fill the monitor containing most of the Minecraft window, with a safe fallback to the primary monitor. (**Added in 1.0.0**)
* Uses practical platform defaults: borderless fullscreen on Windows and native fullscreen with minimization prevention elsewhere. (**Added in 1.0.0**)
* Stores portable settings in `config/fullscreen-tweaks.properties`, automatically migrating both the previous filename and the original enable/disable setting. (**Added in 1.0.0**)
* Lets the loading screen follow the game's saved fullscreen setting or independently use windowed or fullscreen mode. (**Added in 2.0.0**)
* Can keep Minecraft minimized throughout loading, then restore and show it in the regular game window mode when loading finishes. (**Added in 2.0.0**)
* Restores both the actual window mode and Minecraft's displayed fullscreen setting after temporary loading-window overrides. (**Added in 2.0.0**)
* Presents multi-choice settings as radio-style controls, with concise labels and an individual tooltip for every setting and choice. (**Added in 2.0.0**)
* Supports Minecraft's newer SDL3 window backend, including optional classic minimize-on-focus-loss behavior for native fullscreen. (**Added in 2.0.0**)

## Configuration

Open **Options → Fullscreen Settings** in Minecraft. The page controls:

* **Fullscreen:** Minecraft's normal fullscreen setting. F11 and other screens use the same value.
* **Prevent native fullscreen minimization:** controls focus-loss minimization for native fullscreen; borderless fullscreen normally remains visible regardless.
* **Fullscreen mode:** chooses native or borderless fullscreen on GLFW versions. Minecraft 26.3 and newer use the equivalent vanilla exclusive-fullscreen setting.
* **Loading screen:** follows the game's fullscreen setting or temporarily forces the loading screen to be windowed or fullscreen.
* **Start loading screen minimized:** keeps Minecraft minimized in the operating system's taskbar or dock throughout loading, then shows it in the regular game window mode when loading finishes.

**[SCREENSHOT PLACEHOLDER: Fullscreen Tweaks settings screen]**

The Options entry searches for a free position without moving other mods' controls. On crowded screens it can shrink to `...` or hide until a slot becomes available.

## Version Support

Fullscreen Tweaks 2.0.x is actively supported only on the exact Minecraft and loader combinations listed below. Each jar is built for one specific combination; do not use a jar with a different Minecraft version or loader.

| Minecraft version | Fabric | Forge | NeoForge | Support notes |
| --- | --- | --- | --- | --- |
| 1.14.4 | **2.0.x** | Not supported | Not supported | Fabric target for Minecraft 1.14.4 |
| 1.15.2 | **2.0.x** | Not supported | Not supported | Fabric target for Minecraft 1.15.2 |
| 1.16.5 | **2.0.x** | **2.0.x** | Not supported | Fabric and standalone Forge targets |
| 1.17.1 | **2.0.x** | **2.0.x** | Not supported | Fabric and Forge targets |
| 1.18.2 | **2.0.x** | **2.0.x** | Not supported | Fabric and Forge targets |
| 1.19.2 | **2.0.x** | **2.0.x** | Not supported | Fabric and Forge targets |
| 1.19.4 | **2.0.x** | **2.0.x** | Not supported | Fabric and Forge targets |
| 1.20.1 | **2.0.x** | **2.0.x** | **2.0.x** | Fabric, Forge, and standalone NeoForge targets |
| 1.20.6 | **2.0.x** | Not supported | **2.0.x** | Fabric and NeoForge targets |
| 1.21.1 | **2.0.x** | Not supported | **2.0.x** | Fabric and NeoForge targets |
| 1.21.11 | **2.0.x** | Not supported | **2.0.x** | Fabric and NeoForge targets |
| 26.1.2 | **2.0.x** | Not supported | **2.0.x** | Fabric and NeoForge targets |
| 26.2 | **2.0.x** | Not supported | **2.0.x** | Fabric and NeoForge targets |
| 26.3 Snapshot 1 (`26.3-alpha.1`) | **2.0.x** | Not supported | Not supported | Exact Fabric snapshot target |
| 26.3 Snapshot 2 (`26.3-alpha.2`) | **2.0.x** | Not supported | Not supported | Exact Fabric snapshot target |
| 26.3 Snapshot 3 (`26.3-alpha.3`) | **2.0.x** | Not supported | Not supported | Exact Fabric snapshot target |
| 26.3 | **2.0.x** | Not supported | **2.0.x** | Fabric and NeoForge SDL3 targets |

All other Minecraft and loader combinations are legacy-only and are not supported beyond the 0.1.x release line. This includes the Quilt builds, the vanilla Beta 1.7.3 Babric build, Forge 1.7.10, Forge 1.8.9, Forge 1.12.2, and Forge 1.20.6. Existing 0.1.x files may remain available for those combinations, but they do not receive Fullscreen Tweaks 2.0.x features or ongoing compatibility updates.

Better Than Adventure is the exception to that legacy policy. Its Babric targets remain supported independently of the Minecraft 2.0.x matrix and will receive updates whenever needed to support current Better Than Adventure releases.

| Better Than Adventure version | Loader | Support status |
| --- | --- | --- |
| 7.3_04 | Babric | Supported legacy release |
| 8.0.1 | Babric | **2.0.x** current release with focus-loss and loading-window controls |
| Future supported BTA releases | Babric | Compatibility updates will be added as needed |
