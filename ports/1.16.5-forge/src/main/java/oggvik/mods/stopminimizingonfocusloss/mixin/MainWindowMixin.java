// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.IWindowEventListener;
import net.minecraft.client.renderer.MonitorHandler;
import net.minecraft.client.renderer.ScreenSize;
import org.lwjgl.glfw.GLFW;
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

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterCreate(
            IWindowEventListener eventHandler,
            MonitorHandler screenManager,
            ScreenSize screenSize,
            String videoModeName,
            String title,
            CallbackInfo info
    ) {
        stopMinimizingOnFocusLoss$disableAutoIconifyWindow();
    }

    @Inject(method = "setMode", at = @At("RETURN"))
    private void stopMinimizingOnFocusLoss$disableAutoIconifyAfterModeChange(CallbackInfo info) {
        stopMinimizingOnFocusLoss$disableAutoIconifyWindow();
    }

    @Unique
    private void stopMinimizingOnFocusLoss$disableAutoIconifyWindow() {
        GLFW.glfwSetWindowAttrib(this.window, GLFW.GLFW_AUTO_ICONIFY, GLFW.GLFW_FALSE);
    }
}
