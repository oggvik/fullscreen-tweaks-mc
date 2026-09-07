// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss;

import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(StopMinimizingOnFocusLoss.MOD_ID)
public final class StopMinimizingOnFocusLoss {
    public static final String MOD_ID = "stop_minimizing_on_focus_loss";
    private static final Logger LOGGER = LogManager.getLogger();

    public StopMinimizingOnFocusLoss() {
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(
                        (Supplier<String>) () -> FMLNetworkConstants.IGNORESERVERONLY,
                        (BiPredicate<String, Boolean>) (remoteVersion, isServer) -> true
                )
        );

        LOGGER.info("Preventing fullscreen auto-minimize on focus loss");
    }
}
