// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.window;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.stopminimizingonfocusloss.config.LoadingScreenMode;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
import oggvik.mods.stopminimizingonfocusloss.mixins.WindowModeAccessor;
import org.lwjgl.glfw.GLFW;

/** Applies startup-only window choices and restores Minecraft's regular mode after loading. */
public final class StartupWindowController {
    private static boolean startupActive;
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

    public static void beginLoading(Window window) {
        if (window == null) {
            return;
        }
        startupActive = true;
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        boolean gameFullscreen = accessor.stopMinimizingOnFocusLoss$isFullscreen();
        boolean loadingFullscreen = loadingFullscreen(gameFullscreen);
        if (loadingFullscreen != gameFullscreen) {
            accessor.stopMinimizingOnFocusLoss$setFullscreen(loadingFullscreen);
            accessor.stopMinimizingOnFocusLoss$setMode();
            accessor.stopMinimizingOnFocusLoss$setFullscreen(gameFullscreen);
            modeOverridden = true;
        }
        if (SettingsManager.get().isStartMinimized()) {
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }

    public static void finishLoading(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        startupActive = false;
        if (modeOverridden) {
            modeOverridden = false;
            ((WindowModeAccessor) (Object) window).stopMinimizingOnFocusLoss$setMode();
        }
        if (SettingsManager.get().isStartMinimized()) {
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }
}
