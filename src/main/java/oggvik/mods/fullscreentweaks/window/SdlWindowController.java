/*? if template_noop {*/
/*// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.mixins.WindowModeAccessor;
import org.lwjgl.sdl.SDLHints;
import org.lwjgl.sdl.SDLVideo;

// Applies the runtime fullscreen policy to Minecraft's SDL window.
public final class SdlWindowController {
    private SdlWindowController() {
    }

    public static void apply(Window window) {
        boolean minimizeOnFocusLoss = shouldMinimizeOnFocusLoss(window);
        SDLHints.SDL_SetHint(
                SDLHints.SDL_HINT_VIDEO_MINIMIZE_ON_FOCUS_LOSS,
                minimizeOnFocusLoss ? "1" : "0"
        );
    }

    public static void handleFocusChanged(Window window, boolean focused) {
        if (!focused && shouldMinimizeOnFocusLoss(window) && !window.isIconified()) {
            minimize(window);
        }
    }

    private static boolean shouldMinimizeOnFocusLoss(Window window) {
        return !SettingsManager.get().isPreventAutoIconify()
                && ((WindowModeAccessor) (Object) window)
                .fullscreenTweaks$isFullscreenRequested();
    }

    public static void minimize(Window window) {
        SDLVideo.SDL_MinimizeWindow(window.handle());
    }

    public static void restore(Window window) {
        SDLVideo.SDL_RestoreWindow(window.handle());
    }
}
*//*?}*/
