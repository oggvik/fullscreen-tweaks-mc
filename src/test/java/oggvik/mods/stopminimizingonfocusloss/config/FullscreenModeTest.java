// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class FullscreenModeTest {
    @Test
    void windowsDefaultsToBorderless() {
        assertPlatformDefault("Windows 11", FullscreenMode.BORDERLESS);
    }

    @Test
    void nonWindowsPlatformsDefaultToNative() {
        assertPlatformDefault("Linux", FullscreenMode.NATIVE);
        assertPlatformDefault("Mac OS X", FullscreenMode.NATIVE);
        assertPlatformDefault("Darwin", FullscreenMode.NATIVE);
    }

    private static void assertPlatformDefault(String osName, FullscreenMode expected) {
        String original = System.getProperty("os.name");
        try {
            System.setProperty("os.name", osName);
            assertEquals(expected, FullscreenMode.platformDefault());
        } finally {
            if (original == null) {
                System.clearProperty("os.name");
            } else {
                System.setProperty("os.name", original);
            }
        }
    }
}
