// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import java.util.Locale;

public enum LoadingScreenMode {
	SAME_AS_GAME,
	WINDOWED,
	FULLSCREEN;

	static LoadingScreenMode parse(String value, LoadingScreenMode fallback) {
		if (value == null) {
			return fallback;
		}
		try {
			return valueOf(value.trim().toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException exception) {
			return fallback;
		}
	}

	public boolean resolve(boolean gameFullscreen) {
		return switch (this) {
			case SAME_AS_GAME -> gameFullscreen;
			case WINDOWED -> false;
			case FULLSCREEN -> true;
		};
	}
}
