<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss - Babric Better Than Adventure Port

Standalone Better Than Adventure `7.3_04` Babric build using Fabric Loom and the BTA client manifest.

Build:

```bash
./gradlew build
```

Run client:

```bash
./gradlew runClient
```

Note: this port targets BTA's legacy-LWJGL3 `GameWindowGLFW` runtime. It reapplies the GLFW tweak after the BTA window is created and after BTA changes fullscreen/window state.
