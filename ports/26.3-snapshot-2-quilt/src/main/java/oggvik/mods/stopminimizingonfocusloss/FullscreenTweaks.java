// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss;

import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;

public final class FullscreenTweaks {
    public static final String MOD_ID = "stop_minimizing_on_focus_loss";
    public static final String MOD_NAME = "Fullscreen Tweaks";

    private FullscreenTweaks() {
    }

    public static void init() {
        SettingsManager.initialize();
    }
}
