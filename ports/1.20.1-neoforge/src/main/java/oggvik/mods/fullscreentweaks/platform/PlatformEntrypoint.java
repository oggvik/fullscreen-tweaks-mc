// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.platform;

import oggvik.mods.fullscreentweaks.FullscreenTweaks;
/*? if fabric {*/
/*import net.fabricmc.api.ClientModInitializer;

public final class PlatformEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FullscreenTweaks.init();
    }
}
*//*?} elif neoforge {*/
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = FullscreenTweaks.MOD_ID, dist = Dist.CLIENT)
public final class PlatformEntrypoint {
    public PlatformEntrypoint() {
        FullscreenTweaks.init();
    }
}
*//*?} elif forge {*/
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(FullscreenTweaks.MOD_ID)
public final class PlatformEntrypoint {
    public PlatformEntrypoint() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            FullscreenTweaks.init();
        }
    }
}
/*?}*/
