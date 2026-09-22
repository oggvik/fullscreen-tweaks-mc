// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.IWindowEventListener;
import net.minecraft.client.renderer.MonitorHandler;
import net.minecraft.client.renderer.ScreenSize;
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

@Mixin(MainWindow.class)
public class MainWindowMixin {
    @Shadow
    @Final
    private long window;

    @Shadow
    private boolean fullscreen;

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static ScreenSize stopMinimizingOnFocusLoss$selectLoadingWindowMode(ScreenSize screenSize) {
        boolean gameFullscreen = screenSize.isFullscreen
                || Minecraft.getInstance().options.fullscreen;
        boolean loadingFullscreen = StartupWindowController.prepareLoading(gameFullscreen);
        if (loadingFullscreen == screenSize.isFullscreen) {
            return screenSize;
        }
        return new ScreenSize(
                screenSize.width,
                screenSize.height,
                screenSize.fullscreenWidth,
                screenSize.fullscreenHeight,
                loadingFullscreen);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterCreate(
            IWindowEventListener eventHandler,
            MonitorHandler screenManager,
            ScreenSize screenSize,
            String videoModeName,
            String title,
            CallbackInfo info
    ) {
        StartupWindowController.reapplyLoadingState((MainWindow) (Object) this);
        GlfwWindowController.apply(this.window, this.fullscreen);
    }

    @Inject(method = "updateFullscreen", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applySettingsAfterFullscreenTransition(
            boolean updateVsync,
            CallbackInfo info
    ) {
        StartupWindowController.reapplyLoadingState((MainWindow) (Object) this);
        stopMinimizingOnFocusLoss$applySettings();
    }

    @Inject(method = "onFocus", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$repairDecorationsAfterFocusChange(
            long callbackWindow,
            boolean focused,
            CallbackInfo info
    ) {
        if (callbackWindow == this.window) {
            stopMinimizingOnFocusLoss$applySettings();
        }
    }

    @Unique
    private void stopMinimizingOnFocusLoss$applySettings() {
        GlfwWindowController.apply(this.window, this.fullscreen);
    }
}
