// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.render.window.GameWindowGLFW")
public final class GameWindowGLFWMixin {
	@Shadow
	public long window;

	@Inject(method = "init", at = @At("RETURN"))
	private void fullscreenTweaks$disableAutoIconifyAfterInit(
		Minecraft minecraft,
		int width,
		int height,
		CallbackInfo info
	) {
		fullscreenTweaks$disableAutoIconify();
	}

	@Inject(method = "updateWindowState", at = @At("RETURN"))
	private void fullscreenTweaks$disableAutoIconifyAfterWindowStateChange(CallbackInfo info) {
		fullscreenTweaks$disableAutoIconify();
	}

	@Unique
	private void fullscreenTweaks$disableAutoIconify() {
		GLFW.glfwSetWindowAttrib(this.window, GLFW.GLFW_AUTO_ICONIFY, GLFW.GLFW_FALSE);
	}
}
