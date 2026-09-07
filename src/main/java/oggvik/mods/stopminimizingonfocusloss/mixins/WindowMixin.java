// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixins;

import com.mojang.blaze3d.platform.Window;
/*? if !template_noop {*/
import org.lwjgl.glfw.GLFW;
/*?}*/
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
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterCreate(CallbackInfo info) {
        stopMinimizingOnFocusLoss$disableAutoIconifyWindow();
    }

    @Inject(method = "setMode", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterModeChange(CallbackInfo info) {
        stopMinimizingOnFocusLoss$disableAutoIconifyWindow();
    }

    @Unique
    private void stopMinimizingOnFocusLoss$disableAutoIconifyWindow() {
        GLFW.glfwSetWindowAttrib(
                stopMinimizingOnFocusLoss$windowHandle(),
                GLFW.GLFW_AUTO_ICONIFY,
                GLFW.GLFW_FALSE
        );
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
