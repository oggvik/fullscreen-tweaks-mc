// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.window.GameWindowGLFW;
import oggvik.mods.fullscreentweaks.config.FullscreenSettings;
import oggvik.mods.fullscreentweaks.config.SettingsManager;

public final class StartupWindowController {
	private static boolean loading;
	private static boolean gameFullscreen;
	private static boolean restoreFromMinimized;

	private StartupWindowController() {
	}

	public static void begin(GameWindowGLFW window) {
		FullscreenSettings settings = SettingsManager.get();
		gameFullscreen = GameSettings.START_IN_FULLSCREEN.isTrue()
			&& GameSettings.FULLSCREEN.isTrue();
		restoreFromMinimized = settings.isStartMinimized();
		loading = true;

		boolean loadingFullscreen =
			settings.getLoadingScreenMode().resolve(gameFullscreen);
		GameSettings.FULLSCREEN.value = loadingFullscreen;
		if (loadingFullscreen) {
			window.updateWindowState();
		} else {
			GlfwWindowController.applyFocusPolicy(window);
		}
		if (restoreFromMinimized) {
			GlfwWindowController.minimize(window);
		}
	}

	public static void finish(GameWindowGLFW window) {
		if (!loading) {
			return;
		}
		GameSettings.FULLSCREEN.value = gameFullscreen;
		if (restoreFromMinimized) {
			GlfwWindowController.restore(window);
		}
		loading = false;
	}

	public static void abort(GameWindowGLFW window) {
		if (!loading) {
			return;
		}
		finish(window);
		window.updateWindowState();
	}
}
