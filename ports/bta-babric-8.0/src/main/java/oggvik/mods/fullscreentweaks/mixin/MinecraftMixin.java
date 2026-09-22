// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.window.GameWindow;
import net.minecraft.client.render.window.GameWindowGLFW;
import net.minecraft.core.UnexpectedThrowable;
import oggvik.mods.fullscreentweaks.client.FullscreenVideoOptions;
import oggvik.mods.fullscreentweaks.window.StartupWindowController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow
	public GameWindow gameWindow;

	@Inject(
		method = "startGame",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/options/data/OptionsPages;init()V",
			shift = At.Shift.AFTER
		)
	)
	private void fullscreenTweaks$registerVideoOptions(CallbackInfo info) {
		FullscreenVideoOptions.register();
	}

	@Inject(
		method = "startGame",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/window/GameWindow;updateWindowState()V"
		)
	)
	private void fullscreenTweaks$restoreGameWindowMode(CallbackInfo info) {
		if (gameWindow instanceof GameWindowGLFW window) {
			StartupWindowController.finish(window);
		}
	}

	@Inject(method = "onMinecraftCrash", at = @At("HEAD"))
	private void fullscreenTweaks$restoreWindowAfterStartupFailure(
		UnexpectedThrowable throwable,
		CallbackInfo info
	) {
		if (gameWindow instanceof GameWindowGLFW window) {
			StartupWindowController.abort(window);
		}
	}
}
