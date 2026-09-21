// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.window;

import net.minecraft.client.MainWindow;
import oggvik.mods.stopminimizingonfocusloss.config.LoadingScreenMode;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
import oggvik.mods.stopminimizingonfocusloss.mixin.MainWindowModeAccessor;
import org.lwjgl.glfw.GLFW;

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

    public static void reapplyLoadingState(MainWindow window) {
        if (!startupActive || window == null) {
            return;
        }
        MainWindowModeAccessor accessor = (MainWindowModeAccessor) (Object) window;
        if (accessor.stopMinimizingOnFocusLoss$isFullscreen() != loadingFullscreen) {
            accessor.stopMinimizingOnFocusLoss$setFullscreen(loadingFullscreen);
            accessor.stopMinimizingOnFocusLoss$setMode();
        }
        if (SettingsManager.get().isStartMinimized()) {
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }

    public static void finishLoading(MainWindow window) {
        if (!startupActive || window == null) {
            return;
        }
        startupActive = false;
        MainWindowModeAccessor accessor = (MainWindowModeAccessor) (Object) window;
        if (accessor.stopMinimizingOnFocusLoss$isFullscreen() != gameFullscreen) {
            accessor.stopMinimizingOnFocusLoss$setFullscreen(gameFullscreen);
            accessor.stopMinimizingOnFocusLoss$setMode();
        }
        if (SettingsManager.get().isStartMinimized()) {
            GLFW.glfwRestoreWindow(window.getWindow());
        }
    }
}
