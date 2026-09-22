// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class SettingsManagerTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void migratesLegacyConfigurationFile() throws Exception {
        Path current = temporaryDirectory.resolve("fullscreen-tweaks.properties");
        Path legacy = temporaryDirectory.resolve("stop-minimizing-on-focus-loss.properties");
        FullscreenSettings expected = new FullscreenSettings(
                FullscreenMode.BORDERLESS, false, LoadingScreenMode.WINDOWED, true);
        SettingsManager.save(legacy, expected);

        FullscreenSettings migrated = SettingsManager.loadCurrentOrLegacy(current, legacy);

        assertEquals(expected.getFullscreenMode(), migrated.getFullscreenMode());
        assertEquals(expected.isPreventAutoIconify(), migrated.isPreventAutoIconify());
        assertEquals(expected.getLoadingScreenMode(), migrated.getLoadingScreenMode());
        assertEquals(expected.isStartMinimized(), migrated.isStartMinimized());
        assertTrue(Files.isRegularFile(current));
    }
}
