// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

/*? if fabric {*/
package oggvik.mods.fullscreentweaks.platform;

import oggvik.mods.fullscreentweaks.FullscreenTweaks;
import oggvik.mods.fullscreentweaks.client.FullscreenSettingsScreen;
/*? if modmenu_114 {*/
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Function;
/*?} else if legacy_modmenu {*/
/*import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
*//*?} else {*/
/*import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
*//*?}*/

public final class ModMenuIntegration implements ModMenuApi {
   /*? if modmenu_114 {*/
   @Override
   public String getModId() {
       return FullscreenTweaks.MOD_ID;
   }

   @Override
   public Function<Screen, ? extends Screen> getConfigScreenFactory() {
       return FullscreenSettingsScreen::new;
   }
   /*?} else {*/
   /*@Override
   public ConfigScreenFactory<?> getModConfigScreenFactory() {
       return FullscreenSettingsScreen::new;
   }
   *//*?}*/
}
/*?}*/
