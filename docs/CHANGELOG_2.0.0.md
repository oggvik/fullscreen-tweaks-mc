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
* After a temporary loading-window override, the mod restores both the actual window mode and Minecraft's displayed **Fullscreen** setting.
* Loading-window preferences are applied as early as each supported loader and Minecraft version safely permits.

## Fullscreen and Focus-Loss Behavior

* Clarified that minimization prevention applies to native or exclusive fullscreen rather than borderless fullscreen, which normally remains visible by design.
* Hardened borderless fullscreen transitions and window-decoration restoration so the mod changes only window state that it owns.

## Settings Screen

* Replaced cycling buttons for multi-choice settings with radio-style controls so every available choice is visible at once.
* Added concise, choice-specific tooltips for native fullscreen, borderless fullscreen, and every loading-screen mode.
* Improved labels and section organization throughout the screen.
* Added visually distinct fallback help text to older Minecraft versions without native tooltip support.
* Matched Minecraft 26.3's vanilla section-heading style and spacing.
* Corrected the fallback placement of the **Fullscreen Settings...** entry so it aligns with the actual vanilla button columns when another mod occupies its preferred position.
* Added a clone of Minecraft 26.3's **Exclusive Fullscreen** option. It uses the same underlying vanilla setting and remains synchronized with Video Settings.

## Loader and Version Integration

* Added a clickable configuration-screen button to Fabric Mod Menu.
* Mod Menu remains entirely optional and is not required to run Fullscreen Tweaks.
* Added native configuration-screen buttons to supported Forge and NeoForge mod lists.
* Added active Fabric and NeoForge support for Minecraft 26.3.
* Added SDL3-aware fullscreen detection and focus-loss handling for Minecraft 26.3.
* On SDL3, Minecraft's own **Exclusive Fullscreen** setting now controls exclusive versus borderless behavior instead of duplicating that choice in a mod-owned setting.
* When minimization prevention is disabled on SDL3, native fullscreen can once again use the classic minimize-on-focus-loss behavior.
* Upgraded the Better Than Adventure 8.0.1 Babric port to Fullscreen Tweaks 2.0.0 with native controls in BTA's Video settings, loading-window modes, minimized loading, and configurable focus-loss behavior.
* BTA's built-in native-versus-borderless fullscreen setting remains authoritative and is not duplicated by the mod.

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

Fullscreen Tweaks 2.0.x is available for the actively supported Fabric, Forge, and NeoForge combinations listed on the project page, plus the separately maintained Better Than Adventure 8.0.1 Babric port. Other Minecraft and loader combinations remain on the legacy 0.1.x line and do not receive 2.0.x features.
