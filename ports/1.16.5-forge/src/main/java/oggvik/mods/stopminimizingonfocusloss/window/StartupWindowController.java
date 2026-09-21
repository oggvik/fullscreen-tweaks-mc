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

    public static void finishLoading(MainWindow window) {
        if (!modeOverridden || window == null) {
            return;
        }
        boolean keepMinimized = SettingsManager.get().isStartMinimized()
                && GLFW.glfwGetWindowAttrib(window.getWindow(), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
        modeOverridden = false;
        ((MainWindowModeAccessor) (Object) window).stopMinimizingOnFocusLoss$setMode();
        if (keepMinimized) {
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }

    public static void minimizeIfConfigured(MainWindow window) {
        if (SettingsManager.get().isStartMinimized() && window != null) {
            GLFW.glfwIconifyWindow(window.getWindow());
        }
    }
}
