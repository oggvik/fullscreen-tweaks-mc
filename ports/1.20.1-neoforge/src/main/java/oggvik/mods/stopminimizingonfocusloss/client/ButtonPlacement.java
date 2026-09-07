// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.client;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

/** Geometry only: never changes another mod's controls or reserves a fixed screen corner. */
public final class ButtonPlacement {
    private static final int GAP = 4;
    private static final int MARGIN = 5;
    private static final int TITLE_BOTTOM = 32;

    private ButtonPlacement() {}

    public static final class Rect {
        public final int x, y, width, height;

        public Rect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public static Rect find(int screenWidth, int screenHeight, Rect current, List<Rect> occupied) {
        Set<Integer> alignedXs = new LinkedHashSet<>();
        List<Rect> nearestRows = new ArrayList<>(occupied);
        nearestRows.sort((a, b) -> Integer.compare(Math.abs(a.y - current.y), Math.abs(b.y - current.y)));
        for (Rect other : nearestRows) {
            if (other.width == current.width) {
                alignedXs.add(other.x);
            }
        }
        if (current.width == 150 && !occupied.isEmpty()) {
            alignedXs.add(screenWidth / 2 - 155);
            alignedXs.add(screenWidth / 2 + 5);
        }
        // Keep the chosen row, but correct a margin-aligned position to an existing column.
        for (int x : alignedXs) {
            Rect aligned = new Rect(x, current.y, current.width, current.height);
            if (fits(screenWidth, screenHeight, aligned, occupied)) {
                return aligned;
            }
        }
        if (fits(screenWidth, screenHeight, current, occupied)) {
            return current;
        }
        Set<Integer> xs = new LinkedHashSet<>(alignedXs);
        Set<Integer> ys = new LinkedHashSet<>();
        xs.add(MARGIN);
        xs.add(screenWidth - MARGIN - current.width);
        xs.add((screenWidth - current.width) / 2);
        ys.add(screenHeight - MARGIN - current.height);
        ys.add(TITLE_BOTTOM);
        for (Rect other : occupied) {
            xs.add(other.x + other.width + GAP);
            xs.add(other.x - current.width - GAP);
            ys.add(other.y - current.height - GAP);
            ys.add(other.y + other.height + GAP);
        }
        List<Integer> orderedYs = new ArrayList<>(ys);
        orderedYs.sort(Integer::compareTo);
        for (int y : orderedYs) {
            List<Integer> orderedXs = new ArrayList<>(xs);
            orderedXs.sort(Integer::compareTo);
            for (int x : orderedXs) {
                Rect candidate = new Rect(x, y, current.width, current.height);
                if (fits(screenWidth, screenHeight, candidate, occupied)) {
                    return candidate;
                }
            }
        }
        // A fully occupied screen has no safe slot. Retry when its layout changes.
        return null;
    }

    private static boolean fits(int width, int height, Rect r, List<Rect> occupied) {
        if (r.x < MARGIN || r.y < TITLE_BOTTOM || r.x + r.width > width - MARGIN
                || r.y + r.height > height - MARGIN) {
            return false;
        }
        for (Rect other : occupied) {
            if (r.x < other.x + other.width + GAP && r.x + r.width + GAP > other.x
                    && r.y < other.y + other.height + GAP && r.y + r.height + GAP > other.y) {
                return false;
            }
        }
        return true;
    }
}
