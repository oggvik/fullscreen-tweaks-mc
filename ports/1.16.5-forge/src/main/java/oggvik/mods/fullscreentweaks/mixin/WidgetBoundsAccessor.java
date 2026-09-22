// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

/*? if !template_noop {*/
import net.minecraft.client.gui.widget.Widget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Avoids the changing public widget geometry APIs across Minecraft versions. */
@Mixin(Widget.class)
public interface WidgetBoundsAccessor {
    @Accessor("x") int fullscreenTweaks$getX();
    @Accessor("y") int fullscreenTweaks$getY();
    @Accessor("width") int fullscreenTweaks$getWidth();
    @Accessor("height") int fullscreenTweaks$getHeight();
    @Accessor("x") void fullscreenTweaks$setX(int value);
    @Accessor("y") void fullscreenTweaks$setY(int value);
    @Accessor("width") void fullscreenTweaks$setWidth(int value);
}
/*?}*/
