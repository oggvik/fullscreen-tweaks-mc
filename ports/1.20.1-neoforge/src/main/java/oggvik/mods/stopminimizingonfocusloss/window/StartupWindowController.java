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
    private static boolean gameFullscreen;

    private StartupWindowController() {
    }

    public static boolean prepareLoading(boolean regularFullscreen) {
        startupActive = true;
        gameFullscreen = regularFullscreen;
        LoadingScreenMode mode = SettingsManager.get().getLoadingScreenMode();
        if (mode == LoadingScreenMode.WINDOWED) {
            return false;
        }
        if (mode == LoadingScreenMode.FULLSCREEN) {
            return true;
        }
        return regularFullscreen;
    }

    public static void windowCreated(Window window) {
        if (SettingsManager.get().isStartMinimized()) {
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }

    public static void minecraftReady(Window window) {
        if (startupActive && SettingsManager.get().isStartMinimized()) {
            GLFW.glfwIconifyWindow(window.getWindow());
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
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }
}
