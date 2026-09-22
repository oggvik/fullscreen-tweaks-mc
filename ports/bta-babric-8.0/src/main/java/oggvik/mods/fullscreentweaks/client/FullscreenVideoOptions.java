// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.client;

import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.OptionsComponent;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionEnum;
import net.minecraft.client.render.window.GameWindowGLFW;
import oggvik.mods.fullscreentweaks.config.FullscreenSettings;
import oggvik.mods.fullscreentweaks.config.LoadingScreenMode;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
import oggvik.mods.fullscreentweaks.mixin.OptionsCategoryAccessor;
import oggvik.mods.fullscreentweaks.window.GlfwWindowController;

import java.util.List;

public final class FullscreenVideoOptions {
	private static boolean registered;

	private FullscreenVideoOptions() {
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

		List<OptionsComponent> videoComponents = OptionsPages.VIDEO.getComponents();
		if (videoComponents.isEmpty()
			|| !(videoComponents.get(0) instanceof OptionsCategory displayCategory)
			|| !((OptionsCategoryAccessor) displayCategory)
				.fullscreenTweaks$getTranslationKey()
				.equals("gui.options.page.video.category.display")) {
			throw new IllegalStateException("Could not locate BTA's video display options");
		}

		List<OptionsComponent> displayComponents =
			((OptionsCategoryAccessor) displayCategory).fullscreenTweaks$getComponents();
		if (displayComponents.size() < 2) {
			throw new IllegalStateException("BTA's fullscreen options are missing");
		}

		displayComponents.addAll(
			2,
			List.of(
				new BooleanOptionComponent(preventAutoIconify),
				new ToggleableOptionComponent<>(loadingScreenMode).applyOnRelease(),
				new BooleanOptionComponent(startMinimized)
			)
		);
	}
}
