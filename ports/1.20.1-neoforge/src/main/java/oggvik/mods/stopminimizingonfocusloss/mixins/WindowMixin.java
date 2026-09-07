// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixins;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.stopminimizingonfocusloss.window.GlfwWindowController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applySettingsAfterCreate(CallbackInfo info) {
        GlfwWindowController.apply(stopMinimizingOnFocusLoss$windowHandle(), this.fullscreen);
    }

    @Inject(method = "setMode", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applySettingsAfterModeChange(CallbackInfo info) {
        GlfwWindowController.apply(stopMinimizingOnFocusLoss$windowHandle(), this.fullscreen);
    }

    @Unique
    private long stopMinimizingOnFocusLoss$windowHandle() {
        /*? if new_window_handle {*/
        /*return this.handle;
        *//*?} else {*/
        return this.window;
        /*?}*/
    }
    /*?}*/
}
