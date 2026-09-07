// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.client;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

final class ButtonPlacementTest {
    @Test
    void keepsAnUnobstructedPositionStable() {
        ButtonPlacement.Rect own = new ButtonPlacement.Rect(5, 215, 150, 20);
        assertSame(own, ButtonPlacement.find(400, 240, own, Collections.emptyList()));
    }

    @Test
    void movesAwayFromLateAddedButtonsWithoutChangingThem() {
        ButtonPlacement.Rect own = new ButtonPlacement.Rect(5, 215, 150, 20);
        ButtonPlacement.Rect other = new ButtonPlacement.Rect(5, 215, 150, 20);
        ButtonPlacement.Rect result = ButtonPlacement.find(400, 240, own, Arrays.asList(other));
        assertNotNull(result);
        assertEquals(205, result.x);
        assertEquals(215, result.y);
        assertEquals(5, other.x);
    }

    @Test
    void findsSpaceAboveAnOccupiedFooter() {
        ButtonPlacement.Rect result = ButtonPlacement.find(320, 240,
                new ButtonPlacement.Rect(5, 215, 150, 20),
                Arrays.asList(new ButtonPlacement.Rect(0, 210, 320, 30)));
        assertNotNull(result);
        assertTrue(result.y + result.height + 4 <= 210);
    }

    @Test
    void compactEntryFitsBesideVanillaDoneOnSmallScreens() {
        ButtonPlacement.Rect result = ButtonPlacement.find(320, 240,
                new ButtonPlacement.Rect(5, 215, 40, 20),
                Arrays.asList(new ButtonPlacement.Rect(60, 215, 200, 20)));
        assertNotNull(result);
        assertEquals(5, result.x);
        assertEquals(215, result.y);
    }

    @Test
    void alignsWithExistingColumnsWithoutChangingTheRow() {
        ButtonPlacement.Rect result = ButtonPlacement.find(500, 300,
                new ButtonPlacement.Rect(5, 250, 150, 20),
                Arrays.asList(new ButtonPlacement.Rect(95, 210, 150, 20),
                        new ButtonPlacement.Rect(255, 210, 150, 20)));
        assertNotNull(result);
        assertEquals(95, result.x);
        assertEquals(250, result.y);
    }

    @Test
    void hidesWhenFullAndRecoversWhenSpaceReturns() {
        ButtonPlacement.Rect own = new ButtonPlacement.Rect(5, 215, 150, 20);
        assertNull(ButtonPlacement.find(320, 240, own,
                Arrays.asList(new ButtonPlacement.Rect(0, 0, 320, 240))));
        assertNotNull(ButtonPlacement.find(320, 240, own, Collections.emptyList()));
        assertNull(ButtonPlacement.find(100, 60, own, Collections.emptyList()));
    }
}
