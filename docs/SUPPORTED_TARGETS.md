<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Supported targets

Choose a jar by Minecraft version and loader. The Java column is the class-file level expected by Minecraft; the build may use a newer JDK where the notes say so. The declared range is loader metadata and does not prove compatibility beyond the compiled target.

| Compiled target | Loader and build | Java | Declared Minecraft range | Runtime support |
| --- | --- | ---: | --- | --- |
| `1.7.10` | Forge standalone | 8 | `1.7.10` | LWJGL2 client-tick controller and borderless fullscreen |
| `1.8.9` | Forge standalone | 8 | `1.8.9` | LWJGL2 controller and full settings screen |
| `1.12.2` | Forge standalone | 8 | `1.12.2` | LWJGL2 client-tick controller and borderless fullscreen |
| `1.14.4` | Fabric / Quilt port | 8 | `>=1.14 <1.14.5` | GLFW controller and settings screen |
| `1.15.2` | Fabric / Quilt port | 8 | `>=1.15 <1.15.3` | GLFW controller and settings screen |
| `1.16.5` | Fabric / Quilt port | 8 | `>=1.16 <1.16.6` | GLFW controller and settings screen |
| `1.16.5` | Forge standalone | 8 | exact `1.16.5` | `MainWindow` mixin; build runs on JDK 17 |
| `1.17.1` | Fabric / Quilt port | 16 | `>=1.17 <1.17.2` | GLFW controller and settings screen |
| `1.17.1` | Forge | 16 | `[1.17,1.17.2)` | Version-specific `Window` mixins |
| `1.18.2` | Fabric / Quilt port | 17 | `>=1.18 <1.18.3` | GLFW controller and settings screen |
| `1.18.2` | Forge | 17 | `[1.18,1.18.3)` | Version-specific `Window` mixins |
| `1.19.2` | Fabric / Quilt port | 17 | `>=1.19 <1.19.3` | GLFW controller and settings screen |
| `1.19.2` | Forge | 17 | `[1.19,1.19.3)` | Version-specific `Window` mixins |
| `1.19.4` | Fabric / Quilt port | 17 | `>=1.19 <1.19.5` | GLFW controller and settings screen |
| `1.19.4` | Forge | 17 | `[1.19,1.19.5)` | Version-specific `Window` mixins |
| `1.20.1` | Fabric / Quilt port | 17 | `>=1.20 <1.20.7` | Compiled for 1.20.1; broad metadata overlaps the 1.20.6 jar |
| `1.20.1` | Forge | 17 | `[1.20,1.20.2)` | 1.20.1 `Window` and screen APIs |
| `1.20.1` | NeoForge standalone | 17 | `[1.20.1,1.20.2)` | 1.20.1 `Window` and screen APIs |
| `1.20.6` | Fabric / Quilt port | 21 | `>=1.20 <1.20.7` | Compiled for 1.20.6 with `GuiGraphics` |
| `1.20.6` | NeoForge | 21 | `[1.20.6,1.20.7)` | Version-specific `Window` mixins |
| `1.20.6` | Forge standalone | 21 | `[1.20.6,1.20.7)` | Version-specific `Window` mixins |
| `1.21.1` | Fabric / Quilt port | 21 | `>=1.21 <1.21.2` | 1.21.1 handle and screen APIs |
| `1.21.1` | NeoForge | 21 | `[1.21,1.21.9)` | Compiled for 1.21.1; broad metadata includes 1.21–1.21.8 |
| `1.21.11` | Fabric / Quilt port | 21 | `>=1.21 <1.21.12` | New identifier and window-handle mappings |
| `1.21.11` | NeoForge | 21 | `[1.21.9,1.21.12)` | Compiled for 1.21.11; metadata includes 1.21.9–1.21.11 |
| `26.1.2` | Fabric / Quilt port | 25 | `>=26.1 <26.1.3` | Extractor-era GLFW implementation |
| `26.1.2` | NeoForge | 25 | `[26.1,26.1.3)` | Extractor-era GLFW implementation |
| `26.2` | Fabric / Quilt port | 25 | `>=26.2 <26.3` | Extractor-era GLFW implementation |
| `26.2` | NeoForge | 25 | `[26.2,26.3)` | Extractor-era GLFW implementation |
| `26.3-snapshot-1`–`-3` | Fabric / Quilt ports | 25 | exact `26.3-alpha.1`–`.3` | Extractor-era GLFW implementation |
| `26.3-snapshot-4` | Fabric | 25 | exact `26.3-alpha.4` | Template no-op: Minecraft moved to SDL3 |
| `b1.7.3` | Babric standalone | 8 | exact `1.0.0-beta.7.3` | Experimental no-op; no supported GLFW hook |
| BTA `7.3_04` | Babric standalone | 8 | `*` | GLFW controller and properties config; no injected GUI |
| BTA `8.0.1` | Babric standalone | 17 | `*` | GLFW controller and properties config; build runs on JDK 21 |

Fabric and Quilt artifacts use the LWJGL/GLFW libraries shipped by Minecraft. Forge and NeoForge do the same. The BTA ports explicitly depend on their BTA-compatible LWJGL artifacts.

Broad ranges should be narrowed unless the full advertised range has been tested. Mixins, mappings, native-window fields, rendering APIs, and loader behavior can change between patch releases.
