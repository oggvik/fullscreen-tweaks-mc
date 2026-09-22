// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.platform;

import oggvik.mods.fullscreentweaks.FullscreenTweaks;
import oggvik.mods.fullscreentweaks.client.FullscreenSettingsScreen;
/*? if fabric {*/
import net.fabricmc.api.ClientModInitializer;

public final class PlatformEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FullscreenTweaks.init();
    }
}
/*?} elif neoforge {*/
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = FullscreenTweaks.MOD_ID, dist = Dist.CLIENT)
public final class PlatformEntrypoint {
    public PlatformEntrypoint(ModContainer container) {
        FullscreenTweaks.init();
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (IConfigScreenFactory) (ignored, parent) -> new FullscreenSettingsScreen(parent));
    }
}
*//*?} elif forge_config_gui_handler {*/
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

@Mod(FullscreenTweaks.MOD_ID)
public final class PlatformEntrypoint {
    public PlatformEntrypoint() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            FullscreenTweaks.init();
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory(
                            (minecraft, parent) -> new FullscreenSettingsScreen(parent)));
        }
    }
}
*//*?} elif forge_config_gui {*/
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(FullscreenTweaks.MOD_ID)
public final class PlatformEntrypoint {
    public PlatformEntrypoint() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            FullscreenTweaks.init();
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory(
                            parent -> new FullscreenSettingsScreen(parent)));
        }
    }
}
*//*?} elif forge_config_screen {*/
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(FullscreenTweaks.MOD_ID)
public final class PlatformEntrypoint {
    public PlatformEntrypoint() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            FullscreenTweaks.init();
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            parent -> new FullscreenSettingsScreen(parent)));
        }
    }
}
*//*?}*/
