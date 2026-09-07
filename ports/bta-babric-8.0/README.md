<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss - BTA 8.0.1 Babric Port

Standalone Better Than Adventure `8.0.1` Babric build using Fabric Loom and the official BTA 8.0.1 client manifest.

The client mixin targets BTA's `GameWindowGLFW` implementation. It disables GLFW auto-iconify after the window is created and reapplies the attribute after fullscreen or window-state changes.

Build:

```bash
./gradlew build
```

Run client:

```bash
./gradlew runClient
```
