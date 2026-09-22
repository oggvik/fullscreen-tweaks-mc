// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import java.util.Objects;
import java.util.Properties;

public final class FullscreenSettings {
	private final boolean preventAutoIconify;
	private final LoadingScreenMode loadingScreenMode;
	private final boolean startMinimized;

	public FullscreenSettings(
		boolean preventAutoIconify,
		LoadingScreenMode loadingScreenMode,
		boolean startMinimized
	) {
		this.preventAutoIconify = preventAutoIconify;
		this.loadingScreenMode = Objects.requireNonNull(loadingScreenMode, "loadingScreenMode");
		this.startMinimized = startMinimized;
	}

	public static FullscreenSettings defaults() {
		return new FullscreenSettings(true, LoadingScreenMode.SAME_AS_GAME, false);
	}

	static FullscreenSettings load(Properties properties) {
		FullscreenSettings defaults = defaults();
		return new FullscreenSettings(
			parseBoolean(properties.getProperty("preventAutoIconify"), defaults.preventAutoIconify),
			LoadingScreenMode.parse(
				properties.getProperty("loadingScreenMode"), defaults.loadingScreenMode),
			parseBoolean(properties.getProperty("startMinimized"), defaults.startMinimized)
		);
	}

	Properties toProperties() {
		Properties properties = new Properties();
		properties.setProperty("preventAutoIconify", Boolean.toString(preventAutoIconify));
		properties.setProperty("loadingScreenMode", loadingScreenMode.name());
		properties.setProperty("startMinimized", Boolean.toString(startMinimized));
		return properties;
	}

	public boolean isPreventAutoIconify() {
		return preventAutoIconify;
	}

	public LoadingScreenMode getLoadingScreenMode() {
		return loadingScreenMode;
	}

	public boolean isStartMinimized() {
		return startMinimized;
	}

	public FullscreenSettings withPreventAutoIconify(boolean value) {
		return new FullscreenSettings(value, loadingScreenMode, startMinimized);
	}

	public FullscreenSettings withLoadingScreenMode(LoadingScreenMode value) {
		return new FullscreenSettings(preventAutoIconify, value, startMinimized);
	}

	public FullscreenSettings withStartMinimized(boolean value) {
		return new FullscreenSettings(preventAutoIconify, loadingScreenMode, value);
	}

	private static boolean parseBoolean(String value, boolean fallback) {
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		return fallback;
	}
}
