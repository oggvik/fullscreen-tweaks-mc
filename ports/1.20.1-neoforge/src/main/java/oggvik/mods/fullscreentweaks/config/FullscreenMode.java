// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import java.util.Locale;

/** The native-window strategy used while Minecraft considers itself fullscreen. */
public enum FullscreenMode {
    NATIVE,
    BORDERLESS;

    public static FullscreenMode platformDefault() {
        return platformDefault(System.getProperty("os.name", ""));
    }

    static FullscreenMode platformDefault(String name) {
        String osName = name == null ? "" : name.toLowerCase(Locale.ROOT);
        return osName.startsWith("windows") ? BORDERLESS : NATIVE;
    }

    public static FullscreenMode parse(String value, FullscreenMode fallback) {
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
