// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.config;

import java.util.Objects;
import java.util.Properties;

/** Immutable client configuration consumed by the native-window controller. */
public final class FullscreenSettings {
    private final FullscreenMode fullscreenMode;
    private final boolean preventAutoIconify;

    public FullscreenSettings(FullscreenMode fullscreenMode, boolean preventAutoIconify) {
        this.fullscreenMode = Objects.requireNonNull(fullscreenMode, "fullscreenMode");
        this.preventAutoIconify = preventAutoIconify;
    }

    public static FullscreenSettings defaults() {
        return new FullscreenSettings(FullscreenMode.platformDefault(), true);
    }

    static FullscreenSettings load(Properties properties) {
        FullscreenSettings defaults = defaults();
        String preventionValue = properties.getProperty("preventAutoIconify");
        if (preventionValue == null) {
            // Migrate the former mod-wide switch; fullscreen mode now remains independent.
            preventionValue = properties.getProperty("enabled");
        }
        return new FullscreenSettings(
                FullscreenMode.parse(properties.getProperty("fullscreenMode"), defaults.fullscreenMode),
                parseBoolean(preventionValue, true)
        );
    }

    Properties toProperties() {
        Properties properties = new Properties();
        properties.setProperty("fullscreenMode", fullscreenMode.name());
        properties.setProperty("preventAutoIconify", Boolean.toString(preventAutoIconify));
        return properties;
    }

    public FullscreenMode getFullscreenMode() {
        return fullscreenMode;
    }

    public boolean isPreventAutoIconify() {
        return preventAutoIconify;
    }

    public FullscreenSettings withFullscreenMode(FullscreenMode value) {
        return new FullscreenSettings(value, preventAutoIconify);
    }

    public FullscreenSettings withPreventAutoIconify(boolean value) {
        return new FullscreenSettings(fullscreenMode, value);
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
