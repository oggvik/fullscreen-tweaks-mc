// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks;

import oggvik.mods.fullscreentweaks.config.SettingsManager;

public final class FullscreenTweaks {
    public static final String MOD_ID = "fullscreen_tweaks";
    public static final String MOD_NAME = "Fullscreen Tweaks";

    private FullscreenTweaks() {
    }

    public static void init() {
        SettingsManager.initialize();
    }
}
