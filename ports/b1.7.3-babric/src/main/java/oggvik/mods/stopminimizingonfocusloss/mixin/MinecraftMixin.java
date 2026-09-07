// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

import net.minecraft.client.Minecraft;
import oggvik.mods.stopminimizingonfocusloss.StopMinimizingOnFocusLoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public final class MinecraftMixin {
    @Inject(method = "init", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterInit(CallbackInfo info) {
        StopMinimizingOnFocusLoss.disableAutoIconifyIfSupported();
    }

    @Inject(method = "toggleFullscreen", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterFullscreenToggle(CallbackInfo info) {
        StopMinimizingOnFocusLoss.disableAutoIconifyIfSupported();
    }
}
