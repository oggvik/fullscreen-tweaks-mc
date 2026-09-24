// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixins;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import oggvik.mods.fullscreentweaks.window.StartupWindowController;
/*? if template_noop {*/
/*import oggvik.mods.fullscreentweaks.window.SdlWindowController;
*//*?} else {*/
import oggvik.mods.fullscreentweaks.window.GlfwWindowController;
/*?}*/
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    /*?}*/

    @ModifyVariable(
            /*? if template_noop {*/
            /*method = "<init>(Lcom/mojang/blaze3d/platform/WindowEventHandler;"
                    + "Lcom/mojang/blaze3d/platform/DisplayData;Ljava/lang/String;ZLjava/lang/String;"
                    + "Lcom/mojang/blaze3d/platform/MonitorManager;"
                    + "Lcom/mojang/renderpearl/api/device/GpuBackend;I)V",
            *//*?} else {*/
            method = "<init>",
            /*?}*/
            at = @At("HEAD"),
            argsOnly = true
    )
    private static DisplayData fullscreenTweaks$selectLoadingWindowMode(DisplayData displayData) {
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
        /*? if defer_loading_fullscreen_attach {*/
        /*loadingFullscreen = false;
        *//*?}*/
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

    /*? if !template_noop && !render_extractor {*/
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V",
                    remap = false
            )
    )
    private void fullscreenTweaks$configureInitialGlfwHints() {
        GlfwWindowController.resetAndConfigureInitialWindowHints();
    }
    /*?}*/

    /*? if render_extractor && !template_noop {*/
    /*@Inject(
            method = "createGlfwWindow",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/GpuBackend;setWindowHints()V",
                    shift = At.Shift.AFTER
            )
    )
    private static void fullscreenTweaks$configureInitialGlfwHints(
            CallbackInfoReturnable<Long> info
    ) {
        GlfwWindowController.configureInitialWindowHints();
    }
    *//*?}*/

    @Inject(
            /*? if template_noop {*/
            /*method = "<init>(Lcom/mojang/blaze3d/platform/WindowEventHandler;"
                    + "Lcom/mojang/blaze3d/platform/DisplayData;Ljava/lang/String;ZLjava/lang/String;"
                    + "Lcom/mojang/blaze3d/platform/MonitorManager;"
                    + "Lcom/mojang/renderpearl/api/device/GpuBackend;I)V",
            *//*?} else {*/
            method = "<init>",
            /*?}*/
            at = @At("RETURN")
    )
    private void fullscreenTweaks$applySettingsAfterCreate(CallbackInfo info) {
        /*? if defer_minimize_after_window_create {*/
        /*StartupWindowController.reapplyLoadingMode((Window) (Object) this);
        *//*?}*/
        /*? if minimize_after_window_policy {*/
        /*StartupWindowController.reapplyLoadingMode((Window) (Object) this);
        *//*?}*/
        /*? if reapply_loading_state_before_window_policy {*/
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        /*?}*/
        fullscreenTweaks$applyWindowPolicy();
        /*? if minimize_after_window_policy {*/
        /*StartupWindowController.reapplyLoadingMinimized((Window) (Object) this);
        *//*?}*/
    }

    /*? if defer_loading_fullscreen_attach && !template_noop {*/
    /*@Inject(method = "setMode", at = @At("HEAD"))
    private void fullscreenTweaks$prepareForModeChange(CallbackInfo info) {
        GlfwWindowController.prepareModeChange(
                fullscreenTweaks$windowHandle(), this.fullscreen);
    }
    *//*?}*/

    @Inject(method = "setMode", at = @At("RETURN"))
    private void fullscreenTweaks$applySettingsAfterModeChange(CallbackInfo info) {
        /*? if defer_minimize_after_window_create {*/
        /*StartupWindowController.reapplyLoadingMode((Window) (Object) this);
        *//*?}*/
        /*? if minimize_after_window_policy {*/
        /*StartupWindowController.reapplyLoadingMode((Window) (Object) this);
        *//*?}*/
        /*? if reapply_loading_state_before_window_policy {*/
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        /*?}*/
        fullscreenTweaks$applyWindowPolicy();
        /*? if minimize_after_window_policy {*/
        /*StartupWindowController.reapplyLoadingMinimized((Window) (Object) this);
        *//*?}*/
    }

    /*? if template_noop {*/
    /*@ModifyArg(
            method = "createWindow",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/renderpearl/api/device/GpuBackend;"
                            + "createWindow(Ljava/lang/String;IIJ)J"
            ),
            index = 3
    )
    private long fullscreenTweaks$configureInitialWindowFlags(long flags) {
        return StartupWindowController.configureInitialWindowFlags(flags);
    }
    *//*?}*/

    /*? if template_noop {*/
    /*@Inject(method = "onFocus", at = @At("TAIL"))
    private void fullscreenTweaks$applyFocusLossPolicy(boolean focused, CallbackInfo info) {
        SdlWindowController.handleFocusChanged((Window) (Object) this, focused);
    }
    *//*?}*/

    @Unique
    private void fullscreenTweaks$applyWindowPolicy() {
        /*? if template_noop {*/
        /*SdlWindowController.apply((Window) (Object) this);
        *//*?} else {*/
        GlfwWindowController.apply(fullscreenTweaks$windowHandle(), this.fullscreen);
        /*?}*/
    }

    /*? if !template_noop {*/
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
