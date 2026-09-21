// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.window;

import com.mojang.blaze3d.platform.Window;
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
    private static boolean startupActive;
    private static boolean gameFullscreen;
    private static boolean loadingFullscreen;

    private StartupWindowController() {
    }

    public static boolean prepareLoading(boolean regularFullscreen) {
        startupActive = true;
        gameFullscreen = regularFullscreen;
        LoadingScreenMode mode = SettingsManager.get().getLoadingScreenMode();
        if (mode == LoadingScreenMode.WINDOWED) {
            loadingFullscreen = false;
        } else if (mode == LoadingScreenMode.FULLSCREEN) {
            loadingFullscreen = true;
        } else {
            loadingFullscreen = regularFullscreen;
        }
        return loadingFullscreen;
    }

    public static void reapplyLoadingState(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        if (accessor.stopMinimizingOnFocusLoss$isFullscreen() != loadingFullscreen) {
            accessor.stopMinimizingOnFocusLoss$setFullscreen(loadingFullscreen);
            accessor.stopMinimizingOnFocusLoss$setMode();
        }
        if (SettingsManager.get().isStartMinimized()) {
            minimize(window);
        }
    }

    public static void finishLoading(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        startupActive = false;
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        if (accessor.stopMinimizingOnFocusLoss$isFullscreen() != gameFullscreen) {
            accessor.stopMinimizingOnFocusLoss$setFullscreen(gameFullscreen);
            accessor.stopMinimizingOnFocusLoss$setMode();
        }
        if (SettingsManager.get().isStartMinimized()) {
            restore(window);
        }
    }

    private static void minimize(Window window) {
        /*? if template_noop {*/
        /*SDLVideo.SDL_MinimizeWindow(window.handle());
        *//*?} else {*/
        GLFW.glfwIconifyWindow(handle(window));
        /*?}*/
    }

    private static void restore(Window window) {
        /*? if template_noop {*/
        /*SDLVideo.SDL_RestoreWindow(window.handle());
        *//*?} else {*/
        GLFW.glfwRestoreWindow(handle(window));
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
