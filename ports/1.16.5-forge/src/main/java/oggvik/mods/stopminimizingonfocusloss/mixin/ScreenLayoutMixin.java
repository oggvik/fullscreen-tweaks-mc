// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

/*? if !template_noop {*/
import net.minecraft.client.gui.screen.Screen;
import oggvik.mods.stopminimizingonfocusloss.client.SettingsButtonHost;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenLayoutMixin {
    // Runs after all init listeners, immediately before Screen draws its widgets.
    /*? if render_extractor {*/
    /*@Inject(method = "extractRenderState", at = @At("HEAD"))
    *//*?} else {*/
    @Inject(method = "render", at = @At("HEAD"))
    /*?}*/
    private void stopMinimizingOnFocusLoss$layout(CallbackInfo info) {
        if ((Object) this instanceof SettingsButtonHost) {
            ((SettingsButtonHost) (Object) this).stopMinimizingOnFocusLoss$placeSettingsButton();
        }
    }
}
/*?}*/
