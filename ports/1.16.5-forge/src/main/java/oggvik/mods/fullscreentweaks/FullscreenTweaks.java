// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks;

import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import oggvik.mods.fullscreentweaks.client.FullscreenSettingsScreen;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FullscreenTweaks.MOD_ID)
public final class FullscreenTweaks {
    public static final String MOD_ID = "fullscreen_tweaks";
    public static final String MOD_NAME = "Fullscreen Tweaks";
    private static final Logger LOGGER = LogManager.getLogger();

    public FullscreenTweaks() {
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(
                        (Supplier<String>) () -> FMLNetworkConstants.IGNORESERVERONLY,
                        (BiPredicate<String, Boolean>) (remoteVersion, isServer) -> true
                )
        );
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (minecraft, parent) -> new FullscreenSettingsScreen(parent)
        );

        LOGGER.info("Preventing fullscreen auto-minimize on focus loss");
        SettingsManager.initialize();
    }
}
