<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Contributing

Thanks for helping improve Stop Minimizing on Focus Loss.

## Before opening an issue

Search existing issues, then include the Minecraft version, loader and loader version, operating system, Java version, mod version, and the smallest set of steps that reproduces the problem. Attach the relevant crash report or log when Minecraft fails to start.

For display bugs, describe whether Minecraft was windowed, native fullscreen, or borderless; which monitor it used; and what changed after F11 or an alt-tab.

## Making a change

1. Choose the smallest target that reproduces the issue from the [supported-target matrix](docs/SUPPORTED_TARGETS.md).
2. Edit shared code under `src/main` when the behavior applies to generated targets. Edit a project under `ports/` when the behavior is specific to that standalone port.
3. Keep version branches in Stonecutter directives and avoid copying generated code back into shared sources.
4. Build and test the affected target. For shared changes, run the complete Stonecutter test suite and synchronize Quilt sources as described in [the build guide](docs/BUILDING.md).
5. Keep unrelated formatting and generated runtime files out of the change.

Pull requests should explain the user-visible problem, the resulting behavior, and the commands used for validation. Small focused commits are easier to review and backport.

## Licensing

Contributions are accepted under the repository's [AGPL-3.0-or-later license](LICENSE.md). New project-authored source and documentation files should carry the existing SPDX header.
