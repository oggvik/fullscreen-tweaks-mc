// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixins;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
/*? if !new_gui_owner {*/
/*import net.minecraft.client.gui.screens.Overlay;
*//*?}*/
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
    private Window window;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fullscreenTweaks$applyLoadingWindowSettings(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState(this.window);
    }

    /*? if new_set_screen && !template_noop {*/
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwShowWindow(J)V",
                    shift = At.Shift.AFTER
            )
    )
    private void fullscreenTweaks$keepMinimizedAfterShow(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState(this.window);
    }
    /*?}*/

    /*? if !new_gui_owner {*/
    /*@Inject(method = "setOverlay", at = @At("HEAD"))
    private void fullscreenTweaks$restoreModeAfterLoading(
            Overlay overlay, CallbackInfo info
    ) {
        if (overlay == null) {
            StartupWindowController.finishLoading(this.window);
        }
    }
    *//*?}*/
}
