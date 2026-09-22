<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Fullscreen Tweaks 2.0.0

Version 2.0.0 is a major expansion of the mod formerly known as **Stop Minimizing on Focus Loss**. The mod now manages fullscreen style, focus-loss behavior, and the loading window through a dedicated settings screen across its actively supported Minecraft versions.

## Highlights

* Renamed the mod to **Fullscreen Tweaks** to reflect its expanded scope.
* Added independent loading-screen window controls.
* Redesigned the settings screen with clearer labels, radio-style choices, and tooltips.
* Added support for Minecraft's SDL3 window backend.
* Added direct configuration buttons to Fabric Mod Menu and Forge/NeoForge mod lists.
* Added Polish and Norwegian Bokmål translations.

## Loading Window Controls

* Added a **Loading screen** setting with three modes:
  * **Same as game** follows Minecraft's saved fullscreen setting.
  * **Windowed** keeps the loading screen windowed, then switches to the configured game mode when loading finishes.
  * **Fullscreen** keeps the loading screen fullscreen regardless of the saved game setting.
* Added **Start loading screen minimized**, which keeps Minecraft minimized throughout loading and restores it when the regular game window is ready.
* Minecraft's actual window mode and displayed **Fullscreen** setting are now restored correctly after a temporary loading-window override.
* Loading-window preferences are applied as early as each supported loader and Minecraft version safely permits.

## Fullscreen and Focus-Loss Behavior

* Retained independent control over native fullscreen minimization prevention.
* Clarified that minimization prevention applies to native or exclusive fullscreen rather than borderless fullscreen, which normally remains visible by design.
* Improved borderless fullscreen state handling and monitor selection.
* Borderless mode continues to use the monitor containing most of the Minecraft window, with a safe fallback to the primary monitor.
* Windows continues to default to borderless fullscreen. Other platforms continue to default to native fullscreen with automatic minimization disabled.
* Improved restoration of window decorations when leaving borderless fullscreen.
* Hardened fullscreen transitions so the mod changes only window state that it owns.

## Settings Screen

* Replaced cycling buttons for multi-choice settings with radio-style controls so every available choice is visible at once.
* Added concise, choice-specific tooltips for native fullscreen, borderless fullscreen, and every loading-screen mode.
* Improved labels and section organization throughout the screen.
* Added visually distinct fallback help text to older Minecraft versions without native tooltip support.
* Matched Minecraft 26.3's vanilla section-heading style and spacing.
* Improved placement of the **Fullscreen Settings...** button in Minecraft's Options screen.
* The Options-screen entry now detects occupied positions from other mods and aligns itself with the actual vanilla button columns when moved.
* Added a clone of Minecraft 26.3's **Exclusive Fullscreen** option. It uses the same underlying vanilla setting and remains synchronized with Video Settings.

## Loader and Version Integration

* Added a clickable configuration-screen button to Fabric Mod Menu.
* Mod Menu remains entirely optional and is not required to run Fullscreen Tweaks.
* Added native configuration-screen buttons to supported Forge and NeoForge mod lists.
* Added active Fabric and NeoForge support for Minecraft 26.3.
* Added SDL3-aware fullscreen detection and focus-loss handling for Minecraft 26.3.
* On SDL3, Minecraft's own **Exclusive Fullscreen** setting now controls exclusive versus borderless behavior instead of duplicating that choice in a mod-owned setting.
* When minimization prevention is disabled on SDL3, native fullscreen can once again use the classic minimize-on-focus-loss behavior.

## Fixes

* Fixed Minecraft's **Fullscreen: ON/OFF** control incorrectly showing `OFF` after a windowed loading screen switched into fullscreen gameplay.
* Fixed a Forge 1.16.5 startup crash caused by applying loading-window state from an unsafe constructor injection point.
* Fixed relocated Options-screen buttons receiving a slight horizontal offset when another mod occupied the preferred position.
* Fixed SDL exclusive-fullscreen detection by using the native SDL window state directly.
* Improved synchronization between temporary loading modes, saved options, and the real window state.
* Replaced deprecated NeoForge 26.3 logo metadata with the current icon metadata.

## Localization

* Added complete Polish translations.
* Added complete Norwegian Bokmål translations.
* Corrected capitalization and wording to follow each language's Minecraft UI conventions.

## Upgrade Notes

* The mod ID is now `fullscreen_tweaks`.
* The artifact name now begins with `fullscreen_tweaks-`.
* The configuration file is now `config/fullscreen-tweaks.properties`.
* Existing settings from `config/stop-minimizing-on-focus-loss.properties` are migrated automatically.
* Remove the old **Stop Minimizing on Focus Loss** jar before installing Fullscreen Tweaks 2.0.0. Keeping both jars installed may make the loader treat them as separate mods.
* Modpacks, dependency declarations, scripts, and update tools that refer to the old mod ID must be updated.
* Fullscreen Tweaks remains client-side only and is not required on dedicated servers.

## Choosing a Download

Every uploaded jar is specific to one Minecraft version and loader. Install only the file whose `+<minecraft>-<loader>` suffix matches your game exactly.

Fullscreen Tweaks 2.0.x is available for the actively supported Fabric, Forge, and NeoForge combinations listed on the project page. Other Minecraft and loader combinations remain on the legacy 0.1.x line and do not receive 2.0.x features. Better Than Adventure remains separately supported and will receive compatibility updates when needed.
