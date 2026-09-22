// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixins;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Window.class)
public interface WindowModeAccessor {
    @Accessor("fullscreen")
    boolean fullscreenTweaks$isFullscreen();

    @Accessor("fullscreen")
    void fullscreenTweaks$setFullscreen(boolean fullscreen);

    @Invoker("setMode")
    void fullscreenTweaks$setMode();
}
