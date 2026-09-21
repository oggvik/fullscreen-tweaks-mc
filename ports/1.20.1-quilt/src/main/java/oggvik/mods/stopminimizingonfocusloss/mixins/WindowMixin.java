// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixins;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import oggvik.mods.stopminimizingonfocusloss.window.GlfwWindowController;
import oggvik.mods.stopminimizingonfocusloss.window.StartupWindowController;
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
    private static DisplayData stopMinimizingOnFocusLoss$selectLoadingWindowMode(DisplayData displayData) {
        /*? if new_window_handle {*/
        /*boolean gameFullscreen = displayData.isFullscreen();
        *//*?} else {*/
        boolean gameFullscreen = displayData.isFullscreen;
        /*?}*/
        Minecraft minecraft = Minecraft.getInstance();
        /*? if component_factory {*/
        gameFullscreen = gameFullscreen || minecraft.options.fullscreen().get();
        /*?} else {*/
        /*gameFullscreen = gameFullscreen || minecraft.options.fullscreen;
        *//*?}*/
        boolean loadingFullscreen = StartupWindowController.prepareLoading(gameFullscreen);
        if (loadingFullscreen == gameFullscreen) {
            return displayData;
        }
        /*? if new_window_handle {*/
        /*return displayData.withFullscreen(loadingFullscreen);
        *//*?} else {*/
        return new DisplayData(
                displayData.width,
                displayData.height,
                displayData.fullscreenWidth,
                displayData.fullscreenHeight,
                loadingFullscreen);
        /*?}*/
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applySettingsAfterCreate(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        /*? if !template_noop {*/
        GlfwWindowController.apply(stopMinimizingOnFocusLoss$windowHandle(), this.fullscreen);
        /*?}*/
    }

    @Inject(method = "setMode", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applySettingsAfterModeChange(CallbackInfo info) {
        /*? if !template_noop {*/
        GlfwWindowController.apply(stopMinimizingOnFocusLoss$windowHandle(), this.fullscreen);
        /*?}*/
    }

    @Unique
    private long stopMinimizingOnFocusLoss$windowHandle() {
        /*? if new_window_handle {*/
        /*return this.handle;
        *//*?} else {*/
        return this.window;
        /*?}*/
    }
}
