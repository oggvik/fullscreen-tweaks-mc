/*? if template_noop {*/
/*// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.window;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
import org.lwjgl.sdl.SDLHints;
import org.lwjgl.sdl.SDLVideo;

// Applies the runtime fullscreen policy to Minecraft's SDL window.
public final class SdlWindowController {
    private SdlWindowController() {
    }

    public static void apply(Window window) {
        boolean minimizeOnFocusLoss = !SettingsManager.get().isPreventAutoIconify()
                && window.isExclusiveFullscreen();
        SDLHints.SDL_SetHint(
                SDLHints.SDL_HINT_VIDEO_MINIMIZE_ON_FOCUS_LOSS,
                minimizeOnFocusLoss ? "1" : "0"
        );
    }

    public static void minimize(Window window) {
        SDLVideo.SDL_MinimizeWindow(window.handle());
    }

    public static void restore(Window window) {
        SDLVideo.SDL_RestoreWindow(window.handle());
    }
}
*//*?}*/
