// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.client;

import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.option.OptionBoolean;

public final class TooltipBooleanOptionComponent extends BooleanOptionComponent {
	private final String tooltipTranslationKey;

	public TooltipBooleanOptionComponent(
		OptionBoolean option,
		String tooltipTranslationKey
	) {
		super(option);
		this.tooltipTranslationKey = tooltipTranslationKey;
	}

	@Override
	public String getTooltipTranslationKey() {
		return tooltipTranslationKey;
	}
}
