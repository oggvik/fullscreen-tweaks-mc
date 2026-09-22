// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixins;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Window.class)
public interface WindowModeAccessor {
    /*? if template_noop {*/
    /*@Accessor("fullscreenRequested")
    *//*?} else {*/
    @Accessor("fullscreen")
    /*?}*/
    boolean fullscreenTweaks$isFullscreenRequested();

    /*? if template_noop {*/
    /*@Accessor("fullscreenRequested")
    *//*?} else {*/
    @Accessor("fullscreen")
    /*?}*/
    void fullscreenTweaks$setFullscreenRequested(boolean fullscreen);

    @Invoker("setMode")
    void fullscreenTweaks$setMode();
}
