<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss

Stop Minimizing on Focus Loss keeps Minecraft from minimizing itself when you alt-tab, click another monitor, or otherwise move focus away from the game while using native fullscreen.

It is a tiny client-side utility mod. Install the jar that matches your Minecraft version and loader, and the fullscreen window should stay open when focus changes.

What it does:

- Prevents native fullscreen Minecraft windows from auto-minimizing on focus loss.
- Works at the window-system level used by Minecraft's LWJGL/GLFW backend.
- Applies the GLFW setting when Minecraft creates the window and keeps it active across fullscreen mode changes.

What it does not do:

- It does not force Minecraft to stay focused.
- It does not disable pause-on-lost-focus behavior.
- It does not change borderless fullscreen, normal windowed mode, or operating-system window-manager policies.
- It does not restore a window that has already been minimized by something else.

Technical details:

Minecraft versions through `26.3-snapshot-3` use GLFW for their modern fullscreen window. GLFW has a `GLFW_AUTO_ICONIFY` window attribute which, by default, can minimize a native fullscreen window when it loses focus. This mod sets that attribute to `GLFW_FALSE` on Minecraft's GLFW window handle:

```java
glfwSetWindowAttrib(windowHandle, GLFW_AUTO_ICONIFY, GLFW_FALSE);
```

Minecraft `26.3-snapshot-4` replaced GLFW with SDL3, while `26.3-snapshot-3` stopped auto-minimizing fullscreen windows on focus loss. The snapshot-4 target is therefore retained as a template no-op implementation: it builds a compatible mod artifact but does not change SDL window behavior.

Different Minecraft versions expose the native window through different classes, so each target hooks the version-appropriate window class:

- GLFW-based modern Fabric, Forge, NeoForge, and Quilt builds inject into `com.mojang.blaze3d.platform.Window` after `<init>` and `setMode`.
- The Fabric `26.3-snapshot-4` build generates an empty mixin and performs no runtime window changes.
- Older and newer modern versions name the native handle field differently, usually `window` or `handle`; each version-specific build uses the correct field for that target.
- The standalone Forge `1.16.5` port injects into `net.minecraft.client.MainWindow` after `<init>` and `setMode`.
- The BTA Babric `7.3_04` and `8.0.1` ports inject into `net.minecraft.client.render.window.GameWindowGLFW` after `init` and `updateWindowState`.
- The Babric `b1.7.3` port is an experimental no-op for this behavior because vanilla b1.7.3 normally does not expose the relevant native GLFW fullscreen path.

## Supported Targets

This repository uses Java, Modstitch, and Stonecutter to build one codebase for every configured Fabric, Forge, and modern NeoForge target:

- Fabric: `1.14.4`, `1.15.2`, `1.16.5`, `1.17.1`, `1.18.2`, `1.19.2`, `1.19.4`, `1.20.1`, `1.20.6`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3-snapshot-1`, `26.3-snapshot-2`, `26.3-snapshot-3`, `26.3-snapshot-4` template no-op
- Forge: `1.17.1`, `1.18.2`, `1.19.2`, `1.19.4`, `1.20.1`
- NeoForge: `1.20.6`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`
- Quilt ports: `1.14.4`, `1.15.2`, `1.16.5`, `1.17.1`, `1.18.2`, `1.19.2`, `1.19.4`, `1.20.1`, `1.20.6`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3-snapshot-1`, `26.3-snapshot-2`, `26.3-snapshot-3`
- Standalone ports: Forge `1.16.5`, Forge `1.20.6`, NeoForge `1.20.1`, Babric `b1.7.3` experimental no-op, Babric Better Than Adventure `7.3_04` and `8.0.1`

## Build

Build every Stonecutter target:

```bash
./gradlew build
```

Build every target, including standalone ports:

```bash
scripts/build-all-targets.sh
```

Stonecutter artifacts are written to `versions/<minecraft-version>-<loader>/build/libs/`.

Standalone ports write artifacts to `ports/<port>/build/libs/`.

Copy every generated release jar into `release-jars/<mod-version>/`:

```bash
scripts/collect-release-jars.sh
```

### Build Jars

Fabric 1.14.4:

```bash
./gradlew :1.14.4-fabric:build
```

Quilt 1.14.4:

```bash
ports/1.14.4-quilt/gradlew -p ports/1.14.4-quilt build
```

Fabric 1.15.2:

```bash
./gradlew :1.15.2-fabric:build
```

Quilt 1.15.2:

```bash
ports/1.15.2-quilt/gradlew -p ports/1.15.2-quilt build
```

Fabric 1.16.5:

```bash
./gradlew :1.16.5-fabric:build
```

Quilt 1.16.5:

```bash
ports/1.16.5-quilt/gradlew -p ports/1.16.5-quilt build
```

Forge 1.16.5 port:

```bash
ports/1.16.5-forge/gradlew -p ports/1.16.5-forge build
```

Fabric 1.17.1:

```bash
./gradlew :1.17.1-fabric:build
```

Forge 1.17.1:

```bash
./gradlew :1.17.1-forge:build
```

Quilt 1.17.1:

```bash
ports/1.17.1-quilt/gradlew -p ports/1.17.1-quilt build
```

Fabric 1.18.2:

```bash
./gradlew :1.18.2-fabric:build
```

Forge 1.18.2:

```bash
./gradlew :1.18.2-forge:build
```

Quilt 1.18.2:

```bash
ports/1.18.2-quilt/gradlew -p ports/1.18.2-quilt build
```

Fabric 1.19.2:

```bash
./gradlew :1.19.2-fabric:build
```

Forge 1.19.2:

```bash
./gradlew :1.19.2-forge:build
```

Quilt 1.19.2:

```bash
ports/1.19.2-quilt/gradlew -p ports/1.19.2-quilt build
```

Fabric 1.19.4:

```bash
./gradlew :1.19.4-fabric:build
```

Forge 1.19.4:

```bash
./gradlew :1.19.4-forge:build
```

Quilt 1.19.4:

```bash
ports/1.19.4-quilt/gradlew -p ports/1.19.4-quilt build
```

Fabric 1.20.1:

```bash
./gradlew :1.20.1-fabric:build
```

Forge 1.20.1:

```bash
./gradlew :1.20.1-forge:build
```

NeoForge 1.20.1 port:

```bash
ports/1.20.1-neoforge/gradlew -p ports/1.20.1-neoforge build
```

Quilt 1.20.1:

```bash
ports/1.20.1-quilt/gradlew -p ports/1.20.1-quilt build
```

Fabric 1.20.6:

```bash
./gradlew :1.20.6-fabric:build
```

Forge 1.20.6 port:

```bash
ports/1.20.6-forge/gradlew -p ports/1.20.6-forge build
```

NeoForge 1.20.6:

```bash
./gradlew :1.20.6-neoforge:build
```

Quilt 1.20.6:

```bash
ports/1.20.6-quilt/gradlew -p ports/1.20.6-quilt build
```

Fabric 1.21.1:

```bash
./gradlew :1.21.1-fabric:build
```

NeoForge 1.21.1:

```bash
./gradlew :1.21.1-neoforge:build
```

Quilt 1.21.1:

```bash
ports/1.21.1-quilt/gradlew -p ports/1.21.1-quilt build
```

Fabric 1.21.11:

```bash
./gradlew :1.21.11-fabric:build
```

NeoForge 1.21.11:

```bash
./gradlew :1.21.11-neoforge:build
```

Quilt 1.21.11:

```bash
ports/1.21.11-quilt/gradlew -p ports/1.21.11-quilt build
```

Fabric 26.1.2:

```bash
./gradlew :26.1.2-fabric:build
```

NeoForge 26.1.2:

```bash
./gradlew :26.1.2-neoforge:build
```

Quilt 26.1.2:

```bash
ports/26.1.2-quilt/gradlew -p ports/26.1.2-quilt build
```

Fabric 26.2:

```bash
./gradlew :26.2-fabric:build
```

NeoForge 26.2:

```bash
./gradlew :26.2-neoforge:build
```

Quilt 26.2:

```bash
ports/26.2-quilt/gradlew -p ports/26.2-quilt build
```

Fabric 26.3-snapshot-1:

```bash
./gradlew :26.3-snapshot-1-fabric:build
```

Quilt 26.3-snapshot-1:

```bash
ports/26.3-snapshot-1-quilt/gradlew -p ports/26.3-snapshot-1-quilt build
```

Fabric 26.3-snapshot-2:

```bash
./gradlew :26.3-snapshot-2-fabric:build
```

Quilt 26.3-snapshot-2:

```bash
ports/26.3-snapshot-2-quilt/gradlew -p ports/26.3-snapshot-2-quilt build
```

Fabric 26.3-snapshot-3:

```bash
./gradlew :26.3-snapshot-3-fabric:build
```

Quilt 26.3-snapshot-3:

```bash
ports/26.3-snapshot-3-quilt/gradlew -p ports/26.3-snapshot-3-quilt build
```

Fabric 26.3-snapshot-4:

```bash
./gradlew :26.3-snapshot-4-fabric:build
```

Babric b1.7.3 port:

```bash
ports/b1.7.3-babric/gradlew -p ports/b1.7.3-babric build
```

Babric Better Than Adventure 7.3_04 port:

```bash
ports/bta-babric-7.3/gradlew -p ports/bta-babric-7.3 build
```

Babric Better Than Adventure 8.0.1 port:

```bash
ports/bta-babric-8.0/gradlew -p ports/bta-babric-8.0 build
```

### Run Clients

Fabric and Quilt run clients include Mod Menu as a dev-only runtime mod so the mod list can be inspected locally, except for `26.3-snapshot-*` because no compatible Mod Menu build has been published yet. Mod Menu is not declared as a dependency in the generated mod metadata and is not bundled into release jars.

Fabric 1.14.4:

```bash
./gradlew :1.14.4-fabric:runClient
```

Quilt 1.14.4:

```bash
ports/1.14.4-quilt/gradlew -p ports/1.14.4-quilt runClient
```

Fabric 1.15.2:

```bash
./gradlew :1.15.2-fabric:runClient
```

Quilt 1.15.2:

```bash
ports/1.15.2-quilt/gradlew -p ports/1.15.2-quilt runClient
```

Fabric 1.16.5:

```bash
./gradlew :1.16.5-fabric:runClient
```

Quilt 1.16.5:

```bash
ports/1.16.5-quilt/gradlew -p ports/1.16.5-quilt runClient
```

Forge 1.16.5 port:

```bash
ports/1.16.5-forge/gradlew -p ports/1.16.5-forge runClient
```

Fabric 1.17.1:

```bash
./gradlew :1.17.1-fabric:runClient
```

Forge 1.17.1:

```bash
./gradlew :1.17.1-forge:runClient
```

Quilt 1.17.1:

```bash
ports/1.17.1-quilt/gradlew -p ports/1.17.1-quilt runClient
```

Fabric 1.18.2:

```bash
./gradlew :1.18.2-fabric:runClient
```

Forge 1.18.2:

```bash
./gradlew :1.18.2-forge:runClient
```

Quilt 1.18.2:

```bash
ports/1.18.2-quilt/gradlew -p ports/1.18.2-quilt runClient
```

Fabric 1.19.2:

```bash
./gradlew :1.19.2-fabric:runClient
```

Forge 1.19.2:

```bash
./gradlew :1.19.2-forge:runClient
```

Quilt 1.19.2:

```bash
ports/1.19.2-quilt/gradlew -p ports/1.19.2-quilt runClient
```

Fabric 1.19.4:

```bash
./gradlew :1.19.4-fabric:runClient
```

Forge 1.19.4:

```bash
./gradlew :1.19.4-forge:runClient
```

Quilt 1.19.4:

```bash
ports/1.19.4-quilt/gradlew -p ports/1.19.4-quilt runClient
```

Fabric 1.20.1:

```bash
./gradlew :1.20.1-fabric:runClient
```

Forge 1.20.1:

```bash
./gradlew :1.20.1-forge:runClient
```

NeoForge 1.20.1 port:

```bash
ports/1.20.1-neoforge/gradlew -p ports/1.20.1-neoforge runClient
```

Quilt 1.20.1:

```bash
ports/1.20.1-quilt/gradlew -p ports/1.20.1-quilt runClient
```

Fabric 1.20.6:

```bash
./gradlew :1.20.6-fabric:runClient
```

Forge 1.20.6 port:

```bash
ports/1.20.6-forge/gradlew -p ports/1.20.6-forge runClient
```

NeoForge 1.20.6:

```bash
./gradlew :1.20.6-neoforge:runClient
```

Quilt 1.20.6:

```bash
ports/1.20.6-quilt/gradlew -p ports/1.20.6-quilt runClient
```

Fabric 1.21.1:

```bash
./gradlew :1.21.1-fabric:runClient
```

NeoForge 1.21.1:

```bash
./gradlew :1.21.1-neoforge:runClient
```

Quilt 1.21.1:

```bash
ports/1.21.1-quilt/gradlew -p ports/1.21.1-quilt runClient
```

Fabric 1.21.11:

```bash
./gradlew :1.21.11-fabric:runClient
```

NeoForge 1.21.11:

```bash
./gradlew :1.21.11-neoforge:runClient
```

Quilt 1.21.11:

```bash
ports/1.21.11-quilt/gradlew -p ports/1.21.11-quilt runClient
```

Fabric 26.1.2:

```bash
./gradlew :26.1.2-fabric:runClient
```

NeoForge 26.1.2:

```bash
./gradlew :26.1.2-neoforge:runClient
```

Quilt 26.1.2:

```bash
ports/26.1.2-quilt/gradlew -p ports/26.1.2-quilt runClient
```

Fabric 26.2:

```bash
./gradlew :26.2-fabric:runClient
```

NeoForge 26.2:

```bash
./gradlew :26.2-neoforge:runClient
```

Quilt 26.2:

```bash
ports/26.2-quilt/gradlew -p ports/26.2-quilt runClient
```

Fabric 26.3-snapshot-1:

```bash
./gradlew :26.3-snapshot-1-fabric:runClient
```

Quilt 26.3-snapshot-1:

```bash
ports/26.3-snapshot-1-quilt/gradlew -p ports/26.3-snapshot-1-quilt runClient
```

Fabric 26.3-snapshot-2:

```bash
./gradlew :26.3-snapshot-2-fabric:runClient
```

Quilt 26.3-snapshot-2:

```bash
ports/26.3-snapshot-2-quilt/gradlew -p ports/26.3-snapshot-2-quilt runClient
```

Fabric 26.3-snapshot-3:

```bash
./gradlew :26.3-snapshot-3-fabric:runClient
```

Quilt 26.3-snapshot-3:

```bash
ports/26.3-snapshot-3-quilt/gradlew -p ports/26.3-snapshot-3-quilt runClient
```

Fabric 26.3-snapshot-4:

```bash
./gradlew :26.3-snapshot-4-fabric:runClient
```

Babric b1.7.3 port:

```bash
ports/b1.7.3-babric/gradlew -p ports/b1.7.3-babric runClient
```

Babric Better Than Adventure 7.3_04 port:

```bash
ports/bta-babric-7.3/gradlew -p ports/bta-babric-7.3 runClient
```

Babric Better Than Adventure 8.0.1 port:

```bash
ports/bta-babric-8.0/gradlew -p ports/bta-babric-8.0 runClient
```

## Project Layout

```text
src/main/java/oggvik/mods/stopminimizingonfocusloss/
  StopMinimizingOnFocusLoss.java
  platform/PlatformEntrypoint.java

src/main/java/oggvik/mods/stopminimizingonfocusloss/mixins/
  WindowMixin.java

src/main/resources/
  stop_minimizing_on_focus_loss.mixins.json

src/main/templates/
  fabric.mod.json
  META-INF/mods.toml
  META-INF/neoforge.mods.toml

ports/1.16.5-forge/
  build.gradle
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixin/MainWindowMixin.java

ports/1.20.6-forge/
  build.gradle
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixins/WindowMixin.java

ports/1.20.1-neoforge/
  build.gradle
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixins/WindowMixin.java

ports/<minecraft-version>-quilt/
  build.gradle
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixins/WindowMixin.java
  src/main/resources/quilt.mod.json

ports/b1.7.3-babric/
  build.gradle
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixin/MinecraftMixin.java

ports/bta-babric-7.3/
  build.gradle
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixin/GameWindowGLFWMixin.java

ports/bta-babric-8.0/
  build.gradle.kts
  src/main/java/oggvik/mods/stopminimizingonfocusloss/mixin/GameWindowGLFWMixin.java
```

## Notes

- The mod is client-side only in behavior.
- The mod is Java-only. Fabric builds do not require Fabric API or Fabric Language Kotlin.
- Forge and NeoForge builds use `javafml`; KotlinForForge is not required.
- Quilt builds are standalone ports because Modstitch's Loom path is Fabric-specific and generates `fabric.mod.json`; the Quilt ports use `quilt.mod.json` and Quilt Loader.
- Quilt dependency ranges are emitted as Quilt's structured `all` version objects, not a single combined predicate string. Quilt Loader treats each string as one specifier, so combined lower-and-upper bounds must be represented structurally.
- Quilt `26.x` ports use Fabric Loom's non-remap setup because Quilt Loom expects official Mojang mappings, while these `26.x` targets are already non-obfuscated.
- The shared implementation is loader-neutral because the relevant modern fullscreen behavior lives in Minecraft's shared `Window` class and its GLFW backend. The standalone Forge 1.16.5 port targets that version's older `MainWindow` class.
- Fabric and Quilt `1.20.1` jars intentionally declare Minecraft compatibility from `1.20` through `1.20.6`; the artifact names still use `1.20.1` to identify the compile target.
- Fabric and Quilt `26.3-snapshot-*` jars declare their Minecraft dependencies as `26.3-alpha.*`, which are the normalized versions Fabric Loader reports for those snapshots.
- There is no NeoForge `1.19.4` build and below because NeoForge does not provide a `1.19.4` loader line; use the Forge `1.19.4` build instead.
- Forge 1.20.6 is a standalone port because Forge 1.20.6 publishes through the newer ForgeGradle MDK layout, while this Modstitch legacy Forge path expects the older `universal-srg` artifact.
- NeoForge 1.20.1 is a standalone port because early NeoForge 1.20.1 uses the legacy `net.neoforged:forge` artifact and Forge-compatible API packages, which do not fit Modstitch's modern NeoForge target path cleanly.
- NeoForge 1.20.1 and 1.20.6 support their exact Minecraft versions only. Other 1.20.x NeoForge loader lines need dedicated builds because early NeoForge 1.20.x changed loader/discovery behavior across patch versions.
- NeoForge 1.21.1 supports Minecraft `1.21` through `1.21.8`, where Minecraft's `Window` class still uses the older `window` field. NeoForge 1.21.11 supports Minecraft `1.21.9` through `1.21.11`, where `Window` uses the newer `handle` field.
- NeoForge `26.3-snapshot-*` builds are not included because NeoForge has not published matching `26.3` loader artifacts yet.
- Fabric `26.3-snapshot-4` is a template no-op because Minecraft's SDL3 window no longer auto-minimizes on focus loss as of `26.3-snapshot-3`.
- Babric b1.7.3 does not expose the relevant native fullscreen behavior in normal use, so that port is kept as an experimental no-op build rather than a meaningful fix target.
- BTA uses its own legacy-LWJGL3 `GameWindowGLFW` path, not the modern Minecraft `Window` class. The BTA 7.3_04 and 8.0.1 ports inject into `GameWindowGLFW` after window creation and after fullscreen/window-state changes.
- The GLFW attribute should work on Windows, Linux, and macOS where Minecraft uses GLFW fullscreen windows. Operating-system, window-manager, compositor, or exclusive-fullscreen policies can still override behavior outside Minecraft's control.
