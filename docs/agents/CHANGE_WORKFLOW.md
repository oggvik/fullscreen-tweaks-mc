<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Change workflow for coding agents

## Choose the owner

Start from the failing target and decide whether the behavior is shared or standalone. A change belongs in `src/main/` when all relevant Stonecutter targets should receive it. A legacy Forge, standalone Forge/NeoForge, Babric, or BTA-specific change belongs in that project under `ports/`.

For shared code, identify every Stonecutter constant that affects the edited region. Test at least one target where each condition is true and one where it is false. Rendering and identifier APIs frequently change at 1.16, 1.19, 1.20, 1.21, and the 26.x extractor transition.

## Implement safely

- Keep loader-only imports behind the appropriate Stonecutter branch.
- Keep client classes behind mixin/plugin client declarations or sided proxies.
- Use Minecraft’s bundled LWJGL libraries except in ports whose build already declares a compatible replacement.
- Preserve the properties-file migration behavior for legacy `enabled` settings.
- Reuse existing translation keys and shared assets where possible.
- Treat broad dependency ranges as claims of support; change them only with corresponding tests.

## Validate proportionally

| Change | Minimum validation |
| --- | --- |
| Documentation only | Link/path review and `git diff --check` |
| One standalone port | That port’s `build`; run its client for runtime behavior |
| Shared Java | Relevant unit tests plus representative affected Stonecutter builds |
| Stonecutter condition | Builds immediately on both sides of the condition |
| Shared metadata template | At least one Fabric target and each affected Forge-like template family; inspect the built descriptor |
| Quilt synchronization | Build changed Quilt ports after synchronization |
| Build scripts or target matrix | `bash -n` on scripts, coverage audit, then affected JVM group or the full matrix |

Use the commands in `docs/commands/TARGETS.md`. Use the grouped scripts in `docs/commands/JVM_BUILDS.md` for broad validation.

## Review the result

Before completion, inspect `git status --short`, `git diff --check`, and the focused diff. Generated runtime files, crash reports, IDE metadata, caches, and stale jars do not belong in the change. Report which builds and runtime scenarios passed and name any scenario that still needs manual GUI or platform-specific testing.
