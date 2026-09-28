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

## Reproduction profiles

The repository provides isolated `runOculusClient` profiles for Forge 1.16.5,
1.18.2, 1.19.2, 1.19.4, and 1.20.1, plus the standalone NeoForge 1.20.1 port.
Their exact commands and Gradle JVM requirements are listed in
[the target command catalog](../commands/TARGETS.md#oculus-compatibility-runs).

Runtime validation should confirm that the client reaches resource loading or
the title screen, Oculus creates its rendering pipeline, and the log contains no
fatal mixin error. A warning that Fullscreen Tweaks' optional redirect was
skipped is expected when Oculus owns the call.
