<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Fullscreen Tweaks 2.0.1

Fullscreen Tweaks 2.0.1 is a compatibility and reliability release focused on
loading-window behavior, startup minimization, fullscreen transitions, and
settings-screen polish across the supported Minecraft versions and loaders.

## Highlights

- Fixed multiple startup crashes caused by changing or minimizing the native
  window before Minecraft had finished initializing its renderer.
- Fixed Windows fullscreen transitions that could restore an undecorated,
  monitor-sized, misplaced, or invisible window.
- Corrected loading-window and focus-loss behavior on the SDL3-based Minecraft
  26.3 targets.
- Improved the settings experience on Minecraft 1.21.11 and older versions.
- Made Mod Menu an optional Fabric integration rather than a required
  dependency.
- Retired the unmaintained Quilt targets and standalone Forge 1.20.6 port.

## Loading Window and Startup Reliability

- Fixed Fabric 1.15.2 and 1.16.5 startup crashes when **Minimize during
  loading** was enabled by delaying minimization until framebuffer
  initialization is safe.
- Fixed Forge 1.16.5 crashes when the loading window was set to fullscreen.
  The fullscreen transition now waits until Minecraft construction has
  completed, preventing GLFW framebuffer callbacks from reaching partially
  initialized renderer state.
- Fixed a Forge 1.17.1 startup crash caused by applying the loading-window
  state at an incompatible point in that version's window lifecycle.
- Fixed Minecraft 26.1.2 and 26.2 startup failures on Fabric and NeoForge after
  Minecraft moved its GLFW window-hint setup into the rendering-backend
  initialization path.
- Fixed the Minecraft 1.14.4 loading window not automatically returning to the
  configured regular game mode when loading finished.
- Hardened loading-mode changes and startup minimization so temporary
  fullscreen, windowed, and minimized states are restored at the correct
  lifecycle point.
- Preserved the established startup lifecycle on versions that did not require
  version-specific handling.

## Windows Fullscreen Fixes

- Fixed pre-26.2 GLFW targets restoring incorrect window geometry after
  leaving fullscreen, particularly when the game launched fullscreen and used
  a different mode during loading.
- The mod now records the real decorated window position and dimensions before
  entering managed fullscreen and explicitly restores them when returning to
  windowed mode.
- Window decorations are restored before the saved windowed rectangle is
  applied, preventing borderless or native-fullscreen state from leaking into
  normal windowed mode.
- Repeated fullscreen-policy updates no longer overwrite the saved windowed
  rectangle with monitor-sized borderless bounds.
- Applied the equivalent geometry fixes to the standalone Forge 1.16.5 and
  NeoForge 1.20.1 ports.
- Fixed a Minecraft 26.2-specific Windows issue where minimizing during
  borderless fullscreen loading could leave the game invisible while it still
  appeared focused. On 26.2 only, the final native-versus-borderless policy is
  now established before the window is minimized, and the GLFW window is
  refreshed when restored.

## Minecraft 26.3 and SDL3

- Fixed loading-window mode selection on Fabric and NeoForge so **Same as
  game**, **Windowed**, and **Fullscreen** are applied through the SDL3 window
  lifecycle.
- Fixed temporary loading modes not returning reliably to the configured game
  mode after loading.
- Used Minecraft's own exclusive-fullscreen state as the authoritative SDL3
  fullscreen selection instead of maintaining a separate low-level probe.
- Applied the focus-loss policy through SDL's native hint and explicitly
  minimized active exclusive fullscreen when **Prevent minimizing on focus
  loss** is disabled.
- Fixed startup-minimized windows failing to restore after loading.
- Disabled **Minimize during loading** on SDL Wayland because native SDL
  minimization can deadlock or strand the window there. The option remains
  available on supported SDL window systems.
- Updated labels and tooltips to use SDL-appropriate wording and to clarify
  the distinction between loading-window behavior and the regular game mode.

## Settings Screen and Tooltips

- Refined the Minecraft 1.21.11 settings screen to better match vanilla:
  corrected section spacing, heading placement, title alignment, background
  rendering, colors, and control layout.
- Improved fallback tooltips on legacy versions with clearer wording, more
  compact wrapping, and better positioning.
- Disabled the custom fallback tooltip path on Minecraft 1.21.1, where native
  tooltips already provide the correct behavior.
- Clarified wording for focus-loss minimization and post-loading window
  restoration.
- Shortened Forge 1.16.5 translations where the original labels did not fit
  the available controls.

## Loader and Version Integration

- Mod Menu is now optional. When installed, it still provides the Fullscreen
  Tweaks configuration entrypoint; when absent, the mod loads normally and
  remains configurable through its in-game settings button.
- Fixed Forge 1.19.2 and 1.19.4 build metadata warnings.
- Updated version metadata for the maintained release artifacts to 2.0.1.

## Better Than Adventure 8.0.1

- Updated the Better Than Adventure 8.0.1 port to Fullscreen Tweaks 2.0.1.
- Refined its default settings.
- Preserved BTA's own native-versus-borderless fullscreen option as the
  authoritative game setting while retaining Fullscreen Tweaks' loading-window
  controls in BTA's Video settings page.

## Removed Targets

- Removed all Quilt artifacts and checked-in Quilt port sources.
- Removed the standalone Forge 1.20.6 port. Minecraft 1.20.6 remains supported
  on Fabric and NeoForge.
- Removed the obsolete Quilt source-synchronization workflow and Quilt entries
  from grouped builds, target catalogs, and contributor documentation.

Users upgrading from 2.0.0 should select the artifact matching their exact
Minecraft version and loader. Existing configuration files remain compatible.
