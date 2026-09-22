// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixins;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.window.GlfwWindowController;
import oggvik.mods.fullscreentweaks.window.StartupWindowController;
/*? if template_noop {*/
/*import org.lwjgl.sdl.SDLVideo;
*//*?}*/
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
    private void fullscreenTweaks$applySettingsAfterCreate(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        /*? if !template_noop {*/
        GlfwWindowController.apply(fullscreenTweaks$windowHandle(), this.fullscreen);
        /*?}*/
    }

    @Inject(method = "setMode", at = @At("RETURN"))
    private void fullscreenTweaks$applySettingsAfterModeChange(CallbackInfo info) {
        StartupWindowController.reapplyLoadingState((Window) (Object) this);
        /*? if !template_noop {*/
        GlfwWindowController.apply(fullscreenTweaks$windowHandle(), this.fullscreen);
        /*?}*/
    }

    /*? if template_noop {*/
    /*@Inject(method = "onFocus", at = @At("TAIL"))
    private void fullscreenTweaks$restoreAutoIconify(boolean focused, CallbackInfo info) {
        if (!focused && fullscreenTweaks$isExclusiveFullscreen()
                && !SettingsManager.get().isPreventAutoIconify()) {
            SDLVideo.SDL_MinimizeWindow(this.handle);
        }
    }

    @Unique
    private boolean fullscreenTweaks$isExclusiveFullscreen() {
        return (SDLVideo.SDL_GetWindowFlags(this.handle) & SDLVideo.SDL_WINDOW_FULLSCREEN) != 0L
                && SDLVideo.nSDL_GetWindowFullscreenMode(this.handle) != 0L;
    }
    *//*?}*/

    @Unique
    private long fullscreenTweaks$windowHandle() {
        /*? if new_window_handle {*/
        /*return this.handle;
        *//*?} else {*/
        return this.window;
        /*?}*/
    }
}
