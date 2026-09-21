// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.config;

import java.util.Objects;
import java.util.Properties;

/** Immutable client configuration consumed by the native-window controller. */
public final class FullscreenSettings {
    private final FullscreenMode fullscreenMode;
    private final boolean preventAutoIconify;
    private final LoadingScreenMode loadingScreenMode;
    private final boolean startMinimized;

    public FullscreenSettings(FullscreenMode fullscreenMode, boolean preventAutoIconify) {
        this(fullscreenMode, preventAutoIconify, LoadingScreenMode.SAME_AS_GAME, false);
    }

    public FullscreenSettings(
            FullscreenMode fullscreenMode,
            boolean preventAutoIconify,
            LoadingScreenMode loadingScreenMode,
            boolean startMinimized
    ) {
        this.fullscreenMode = Objects.requireNonNull(fullscreenMode, "fullscreenMode");
        this.preventAutoIconify = preventAutoIconify;
        this.loadingScreenMode = Objects.requireNonNull(loadingScreenMode, "loadingScreenMode");
        this.startMinimized = startMinimized;
    }

    public static FullscreenSettings defaults() {
        return new FullscreenSettings(
                FullscreenMode.platformDefault(), true, LoadingScreenMode.SAME_AS_GAME, false);
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
                parseBoolean(preventionValue, true),
                LoadingScreenMode.parse(
                        properties.getProperty("loadingScreenMode"), defaults.loadingScreenMode),
                parseBoolean(properties.getProperty("startMinimized"), false)
        );
    }

    Properties toProperties() {
        Properties properties = new Properties();
        properties.setProperty("fullscreenMode", fullscreenMode.name());
        properties.setProperty("preventAutoIconify", Boolean.toString(preventAutoIconify));
        properties.setProperty("loadingScreenMode", loadingScreenMode.name());
        properties.setProperty("startMinimized", Boolean.toString(startMinimized));
        return properties;
    }

    public FullscreenMode getFullscreenMode() {
        return fullscreenMode;
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

    public FullscreenSettings withFullscreenMode(FullscreenMode value) {
        return new FullscreenSettings(value, preventAutoIconify, loadingScreenMode, startMinimized);
    }

    public FullscreenSettings withPreventAutoIconify(boolean value) {
        return new FullscreenSettings(fullscreenMode, value, loadingScreenMode, startMinimized);
    }

    public FullscreenSettings withLoadingScreenMode(LoadingScreenMode value) {
        return new FullscreenSettings(fullscreenMode, preventAutoIconify, value, startMinimized);
    }

    public FullscreenSettings withStartMinimized(boolean value) {
        return new FullscreenSettings(fullscreenMode, preventAutoIconify, loadingScreenMode, value);
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
