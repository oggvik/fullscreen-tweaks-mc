// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.OptionsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(OptionsCategory.class)
public interface OptionsCategoryAccessor {
	@Accessor("translationKey")
	String fullscreenTweaks$getTranslationKey();

	@Accessor("components")
	List<OptionsComponent> fullscreenTweaks$getComponents();
}
