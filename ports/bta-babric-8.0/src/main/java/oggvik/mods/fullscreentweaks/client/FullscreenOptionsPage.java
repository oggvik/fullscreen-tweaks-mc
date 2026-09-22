// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.client;

import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionEnum;
import net.minecraft.client.render.window.GameWindowGLFW;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import oggvik.mods.fullscreentweaks.config.FullscreenSettings;
import oggvik.mods.fullscreentweaks.config.LoadingScreenMode;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.window.GlfwWindowController;

public final class FullscreenOptionsPage {
	private static boolean registered;

	private FullscreenOptionsPage() {
	}

	public static void register() {
		if (registered) {
			return;
		}
		registered = true;

		FullscreenSettings settings = SettingsManager.get();
		OptionBoolean preventAutoIconify = new OptionBoolean(
			"fullscreen_tweaks.prevent_auto_iconify",
			settings.isPreventAutoIconify()
		);
		preventAutoIconify.addOnChangeCallback((minecraft, option) -> {
			SettingsManager.set(
				SettingsManager.get().withPreventAutoIconify((Boolean) option.value));
			if (minecraft.gameWindow instanceof GameWindowGLFW window) {
				GlfwWindowController.applyFocusPolicy(window);
			}
		});

		OptionEnum<LoadingScreenMode> loadingScreenMode = new OptionEnum<>(
			"fullscreen_tweaks.loading_screen_mode",
			LoadingScreenMode.class,
			settings.getLoadingScreenMode()
		);
		loadingScreenMode.addOnChangeCallback((minecraft, option) ->
			SettingsManager.set(
				SettingsManager.get().withLoadingScreenMode(
					(LoadingScreenMode) option.value)));

		OptionBoolean startMinimized = new OptionBoolean(
			"fullscreen_tweaks.start_minimized",
			settings.isStartMinimized()
		);
		startMinimized.addOnChangeCallback((minecraft, option) ->
			SettingsManager.set(
				SettingsManager.get().withStartMinimized((Boolean) option.value)));

		OptionsPage page = new OptionsPage(
			"fullscreen_tweaks.options.page",
			new ItemStack(Blocks.GLASS)
		).withComponent(
			new OptionsCategory("fullscreen_tweaks.options.category.after_loading")
				.withComponent(new TooltipBooleanOptionComponent(
					preventAutoIconify,
					"fullscreen_tweaks.tooltip.prevent_auto_iconify"
				))
		).withComponent(
			new OptionsCategory("fullscreen_tweaks.options.category.loading")
				.withComponent(new ToggleableOptionComponent<>(
					loadingScreenMode,
					"fullscreen_tweaks.tooltip.loading_screen_mode"
				).applyOnRelease())
				.withComponent(new TooltipBooleanOptionComponent(
					startMinimized,
					"fullscreen_tweaks.tooltip.start_minimized"
				))
		);
		OptionsPages.register(page);
	}
}
