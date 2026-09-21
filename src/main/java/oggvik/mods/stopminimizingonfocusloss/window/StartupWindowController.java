// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.window;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.stopminimizingonfocusloss.config.FullscreenSettings;
import oggvik.mods.stopminimizingonfocusloss.config.LoadingScreenMode;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
import oggvik.mods.stopminimizingonfocusloss.mixins.WindowModeAccessor;
/*? if template_noop {*/
/*import org.lwjgl.sdl.SDLVideo;
*//*?} else {*/
import org.lwjgl.glfw.GLFW;
/*?}*/

/** Applies startup-only window choices and restores Minecraft's regular mode after loading. */
public final class StartupWindowController {
    private static boolean modeOverridden;

    private StartupWindowController() {
    }

    public static boolean loadingFullscreen(boolean gameFullscreen) {
        LoadingScreenMode mode = SettingsManager.get().getLoadingScreenMode();
        if (mode == LoadingScreenMode.WINDOWED) {
            return false;
        }
        if (mode == LoadingScreenMode.FULLSCREEN) {
            return true;
        }
        return gameFullscreen;
    }

    public static void markModeOverridden() {
        modeOverridden = true;
    }

    public static void finishLoading(Window window) {
        if (!modeOverridden || window == null) {
            return;
        }
        boolean keepMinimized = SettingsManager.get().isStartMinimized() && isMinimized(window);
        modeOverridden = false;
        ((WindowModeAccessor) (Object) window).stopMinimizingOnFocusLoss$setMode();
        if (keepMinimized) {
            minimize(window);
        }
    }

    public static void minimizeIfConfigured(Window window) {
        FullscreenSettings settings = SettingsManager.get();
        if (!settings.isStartMinimized() || window == null) {
            return;
        }
        minimize(window);
    }

    private static boolean isMinimized(Window window) {
        /*? if template_noop {*/
        /*return window.isIconified();
        *//*?} else {*/
        return GLFW.glfwGetWindowAttrib(handle(window), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
        /*?}*/
    }

    private static void minimize(Window window) {
        /*? if template_noop {*/
        /*SDLVideo.SDL_MinimizeWindow(window.handle());
        *//*?} else {*/
        GLFW.glfwIconifyWindow(handle(window));
        /*?}*/
    }

    /*? if !template_noop {*/
    private static long handle(Window window) {
        /*? if new_window_handle {*/
        /*return window.handle();
        *//*?} else {*/
        return window.getWindow();
        /*?}*/
    }
    /*?}*/
}
