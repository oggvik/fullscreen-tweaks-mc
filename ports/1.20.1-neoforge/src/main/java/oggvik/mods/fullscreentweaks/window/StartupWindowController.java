// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.fullscreentweaks.config.LoadingScreenMode;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.mixins.WindowModeAccessor;
import oggvik.mods.fullscreentweaks.platform.MinecraftWindowBridge;
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

    public static void reapplyLoadingState(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        if (accessor.fullscreenTweaks$isFullscreen() != loadingFullscreen) {
            accessor.fullscreenTweaks$setFullscreen(loadingFullscreen);
            accessor.fullscreenTweaks$setMode();
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
        MinecraftWindowBridge.setFullscreenSetting(gameFullscreen);
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        if (accessor.fullscreenTweaks$isFullscreen() != gameFullscreen) {
            accessor.fullscreenTweaks$setFullscreen(gameFullscreen);
            accessor.fullscreenTweaks$setMode();
        }
        if (SettingsManager.get().isStartMinimized()) {
            GLFW.glfwRestoreWindow(window.getWindow());
        }
    }
}
