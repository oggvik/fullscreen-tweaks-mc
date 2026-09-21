// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.config;

import java.util.Locale;

/** The window mode used while Minecraft displays its initial loading screen. */
public enum LoadingScreenMode {
    SAME_AS_GAME,
    WINDOWED,
    FULLSCREEN;

    public LoadingScreenMode next() {
        LoadingScreenMode[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public static LoadingScreenMode parse(String value, LoadingScreenMode fallback) {
        if (value == null) {
            return fallback;
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return fallback;
        }
    }
}
