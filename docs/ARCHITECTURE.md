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

Minecraft `26.3-snapshot-4` moved from GLFW to SDL3, after snapshot 3 stopped auto-minimizing fullscreen windows. That target is retained as a buildable template no-op.

## Version-specific hooks

- Modern Fabric, Forge, NeoForge, and Quilt builds inject into `com.mojang.blaze3d.platform.Window` after construction and fullscreen-mode changes.
- Forge 1.16.5 targets `net.minecraft.client.MainWindow`.
- Forge 1.7.10, 1.8.9, and 1.12.2 use a client-tick controller around LWJGL2's `Display` API.
- BTA 7.3 and 8.0 target `net.minecraft.client.render.window.GameWindowGLFW`.
- Babric b1.7.3 remains an experimental no-op because it does not expose the required GLFW fullscreen path.

Stonecutter conditions select the correct handle field, identifier type, GUI renderer, screen package, and button API for each generated target. Generated mixins are tied to their compiled Minecraft version.

## Settings and UI

The properties file stores only `preventAutoIconify` and `fullscreenMode`. Legacy `enabled` values migrate to the prevention setting on read. Prevention defaults to enabled; fullscreen mode defaults to borderless on Windows and native on other systems.

The settings button is attached to the main Options screen so replacements for video settings, including Sodium and Embeddium, do not remove the entry point. Its geometry helper checks visible widget bounds, tries aligned free slots, uses a compact button when necessary, and hides the control if no safe position exists.

Fabric and Quilt settings builds bundle only the matching Fabric API base and resource-loader modules. Forge and NeoForge use their built-in resource-pack support. Quilt targets from 1.21.11 onward require Quilt Loader 0.30.1-beta.2 or newer.

## Platform boundaries

GLFW native fullscreen normally changes the display mode and can iconify on focus loss. Borderless mode uses an undecorated window at desktop size, which avoids that native-fullscreen transition.

Windows Fullscreen Optimizations are operating-system presentation behavior rather than a GLFW/OpenGL window attribute. The mod does not modify executable compatibility flags. On Linux, compositor bypass and unredirect behavior belongs to the active X11 window manager or Wayland compositor, so the mod does not write desktop-environment settings.

The mod does not force focus, change pause-on-lost-focus behavior, or restore windows minimized by another program.

Useful background:

- [GLFW window guide](https://www.glfw.org/docs/latest/window.html)
- [GLFW platform compatibility notes](https://www.glfw.org/docs/latest/compat_guide.html)
- [Microsoft: Demystifying Fullscreen Optimizations](https://devblogs.microsoft.com/directx/demystifying-full-screen-optimizations/)

The borderless implementation was informed by the GPL-3.0-licensed [Borderless Fullscreen](https://github.com/Bestsoft101/Borderless-Fullscreen) project.
