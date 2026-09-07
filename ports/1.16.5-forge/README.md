<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss - Forge 1.16.5 Port

Standalone ForgeGradle 5 build for Minecraft Forge `1.16.5-36.2.42`.

This port exists because Forge 1.16.5 uses `net.minecraft.client.MainWindow` instead of the newer `com.mojang.blaze3d.platform.Window` class targeted by the shared Stonecutter build.

## Build

```bash
./gradlew build
```

The release jar is written to:

```text
build/libs/stop_minimizing_on_focus_loss-0.1.1+1.16.5-forge.jar
```

Run the client:

```bash
./gradlew runClient
```
