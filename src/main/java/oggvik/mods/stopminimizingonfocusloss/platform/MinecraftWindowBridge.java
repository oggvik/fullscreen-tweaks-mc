// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.platform;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
/*? if !template_noop {*/
import net.minecraft.client.gui.components.AbstractWidget;
/*? if !component_factory {*/
/*import net.minecraft.client.Option;
*//*?}*/
/*?}*/
import oggvik.mods.stopminimizingonfocusloss.window.GlfwWindowController;

/** The only Minecraft-version-specific window access used by the settings UI. */
public final class MinecraftWindowBridge {
    private MinecraftWindowBridge() {
    }

    /*? if !template_noop {*/
    public static AbstractWidget createFullscreenButton(int x, int y, int width) {
        Minecraft minecraft = Minecraft.getInstance();
        /*? if component_factory {*/
        return minecraft.options.fullscreen().createButton(minecraft.options, x, y, width);
        /*?} else {*/
        /*return Option.USE_FULLSCREEN.createButton(minecraft.options, x, y, width);
        *//*?}*/
    }

    public static boolean fullscreenSetting() {
        /*? if component_factory {*/
        return Minecraft.getInstance().options.fullscreen().get();
        /*?} else {*/
        /*return Minecraft.getInstance().options.fullscreen;
        *//*?}*/
    }
    /*?}*/

    public static void showScreen(Screen screen) {
        /*? if new_set_screen {*/
        /*Minecraft.getInstance().gui.setScreen(screen);
        *//*?} else {*/
        Minecraft.getInstance().setScreen(screen);
        /*?}*/
    }

    public static void reapply() {
        /*? if template_noop {*/
        /*return;
        *//*?} else {*/
        Window window = minecraftWindow();
        if (window != null) {
            GlfwWindowController.reapply(handle(window), window.isFullscreen());
        }
        /*?}*/
    }

    private static Window minecraftWindow() {
        Minecraft minecraft = Minecraft.getInstance();
        /*? if old_minecraft_window_field {*/
        /*return minecraft.window;
        *//*?} else {*/
        return minecraft.getWindow();
        /*?}*/
    }

    /*? if !template_noop {*/
    private static long handle(Window window) {
        /*? if new_window_handle {*/
        /*return window.handle();
        *//*?} else {*/
        return window.getWindow();
        /*?}*/
    }
    /*?}*/
}
