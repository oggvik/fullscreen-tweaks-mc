// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.fullscreentweaks.config.LoadingScreenMode;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.mixins.WindowModeAccessor;
import oggvik.mods.fullscreentweaks.platform.MinecraftWindowBridge;
/*? if template_noop {*/
/*import org.lwjgl.sdl.SDLVideo;
*//*?}*/

/** Applies startup-only window choices and restores Minecraft's regular mode after loading. */
public final class StartupWindowController {
    private static boolean startupActive;
    private static boolean gameFullscreen;
    private static boolean loadingFullscreen;
    private static boolean startupMinimized;
    private static boolean loadingResourcesFinished;

    private StartupWindowController() {
    }

    public static boolean prepareLoading(boolean regularFullscreen) {
        // 26.3 delegates through two Window constructors, so preserve the original game mode.
        if (!startupActive) {
            startupActive = true;
            gameFullscreen = regularFullscreen;
            startupMinimized = false;
            loadingResourcesFinished = false;
        }
        LoadingScreenMode mode = SettingsManager.get().getLoadingScreenMode();
        if (mode == LoadingScreenMode.WINDOWED) {
            loadingFullscreen = false;
        } else if (mode == LoadingScreenMode.FULLSCREEN) {
            loadingFullscreen = true;
        } else {
            loadingFullscreen = gameFullscreen;
        }
        return loadingFullscreen;
    }

    public static void reapplyLoadingState(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        reapplyLoadingMode(window);
        reapplyLoadingMinimized(window);
    }

    public static void reapplyLoadingMinimized(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        /*? if !template_noop {*/
        if (SettingsManager.get().isStartMinimized() && !loadingResourcesFinished) {
            startupMinimized = true;
            minimize(window);
        }
        /*?}*/
    }

    /** Applies the loading mode without minimizing before legacy framebuffers are initialized. */
    public static void reapplyLoadingMode(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        if (accessor.fullscreenTweaks$isFullscreenRequested() != loadingFullscreen) {
            accessor.fullscreenTweaks$setFullscreenRequested(loadingFullscreen);
            accessor.fullscreenTweaks$setMode();
        }
    }

    /*? if template_noop {*/
    /*public static long configureInitialWindowFlags(long flags) {
        if (startupActive && !loadingResourcesFinished
                && SdlWindowController.supportsStartupMinimized()
                && SettingsManager.get().isStartMinimized()) {
            startupMinimized = true;
            return flags | SDLVideo.SDL_WINDOW_MINIMIZED;
        }
        return flags;
    }
    *//*?}*/

    public static void restoreAfterLoadingResources(Window window) {
        loadingResourcesFinished = true;
        if (startupActive && window != null) {
            restoreStartupMinimized(window);
        }
    }

    public static void finishLoading(Window window) {
        if (!startupActive || window == null) {
            return;
        }
        startupActive = false;
        MinecraftWindowBridge.setFullscreenSetting(gameFullscreen);
        WindowModeAccessor accessor = (WindowModeAccessor) (Object) window;
        if (accessor.fullscreenTweaks$isFullscreenRequested() != gameFullscreen) {
            accessor.fullscreenTweaks$setFullscreenRequested(gameFullscreen);
            accessor.fullscreenTweaks$setMode();
        }
        restoreStartupMinimized(window);
    }

    private static void restoreStartupMinimized(Window window) {
        if (startupMinimized) {
            startupMinimized = false;
            restore(window);
        }
    }

    private static void minimize(Window window) {
        /*? if template_noop {*/
        /*SdlWindowController.minimize(window);
        *//*?} else {*/
        GlfwWindowController.minimize(handle(window));
        /*?}*/
    }

    private static void restore(Window window) {
        /*? if template_noop {*/
        /*SdlWindowController.restore(window);
        *//*?} else {*/
        GlfwWindowController.restore(
                handle(window),
                ((WindowModeAccessor) (Object) window)
                        .fullscreenTweaks$isFullscreenRequested());
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
