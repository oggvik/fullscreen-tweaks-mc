// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(StopMinimizingOnFocusLoss.MOD_ID)
public final class StopMinimizingOnFocusLoss {
    public static final String MOD_ID = "stop_minimizing_on_focus_loss";

    public StopMinimizingOnFocusLoss() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            init();
        }
    }

    public static void init() {
    }
}
