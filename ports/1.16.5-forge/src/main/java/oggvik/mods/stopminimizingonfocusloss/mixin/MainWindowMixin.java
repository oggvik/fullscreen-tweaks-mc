// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.IWindowEventListener;
import net.minecraft.client.renderer.MonitorHandler;
import net.minecraft.client.renderer.ScreenSize;
import oggvik.mods.stopminimizingonfocusloss.window.GlfwWindowController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MainWindow.class)
public class MainWindowMixin {
    @Shadow
    @Final
    private long window;

    @Shadow
    private boolean fullscreen;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterCreate(
            IWindowEventListener eventHandler,
            MonitorHandler screenManager,
            ScreenSize screenSize,
            String videoModeName,
            String title,
            CallbackInfo info
    ) {
        stopMinimizingOnFocusLoss$applySettings();
    }

    @Inject(method = "updateFullscreen", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$applySettingsAfterFullscreenTransition(
            boolean updateVsync,
            CallbackInfo info
    ) {
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
