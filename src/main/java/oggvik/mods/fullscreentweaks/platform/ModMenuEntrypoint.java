// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.platform;

/*? if fabric && !legacy_modmenu {*/
/*import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import oggvik.mods.fullscreentweaks.client.FullscreenSettingsScreen;

public final class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FullscreenSettingsScreen::new;
    }
}
*//*?} else if modmenu_114 {*/
/*import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import oggvik.mods.fullscreentweaks.FullscreenTweaks;
import oggvik.mods.fullscreentweaks.client.FullscreenSettingsScreen;

import java.util.function.Function;

public final class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public String getModId() {
        return FullscreenTweaks.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return FullscreenSettingsScreen::new;
    }
}
*//*?} else if legacy_modmenu {*/
/*import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import oggvik.mods.fullscreentweaks.client.FullscreenSettingsScreen;

public final class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FullscreenSettingsScreen::new;
    }
}
*//*?} else {*/
public final class ModMenuEntrypoint {
    private ModMenuEntrypoint() {
    }
}
/*?}*/
