// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixins;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import oggvik.mods.fullscreentweaks.window.GlfwWindowController;
import oggvik.mods.fullscreentweaks.window.StartupWindowController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    /*? if !template_noop {*/
    @Shadow
    private boolean fullscreen;

    /*? if new_window_handle {*/
    /*@Shadow
    @Final
    private long handle;
    *//*?} else {*/
    @Shadow
    @Final
    private long window;
    /*?}*/

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static DisplayData fullscreenTweaks$selectLoadingWindowMode(DisplayData displayData) {
        boolean gameFullscreen = displayData.isFullscreen
                || Minecraft.getInstance().options.fullscreen().get();
        StartupWindowController.prepareLoading(gameFullscreen);
        if (!displayData.isFullscreen) {
            return displayData;
        }
        return new DisplayData(
                displayData.width,
                displayData.height,
                displayData.fullscreenWidth,
                displayData.fullscreenHeight,
                false);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fullscreenTweaks$applySettingsAfterCreate(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        GlfwWindowController.apply(fullscreenTweaks$windowHandle(), this.fullscreen);
    }

    @Inject(method = "setMode", at = @At("HEAD"))
    private void fullscreenTweaks$prepareForModeChange(CallbackInfo info) {
        GlfwWindowController.prepareModeChange(
                fullscreenTweaks$windowHandle(), this.fullscreen);
    }

    @Inject(method = "setMode", at = @At("RETURN"))
    private void fullscreenTweaks$applySettingsAfterModeChange(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        GlfwWindowController.apply(fullscreenTweaks$windowHandle(), this.fullscreen);
    }

    @Unique
    private long fullscreenTweaks$windowHandle() {
        /*? if new_window_handle {*/
        /*return this.handle;
        *//*?} else {*/
        return this.window;
        /*?}*/
    }
    /*?}*/
}
