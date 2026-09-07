// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixins;

/*? if !template_noop {*/
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Avoids the changing public widget geometry APIs across Minecraft versions. */
@Mixin(AbstractWidget.class)
public interface WidgetBoundsAccessor {
    @Accessor("x") int stopMinimizingOnFocusLoss$getX();
    @Accessor("y") int stopMinimizingOnFocusLoss$getY();
    @Accessor("width") int stopMinimizingOnFocusLoss$getWidth();
    @Accessor("height") int stopMinimizingOnFocusLoss$getHeight();
    @Accessor("x") void stopMinimizingOnFocusLoss$setX(int value);
    @Accessor("y") void stopMinimizingOnFocusLoss$setY(int value);
    @Accessor("width") void stopMinimizingOnFocusLoss$setWidth(int value);
}
/*?}*/
