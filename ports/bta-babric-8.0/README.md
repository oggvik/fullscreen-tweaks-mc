<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Fullscreen Tweaks - BTA 8.0.1 Babric Port

Standalone Fullscreen Tweaks `2.0.0` build for Better Than Adventure `8.0.1` using Babric, Fabric Loom, and the official BTA client manifest.

This port integrates with BTA's `GameWindowGLFW` implementation while leaving BTA's built-in native-versus-borderless fullscreen setting in control. Its native Options page provides:

- Focus-loss minimization prevention for native fullscreen
- Loading-window modes that follow the game, force windowed mode, or force fullscreen
- Optional minimization throughout loading, followed by restoration of BTA's saved game-window state

Source and issue tracker: <https://github.com/oggvik/fullscreen-tweaks-mc>

Build:

```bash
./gradlew build
```

Run client:

```bash
./gradlew runClient
```
