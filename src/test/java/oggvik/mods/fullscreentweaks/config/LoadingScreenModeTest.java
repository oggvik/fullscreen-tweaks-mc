// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class LoadingScreenModeTest {
    @Test
    void cyclesThroughAllChoices() {
        assertEquals(LoadingScreenMode.WINDOWED, LoadingScreenMode.SAME_AS_GAME.next());
        assertEquals(LoadingScreenMode.FULLSCREEN, LoadingScreenMode.WINDOWED.next());
        assertEquals(LoadingScreenMode.SAME_AS_GAME, LoadingScreenMode.FULLSCREEN.next());
    }

    @Test
    void invalidValuesUseFallback() {
        assertEquals(LoadingScreenMode.WINDOWED,
                LoadingScreenMode.parse("invalid", LoadingScreenMode.WINDOWED));
    }
}
