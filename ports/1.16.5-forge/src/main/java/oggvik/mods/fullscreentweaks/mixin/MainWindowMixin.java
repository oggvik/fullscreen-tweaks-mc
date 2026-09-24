// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.IWindowEventListener;
import net.minecraft.client.renderer.MonitorHandler;
import net.minecraft.client.renderer.ScreenSize;
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

@Mixin(MainWindow.class)
public class MainWindowMixin {
    @Shadow
    @Final
    private long window;

    @Shadow
    private boolean fullscreen;

    @Shadow
    private int windowedX;

    @Shadow
    private int windowedY;

    @Shadow
    private int windowedWidth;

    @Shadow
    private int windowedHeight;

    @Unique
    private int fullscreenTweaks$startupWindowedX;

    @Unique
    private int fullscreenTweaks$startupWindowedY;

    @Unique
    private int fullscreenTweaks$startupWindowedWidth;

    @Unique
    private int fullscreenTweaks$startupWindowedHeight;

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static ScreenSize fullscreenTweaks$selectLoadingWindowMode(ScreenSize screenSize) {
        boolean gameFullscreen = screenSize.isFullscreen
                || Minecraft.getInstance().options.fullscreen;
        StartupWindowController.prepareLoading(gameFullscreen);
        if (!screenSize.isFullscreen) {
            return screenSize;
        }
        return new ScreenSize(
                screenSize.width,
                screenSize.height,
                screenSize.fullscreenWidth,
                screenSize.fullscreenHeight,
                false);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fullscreenTweaks$disableAutoIconifyAfterCreate(
            IWindowEventListener eventHandler,
            MonitorHandler screenManager,
            ScreenSize screenSize,
            String videoModeName,
            String title,
            CallbackInfo info
    ) {
        this.fullscreenTweaks$startupWindowedX = this.windowedX;
        this.fullscreenTweaks$startupWindowedY = this.windowedY;
        this.fullscreenTweaks$startupWindowedWidth = this.windowedWidth;
        this.fullscreenTweaks$startupWindowedHeight = this.windowedHeight;
        GlfwWindowController.apply(this.window, this.fullscreen);
    }

    @Inject(method = "updateFullscreen", at = @At("HEAD"))
    private void fullscreenTweaks$prepareForFullscreenTransition(
            boolean updateVsync,
            CallbackInfo info
    ) {
        if (this.fullscreen) {
            GlfwWindowController.prepareModeChange(this.window);
            return;
        }

        if (StartupWindowController.requiresWindowsFullscreenExitRepair()) {
            GlfwWindowController.prepareWindowedMode(this.window, null);
            this.windowedX = this.fullscreenTweaks$startupWindowedX;
            this.windowedY = this.fullscreenTweaks$startupWindowedY;
            this.windowedWidth = this.fullscreenTweaks$startupWindowedWidth;
            this.windowedHeight = this.fullscreenTweaks$startupWindowedHeight;
            return;
        }

        int[] bounds = new int[4];
        if (GlfwWindowController.prepareWindowedMode(this.window, bounds)) {
            this.windowedX = bounds[0];
            this.windowedY = bounds[1];
            this.windowedWidth = bounds[2];
            this.windowedHeight = bounds[3];
        }
    }

    @Inject(method = "updateFullscreen", at = @At("RETURN"))
    private void fullscreenTweaks$applySettingsAfterFullscreenTransition(
            boolean updateVsync,
            CallbackInfo info
    ) {
        StartupWindowController.reapplyLoadingState((MainWindow) (Object) this);
        fullscreenTweaks$applySettings();
        if (!this.fullscreen
                && StartupWindowController.requiresWindowsFullscreenExitRepair()) {
            GlfwWindowController.refreshWindowedPresentation(this.window);
        }
    }

    @Inject(method = "onFocus", at = @At("RETURN"))
    private void fullscreenTweaks$repairDecorationsAfterFocusChange(
            long callbackWindow,
            boolean focused,
            CallbackInfo info
    ) {
        if (callbackWindow == this.window) {
            fullscreenTweaks$applySettings();
        }
    }

    @Unique
    private void fullscreenTweaks$applySettings() {
        GlfwWindowController.apply(this.window, this.fullscreen);
    }
}
