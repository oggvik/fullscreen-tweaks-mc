// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class FullscreenSettingsTest {
    @Test
    void defaultsToBorderlessOnWindows() {
        assertEquals(FullscreenMode.BORDERLESS, FullscreenMode.platformDefault("Windows 11"));
        assertEquals(FullscreenMode.BORDERLESS, FullscreenMode.platformDefault("windows server 2025"));
    }

    @Test
    void defaultsToNativeOutsideWindows() {
        assertEquals(FullscreenMode.NATIVE, FullscreenMode.platformDefault("Linux"));
        assertEquals(FullscreenMode.NATIVE, FullscreenMode.platformDefault("Mac OS X"));
        assertEquals(FullscreenMode.NATIVE, FullscreenMode.platformDefault("FreeBSD"));
        assertEquals(FullscreenMode.NATIVE, FullscreenMode.platformDefault(null));
    }

    @Test
    void invalidValuesUseSafeDefaults() {
        Properties properties = new Properties();
        properties.setProperty("fullscreenMode", "invalid");
        properties.setProperty("preventAutoIconify", "invalid");
        properties.setProperty("monitor.mode", "selected");
        properties.setProperty("videoMode.mode", "selected");

        FullscreenSettings settings = FullscreenSettings.load(properties);

        assertEquals(FullscreenMode.platformDefault(), settings.getFullscreenMode());
        assertTrue(settings.isPreventAutoIconify());
        assertEquals(LoadingScreenMode.SAME_AS_GAME, settings.getLoadingScreenMode());
        assertFalse(settings.isStartMinimized());
        assertEquals(4, settings.toProperties().size());
    }

    @Test
    void validValuesRoundTrip() {
        FullscreenSettings original = new FullscreenSettings(
                FullscreenMode.BORDERLESS, false, LoadingScreenMode.FULLSCREEN, true);

        FullscreenSettings restored = FullscreenSettings.load(original.toProperties());

        assertEquals(FullscreenMode.BORDERLESS, restored.getFullscreenMode());
        assertFalse(restored.isPreventAutoIconify());
        assertEquals(LoadingScreenMode.FULLSCREEN, restored.getLoadingScreenMode());
        assertTrue(restored.isStartMinimized());
        assertEquals("BORDERLESS", restored.toProperties().getProperty("fullscreenMode"));
        assertEquals("false", restored.toProperties().getProperty("preventAutoIconify"));
        assertEquals("FULLSCREEN", restored.toProperties().getProperty("loadingScreenMode"));
        assertEquals("true", restored.toProperties().getProperty("startMinimized"));
    }

    @Test
    void oldEnabledSettingMigratesToPrevention() {
        Properties properties = new Properties();
        properties.setProperty("enabled", "false");

        FullscreenSettings settings = FullscreenSettings.load(properties);

        assertFalse(settings.isPreventAutoIconify());
    }

    @Test
    void preventionTogglesWithoutChangingFullscreenMode() {
        for (FullscreenMode mode : FullscreenMode.values()) {
            FullscreenSettings settings = new FullscreenSettings(mode, true)
                    .withPreventAutoIconify(false);
            assertEquals(mode, settings.getFullscreenMode());
            assertFalse(settings.isPreventAutoIconify());
            assertFalse(settings.toProperties().containsKey("enabled"));
        }
    }

    @Test
    void explicitPreventionSettingTakesPrecedenceOverLegacySwitch() {
        Properties properties = new Properties();
        properties.setProperty("enabled", "false");
        properties.setProperty("preventAutoIconify", "true");
        assertTrue(FullscreenSettings.load(properties).isPreventAutoIconify());
    }
}
