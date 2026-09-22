// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.window.GameWindowGLFW;
import oggvik.mods.fullscreentweaks.window.GlfwWindowController;
import oggvik.mods.fullscreentweaks.window.StartupWindowController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.render.window.GameWindowGLFW")
public final class GameWindowGLFWMixin {
	@Inject(method = "init", at = @At("RETURN"))
	private void fullscreenTweaks$applyLoadingWindowState(
		Minecraft minecraft,
		int width,
		int height,
		CallbackInfo info
	) {
		StartupWindowController.begin((GameWindowGLFW) (Object) this);
	}

	@Inject(method = "updateWindowState", at = @At("RETURN"))
	private void fullscreenTweaks$applyFocusPolicyAfterWindowStateChange(CallbackInfo info) {
		GlfwWindowController.applyFocusPolicy((GameWindowGLFW) (Object) this);
	}
}
