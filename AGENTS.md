<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Repository instructions for coding agents

These instructions apply to the whole repository.

## Start here

Before making a cross-version change, read:

- `docs/agents/REPOSITORY_MAP.md` for source ownership and generated files.
- `docs/agents/CHANGE_WORKFLOW.md` for implementation and validation rules.
- `docs/commands/TARGETS.md` for exact target commands.
- `docs/SUPPORTED_TARGETS.md` for the compatibility contract.

Inspect the worktree before editing. Preserve user changes and keep unrelated changes out of the patch.

## Source ownership

- Treat `src/main/` as the source of truth for Stonecutter targets under `versions/`.
- Do not edit `versions/*/build/`, `build/`, `run/`, `.gradle/`, or generated IDE files.
- Projects under `ports/` are standalone. Edit a port directly when behavior is specific to it.
- Quilt ports contain checked-in synchronized sources. After a shared change, build the Stonecutter targets and run `scripts/sync-quilt-sources.sh`; review the resulting Quilt diffs.
- Keep version differences inside existing Stonecutter directives. Do not replace conditional source with copied per-version implementations unless the architecture requires a standalone port.

## Build and runtime rules

- Use the repository Gradle wrappers. Do not use a system Gradle installation.
- Use `scripts/build-jvm-*.sh` for grouped builds. Their number is the JVM that launches Gradle, not necessarily the jar bytecode version.
- Run the smallest relevant build first. Run broader checks after shared build logic, shared Java, templates, or synchronized sources change.
- A successful build does not prove a reported runtime bug is fixed. For startup, mixin, rendering, or window behavior, run the affected client when the environment permits and inspect its log.
- Never copy a built jar or mixin between Minecraft versions. Window classes, mappings, handles, GUI APIs, and loader metadata are target-specific.

## Metadata and releases

- The release version comes from root `gradle.properties` for Stonecutter targets and the corresponding standalone port property or build script under `ports/`.
- Preserve the `+<minecraft>-<loader>` artifact suffix. The base mod version is shared.
- Keep homepage, issue, and source metadata aligned across Fabric, Quilt, Forge, NeoForge, Babric, and legacy `mcmod.info` descriptors.
- Do not broaden declared Minecraft ranges without runtime evidence for every newly claimed version.
- Do not publish, tag, push, or modify a release unless the user explicitly asks.

## Code and documentation

- Maintain Java compatibility for the target. Avoid newer language or library APIs in Java 8 and Java 16 sources.
- Preserve the client-only boundary: dedicated-server class loading must not resolve Minecraft client, GLFW, or LWJGL display classes.
- New project-authored source, scripts, and documentation must use the repository SPDX header and AGPL-3.0-or-later identifier.
- Update `docs/commands/TARGETS.md`, `docs/SUPPORTED_TARGETS.md`, publishing configuration, and JVM group scripts when targets are added, removed, or renamed.
- Prefer focused comments that explain a compatibility constraint or non-obvious version boundary.

## Completion checks

Before handing off a change:

1. Run `git diff --check`.
2. Build and test every affected target.
3. For shared-source or shared-template changes, build representative targets on both sides of each changed Stonecutter condition.
4. For build orchestration changes, verify script syntax and target coverage.
5. Confirm only intended files are modified and summarize any runtime validation that was not possible.
