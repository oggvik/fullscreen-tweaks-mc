// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import net.minecraft.client.MainWindow;
import oggvik.mods.fullscreentweaks.config.LoadingScreenMode;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.mixin.MainWindowModeAccessor;
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

    public static void reapplyLoadingState(MainWindow window) {
        if (!startupActive || window == null) {
            return;
        }
        MainWindowModeAccessor accessor = (MainWindowModeAccessor) (Object) window;
        if (accessor.fullscreenTweaks$isFullscreen() != loadingFullscreen) {
            accessor.fullscreenTweaks$setFullscreen(loadingFullscreen);
            accessor.fullscreenTweaks$setMode();
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
        MinecraftWindowBridge.setFullscreenSetting(gameFullscreen);
        boolean restoreBeforeFullscreenAttach =
                SettingsManager.get().isStartMinimized() && !loadingFullscreen && gameFullscreen;
        if (restoreBeforeFullscreenAttach) {
            GLFW.glfwRestoreWindow(window.getWindow());
        }
        MainWindowModeAccessor accessor = (MainWindowModeAccessor) (Object) window;
        if (accessor.fullscreenTweaks$isFullscreen() != gameFullscreen) {
            accessor.fullscreenTweaks$setFullscreen(gameFullscreen);
            accessor.fullscreenTweaks$setMode();
        }
        if (SettingsManager.get().isStartMinimized() && !restoreBeforeFullscreenAttach) {
            GLFW.glfwRestoreWindow(window.getWindow());
        }
    }
}
