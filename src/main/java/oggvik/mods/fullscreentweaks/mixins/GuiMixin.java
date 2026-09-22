// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixins;

/*? if new_gui_owner {*/
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Overlay;
import oggvik.mods.fullscreentweaks.window.StartupWindowController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "setOverlay", at = @At("HEAD"))
    private void fullscreenTweaks$restoreModeAfterLoading(
            Overlay overlay, CallbackInfo info
    ) {
        if (overlay == null) {
            StartupWindowController.finishLoading(Minecraft.getInstance().getWindow());
        }
    }
}
*//*?} else {*/
public final class GuiMixin {
    private GuiMixin() {
    }
}
/*?}*/
