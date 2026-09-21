// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LoadingGui;
import oggvik.mods.stopminimizingonfocusloss.window.StartupWindowController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    private MainWindow window;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applyLoadingWindowSettings(CallbackInfo info) {
        StartupWindowController.beginLoading(this.window);
    }

    @Inject(method = "setOverlay", at = @At("HEAD"))
    private void stopMinimizingOnFocusLoss$restoreModeAfterLoading(
            LoadingGui overlay, CallbackInfo info
    ) {
        if (overlay == null) {
            StartupWindowController.finishLoading(this.window);
        }
    }
}
