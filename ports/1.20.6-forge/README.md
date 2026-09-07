<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss - Forge 1.20.6 Port

Standalone ForgeGradle 6 build for Minecraft Forge `1.20.6-50.2.8`.

This port exists because Forge 1.20.6 publishes through the newer ForgeGradle MDK layout, while the shared Modstitch legacy Forge path expects the older `universal-srg` artifact.

## Build

```bash
./gradlew build
```

The release jar is written to:

```text
build/libs/stop_minimizing_on_focus_loss-0.1.1+1.20.6-forge.jar
```

Run the client:

```bash
./gradlew runClient
```
