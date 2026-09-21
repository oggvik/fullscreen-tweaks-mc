// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixins;

import com.mojang.blaze3d.platform.Window;
import oggvik.mods.stopminimizingonfocusloss.window.GlfwWindowController;
import oggvik.mods.stopminimizingonfocusloss.window.StartupWindowController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    @Shadow
    private boolean fullscreen;

    @Shadow
    private void setMode() {
    }

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
        boolean gameFullscreen = this.fullscreen;
        boolean loadingFullscreen = StartupWindowController.loadingFullscreen(gameFullscreen);
        if (loadingFullscreen != gameFullscreen) {
            this.fullscreen = loadingFullscreen;
            this.setMode();
            this.fullscreen = gameFullscreen;
            StartupWindowController.markModeOverridden();
        }
        /*? if !template_noop {*/
        GlfwWindowController.apply(stopMinimizingOnFocusLoss$windowHandle(), loadingFullscreen);
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
