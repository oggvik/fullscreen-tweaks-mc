<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Repository map for coding agents

## Build families

The repository has two source models:

| Area | Role | Edit policy |
| --- | --- | --- |
| `src/main/java/` | Shared Stonecutter Java source | Edit for behavior shared by generated Fabric, Forge, and NeoForge targets |
| `src/main/templates/` | Shared loader metadata templates | Edit for generated metadata changes |
| `versions/` | Stonecutter target projects and generated build output | Edit target properties when required; never edit generated files under `build/` |
| `ports/*-quilt/` | Standalone Quilt projects with synchronized Java | Regenerate with `scripts/sync-quilt-sources.sh` after shared changes |
| Other `ports/` projects | Standalone legacy, Forge, NeoForge, and Babric implementations | Edit directly for port-specific behavior |

Root `settings.gradle.kts` declares all Stonecutter projects. Root `build.gradle.kts` owns Modstitch setup, Java targets, loader selection, metadata replacement, dependencies, and Stonecutter constants. Per-target dependency and metadata values live in `versions/*/gradle.properties`.

## Runtime structure

- `config/` code owns the two persistent settings: minimization prevention and fullscreen mode.
- `window/` or `platform/` code bridges Minecraft to GLFW or LWJGL2 and applies the selected policy.
- `mixins/` inject lifecycle and Options-screen behavior on modern targets.
- Legacy Forge ports use event subscribers and sided proxies instead of mixins.
- `client/` owns the settings screen and Options entry. Keep these classes unreachable on dedicated servers.

See `docs/ARCHITECTURE.md` for behavioral details and `docs/SUPPORTED_TARGETS.md` for each target’s runtime status.

## Automation and releases

- `scripts/build-jvm-*.sh` build disjoint Gradle-runtime groups.
- `scripts/build-all-targets.sh` invokes every JVM group.
- `scripts/sync-quilt-sources.sh` copies processed shared sources into Quilt ports.
- `scripts/collect-release-jars.sh` groups built artifacts by base mod version.
- `.github/workflows/build.yml` performs routine CI.
- `.github/workflows/publish.yml` defines the public release matrix; keep it aligned with supported targets.

Build directories and `run/` directories are disposable outputs. Never use them as the only location for a source fix.
