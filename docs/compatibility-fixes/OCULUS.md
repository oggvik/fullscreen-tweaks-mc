<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Oculus compatibility

## Symptom

Minecraft can crash during startup when Fullscreen Tweaks and Oculus are both
installed. The relevant log message reports an `@Redirect conflict` in
`com.mojang.blaze3d.platform.Window`, followed by a critical injection failure
for Oculus's `MixinWindow.iris$enableDebugContext` handler.

The failure was reproduced with Minecraft 1.19.2, Forge 43.5.2, Oculus 1.6.9a,
and Rubidium 0.6.2c. The same window hook exists across several Oculus releases,
so the compatibility rule applies to the shared GLFW targets rather than only
that dependency combination.

## Cause

Fullscreen Tweaks and Oculus both redirect the constructor call to
`GLFW.glfwDefaultWindowHints()`. Both mixins originally used the default priority
of 1000. Whichever redirect applied first removed the invocation targeted by the
other redirect. When Fullscreen Tweaks won, Oculus found zero targets for a
required injection and aborted class transformation.

The development profiles must also remap the published Oculus and
Rubidium/Embeddium jars from production SRG names to the named development
runtime. Loading the published jars directly produces unrelated missing-target
errors before the actual compatibility behavior can be tested.

## Resolution

`WindowMixin` uses priority 900 so Oculus's priority-1000 redirect applies first.
The Fullscreen Tweaks redirect uses `require = 0`, allowing it to be skipped when
another mod already owns that invocation. Fullscreen Tweaks still applies its
window policy after creation and during later mode changes.

Without Oculus, the invocation remains available and Fullscreen Tweaks installs
its initial `GLFW_AUTO_ICONIFY` hint as before. With Oculus, Oculus controls the
initial default-window-hint call and Fullscreen Tweaks yields that one startup
hook to preserve both mods' remaining behavior.

## Consequences

### Without Oculus

Fullscreen Tweaks still redirects `glfwDefaultWindowHints()` and immediately
adds its `GLFW_AUTO_ICONIFY` hint. Making the redirect optional does not change
the successful-injection path. Priority 900 only places `WindowMixin` after
higher-priority mixins; in an otherwise unchanged installation, the resulting
window behavior is the same as before this fix.

The optional redirect also avoids a Fullscreen Tweaks startup failure if another
higher-priority mod removes the invocation. In that case, the other mod owns the
initial GLFW hints. Fullscreen Tweaks continues to apply the configured window
policy after window creation and on later mode changes.

### With Oculus

Oculus's priority-1000 redirect runs first and Fullscreen Tweaks logs a warning
that its priority-900 redirect was skipped. This warning is expected and is not
a partial mixin failure. Oculus can install its OpenGL debug-context hints and
startup continues.

Fullscreen Tweaks does not set `GLFW_AUTO_ICONIFY` during the initial hint phase
in this combination. It applies the configured auto-iconify state to the window
after creation, so normal gameplay and later fullscreen transitions remain
managed. The practical difference is limited to the interval while the native
window is being created.

#### Loading-screen controls remain available

Installing Oculus does not disable the loading-screen settings:

- **Loading screen window** is selected by changing the `DisplayData` constructor
  argument and is reapplied to the created window. It does not depend on the
  skipped GLFW redirect.
- **Minimize while loading** minimizes the created window through
  `StartupWindowController`. It also does not depend on the skipped redirect.
- **Prevent native fullscreen from minimizing on focus loss** misses only its
  earliest pre-creation GLFW hint. The same value is applied as a window
  attribute immediately after creation and during later mode changes.

The settings therefore remain functional and their controls should stay active
in the UI. This differs from the SDL-on-Wayland case: that backend deliberately
does not perform startup minimization because the native operation can leave the
game window permanently stuck. There is no equivalent unavailable capability
when Oculus is installed, so showing an unavailable value or disabling a button
would incorrectly describe the runtime behavior.

### Target and binary scope

The compatibility source change is compiled into every Stonecutter target
because all of them share `WindowMixin`. Consequently, rebuilding any
Stonecutter target produces a binary-different mixin class due to the priority
annotation, even where the conflicting redirect is not compiled in.

| Targets | Compiled behavior | Expected runtime consequence |
| --- | --- | --- |
| Minecraft 1.14.4 through 1.21.11 Stonecutter targets | Lower mixin priority and optional GLFW redirect | Behavior is unchanged without a competing redirect; Oculus or another higher-priority owner can take the initial hint call. |
| Minecraft 26.1.2, 26.2, and 26.3 snapshot Stonecutter targets | Lower mixin priority; these targets use the render-extractor window-hint injection instead of the redirect | Binary changes because the annotation changes, but this Oculus redirect conflict does not apply. No runtime behavior change is expected because the relevant injections target different calls. |
| Minecraft 26.3 release Stonecutter targets | Lower priority on the no-op template mixin | Binary metadata changes, but the no-op mixin has no window hook, so no runtime behavior changes. |
| Standalone projects under `ports/` | No source change from this compatibility fix | Their production mod binaries are unaffected. The added Oculus run profiles affect development runs only. |

The tested Oculus profiles cover the released Forge targets where Oculus is
available. The priority change is intentionally shared so Fabric builds keep the
same cooperative behavior if another mod redirects the same GLFW call.

## Reproduction profiles

The repository provides isolated `runOculusClient` profiles for Forge 1.16.5,
1.18.2, 1.19.2, 1.19.4, and 1.20.1, plus the standalone NeoForge 1.20.1 port.
Their exact commands and Gradle JVM requirements are listed in
[the target command catalog](../commands/TARGETS.md#oculus-compatibility-runs).

Runtime validation should confirm that the client reaches resource loading or
the title screen, Oculus creates its rendering pipeline, and the log contains no
fatal mixin error. A warning that Fullscreen Tweaks' optional redirect was
skipped is expected when Oculus owns the call.
