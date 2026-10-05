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
`GLFW.glfwDefaultWindowHints()`. Fullscreen Tweaks originally used the default
mixin priority of 1000 and required its redirect to find one target. The first
redirect to apply removes the invocation targeted by the second. If Fullscreen
Tweaks applies first, Oculus's required injection can fail; if Oculus applies
first, Fullscreen Tweaks' required injection can fail. Oculus's priority varies
by release: the tested 1.20.1 Oculus 1.8.0 uses priority 1010.

The development profiles must also remap the published Oculus and
Rubidium/Embeddium jars from production SRG names to the named development
runtime. Loading the published jars directly produces unrelated missing-target
errors before the actual compatibility behavior can be tested.

## Resolution

`WindowMixin` uses priority 900 so the known Oculus redirects apply first.
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

Oculus's higher-priority redirect runs first and Fullscreen Tweaks logs a warning
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

This table describes the Oculus compatibility change, separately from the
subsequent 2.0.2 version bump. That bump changes artifact names and version
metadata on maintained targets, but does not add another window compatibility
change. All 28 Stonecutter targets compile the shared `WindowMixin`, so the
priority annotation changes their mixin class even when the redirect is absent.
Lowering the priority affects the whole mixin, not just its redirect.

| Targets | Change from the Oculus work | Effect |
| --- | --- | --- |
| **Forge 1.18.2, 1.19.2, 1.19.4, 1.20.1** (4 Stonecutter targets) | `WindowMixin` priority changes from 1000 to 900; its `glfwDefaultWindowHints()` redirect becomes optional with `require = 0`. Each target also gains an isolated Oculus client run profile. | Oculus can redirect the call first. Fullscreen Tweaks then skips only its initial hint redirect, logs an expected warning, and still applies the window policy after creation and on later mode changes. This avoids the documented startup failure mode; the same mechanism may help with another higher-priority redirect. |
| **Forge 1.17.1** (1 Stonecutter target) | The same priority and optional redirect compile into the jar; no Oculus profile is configured. | Normal behavior is unchanged when the redirect applies. A higher-priority mod can own that call without causing Fullscreen Tweaks' optional injection to fail. There is no Oculus-specific benefit established for this target. |
| **Fabric 1.14.4, 1.15.2, 1.16.5, 1.17.1, 1.18.2, 1.19.2, 1.19.4, 1.20.1, 1.20.6, 1.21.1, 1.21.11** (11 Stonecutter targets) | The same priority and optional redirect compile into each jar; no Oculus profile is configured. | This can help if another mod redirects the same GLFW call at higher priority. It does not establish an Iris regression or an Iris-specific fix; combinations that already worked should behave as before when this redirect still applies. |
| **NeoForge 1.20.6, 1.21.1, 1.21.11** (3 Stonecutter targets) | The same priority and optional redirect compile into each jar; no Oculus profile is configured. | The potential benefit is limited to another mod taking the GLFW call first. Normal window behavior remains the same when there is no competing redirect. |
| **Fabric and NeoForge 26.1.2 and 26.2; Fabric 26.3 snapshots 1–3** (7 Stonecutter targets) | Only `WindowMixin` priority changes. These targets inject their initial window hints after the rendering backend sets them; they do not compile the `glfwDefaultWindowHints()` redirect. | The jar's mixin class changes, and its injections may run after other mixins that retain priority 1000. The Oculus redirect conflict cannot occur through this hook, so no Oculus-specific runtime benefit is expected. |
| **Fabric and NeoForge 26.3 release** (2 Stonecutter targets) | Only the priority of the template no-op `WindowMixin` changes; there is no GLFW redirect. | Mixin metadata differs, but the changed class has no active window hook. No runtime behavior change from this fix is expected. |
| **Standalone Forge 1.16.5 and NeoForge 1.20.1** | Their production source does not use the shared redirect and is unchanged by this fix. Each build gains an isolated Oculus client run profile and pinned test dependencies. | The profiles make the combination reproducible in development. They do not add compatibility behavior to either distributed mod jar; an update to either port solely for this fix has no runtime benefit. |
| **Standalone b1.7.3 Babric and BTA 7.3_04/8.0.1 Babric** | No production source or Oculus profile change. | No effect from the Oculus compatibility work. |

When a higher-priority mod takes the redirect on the first four groups,
Fullscreen Tweaks does not set its earliest `GLFW_AUTO_ICONIFY` hint during
native window creation.
Fullscreen Tweaks still applies the configured value to the created window.
The lower priority can also change ordering relative to other mods' mixins,
so compatibility outside the tested Oculus combinations remains a potential
benefit, not a verified fix for every mod.

## Reproduction profiles

The repository provides isolated `runOculusClient` profiles for Forge 1.16.5,
1.18.2, 1.19.2, 1.19.4, and 1.20.1, plus the standalone NeoForge 1.20.1 port.
Their exact commands and Gradle JVM requirements are listed in
[the target command catalog](../commands/TARGETS.md#oculus-compatibility-runs).

Runtime validation should confirm that the client reaches resource loading or
the title screen, Oculus creates its rendering pipeline, and the log contains no
fatal mixin error. A warning that Fullscreen Tweaks' optional redirect was
skipped is expected when Oculus owns the call.
