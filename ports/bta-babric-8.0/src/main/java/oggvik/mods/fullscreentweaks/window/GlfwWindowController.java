// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import net.minecraft.client.render.window.GameWindowGLFW;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import org.lwjgl.glfw.GLFW;

public final class GlfwWindowController {
	private GlfwWindowController() {
	}

	public static void applyFocusPolicy(GameWindowGLFW window) {
		int autoIconify = SettingsManager.get().isPreventAutoIconify()
			? GLFW.GLFW_FALSE
			: GLFW.GLFW_TRUE;
		GLFW.glfwSetWindowAttrib(window.window, GLFW.GLFW_AUTO_ICONIFY, autoIconify);
	}

	public static void minimize(GameWindowGLFW window) {
		GLFW.glfwIconifyWindow(window.window);
	}

	public static void restore(GameWindowGLFW window) {
		GLFW.glfwRestoreWindow(window.window);
	}
}
