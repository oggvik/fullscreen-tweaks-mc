<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Architecture and platform behavior

## Window policy

Modern Minecraft versions through `26.3-snapshot-3` use GLFW. When minimization prevention is enabled, the mod sets `GLFW_AUTO_ICONIFY` to false on Minecraft's window:

```java
glfwSetWindowAttrib(windowHandle, GLFW_AUTO_ICONIFY, GLFW_FALSE);
```

Borderless mode detaches the window from native fullscreen, removes decorations, and positions it at the active monitor's desktop bounds. Native mode leaves Minecraft's selected resolution intact. Switching from borderless back to native uses the monitor's current desktop mode because GLFW requires a mode for that transition.

The controller reapplies the policy after window creation and fullscreen transitions. If Minecraft is windowed, it chooses the monitor with the greatest overlap and falls back to the primary monitor. Native handles, monitor choices, and video modes are never persisted.

Minecraft `26.3-snapshot-4` moved from GLFW to SDL3, after snapshot 3 stopped auto-minimizing fullscreen windows. The focus-loss policy remains unnecessary on that target, while its startup-window controller uses SDL3 to minimize the window.

At startup, the loading-screen setting can preserve Minecraft's regular fullscreen choice or temporarily force the native window to be windowed or fullscreen. A constructor-argument mixin replaces Minecraft's initial fullscreen value before GLFW or SDL3 creates the native window. The controller records Minecraft's fullscreen option as well as the launch-time display value because modern Minecraft synchronizes those values later in its constructor. It reapplies the temporary mode after that synchronization and after Minecraft shows the window, which covers native-window handoff from NeoForge. When Minecraft clears its startup overlay, the controller restores the regular mode and restores the window from its minimized state.

NeoForge's immediate window provider runs before ordinary mods are discovered and hands the same GLFW handle to Minecraft. A normal mod jar cannot affect creation of that earliest NeoForge window. The startup controller begins managing the handle when Minecraft constructs its `Window`, keeps it minimized through the vanilla loading overlay when requested, and makes the completed game visible in its regular mode. Reapplying minimization avoids relying on a compositor-reported iconified state, which is unavailable on Wayland.

## Version-specific hooks

- Maintained Modstitch Fabric, Forge, and NeoForge builds replace the initial `DisplayData` argument at the head of `com.mojang.blaze3d.platform.Window`'s constructor, then inject after construction and fullscreen-mode changes.
- Forge 1.16.5 uses the equivalent `ScreenSize` constructor argument on `net.minecraft.client.MainWindow`.
- Forge 1.7.10, 1.8.9, and 1.12.2 use a client-tick controller around LWJGL2's `Display` API.
- BTA 7.3 and 8.0 target `net.minecraft.client.render.window.GameWindowGLFW`.
- Babric b1.7.3 remains an experimental no-op because it does not expose the required GLFW fullscreen path.

Stonecutter conditions select the correct handle field, identifier type, GUI renderer, screen package, and button API for each generated target. Generated mixins are tied to their compiled Minecraft version.

## Settings and UI

The properties file stores `preventAutoIconify`, `fullscreenMode`, `loadingScreenMode`, and `startMinimized`. Legacy `enabled` values migrate to the prevention setting on read. Prevention defaults to enabled; fullscreen mode defaults to borderless on Windows and native on other systems. The loading screen follows the game by default and does not start minimized.

The settings button is attached to the main Options screen so replacements for video settings, including Sodium and Embeddium, do not remove the entry point. Its geometry helper checks visible widget bounds, tries aligned free slots, uses a compact button when necessary, and hides the control if no safe position exists.

Fabric and Quilt settings builds bundle only the matching Fabric API base and resource-loader modules. Forge and NeoForge use their built-in resource-pack support. Quilt targets from 1.21.11 onward require Quilt Loader 0.30.1-beta.2 or newer.

## Platform boundaries

GLFW native fullscreen normally changes the display mode and can iconify on focus loss. Borderless mode uses an undecorated window at desktop size, which avoids that native-fullscreen transition.

Startup mode selection does not call operating-system window APIs directly. GLFW handles Windows, macOS, X11, Wayland, and the Unix-like platforms supported by Minecraft's LWJGL build; SDL3 does the same for Minecraft versions that use it. Minimization is a request to that backend, so a Wayland compositor or another window manager may apply its own policy.

Windows Fullscreen Optimizations are operating-system presentation behavior rather than a GLFW/OpenGL window attribute. The mod does not modify executable compatibility flags. On Linux, compositor bypass and unredirect behavior belongs to the active X11 window manager or Wayland compositor, so the mod does not write desktop-environment settings.

The mod does not force focus, change pause-on-lost-focus behavior, or restore windows minimized by another program.

Useful background:

- [GLFW window guide](https://www.glfw.org/docs/latest/window.html)
- [GLFW platform compatibility notes](https://www.glfw.org/docs/latest/compat_guide.html)
- [Microsoft: Demystifying Fullscreen Optimizations](https://devblogs.microsoft.com/directx/demystifying-full-screen-optimizations/)

The borderless implementation was informed by the GPL-3.0-licensed [Borderless Fullscreen](https://github.com/Bestsoft101/Borderless-Fullscreen) project.
