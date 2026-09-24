// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.window;

import oggvik.mods.fullscreentweaks.config.FullscreenMode;
import oggvik.mods.fullscreentweaks.config.FullscreenSettings;
import oggvik.mods.fullscreentweaks.config.SettingsManager;
/*? if !template_noop {*/
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
/*?}*/

/** Applies the runtime fullscreen policy to Minecraft's GLFW window. */
public final class GlfwWindowController {
    private static long managedWindow;
    private static boolean managedBorderless;
    private static boolean managedFullscreen;
    private static boolean hasWindowedBounds;
    private static int windowedX;
    private static int windowedY;
    private static int windowedWidth;
    private static int windowedHeight;

    private GlfwWindowController() {
    }

    public static void apply(long window, boolean minecraftFullscreen) {
        /*? if template_noop {*/
        /*return;
        *//*?} else {*/
        if (window == 0L) {
            return;
        }
        if (managedWindow != window) {
            managedWindow = window;
            managedBorderless = false;
            managedFullscreen = false;
            hasWindowedBounds = false;
        }

        FullscreenSettings settings = SettingsManager.get();
        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_AUTO_ICONIFY,
                settings.isPreventAutoIconify() ? GLFW.GLFW_FALSE : GLFW.GLFW_TRUE);

        if (!minecraftFullscreen) {
            restoreWindowDecorations(window);
            managedFullscreen = false;
            managedBorderless = false;
            return;
        }
        managedFullscreen = true;

        long monitor = findCurrentMonitor(window);
        GLFWVidMode desktopMode = monitor == 0L ? null : GLFW.glfwGetVideoMode(monitor);
        if (desktopMode == null) {
            return;
        }

        if (settings.getFullscreenMode() == FullscreenMode.BORDERLESS) {
            applyBorderless(window, monitor, desktopMode);
        } else if (managedBorderless || GLFW.glfwGetWindowMonitor(window) == 0L) {
            // Reattaching a borderless window requires a mode. Use the desktop mode; otherwise
            // leave Minecraft's already-active native fullscreen resolution completely alone.
            applyNative(window, monitor, desktopMode);
        } else {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
            managedBorderless = false;
        }
        /*?}*/
    }

    /*? if !template_noop {*/
    public static void reapply(long window, boolean minecraftFullscreen) {
        apply(window, minecraftFullscreen);
    }

    public static void prepareModeChange(long window) {
        if (window == 0L || GLFW.glfwGetWindowMonitor(window) != 0L) {
            return;
        }
        if (managedWindow != window) {
            managedWindow = window;
            managedBorderless = false;
            managedFullscreen = false;
            hasWindowedBounds = false;
        }
        if (managedFullscreen) {
            return;
        }

        int[] x = new int[1];
        int[] y = new int[1];
        int[] width = new int[1];
        int[] height = new int[1];
        GLFW.glfwGetWindowPos(window, x, y);
        GLFW.glfwGetWindowSize(window, width, height);
        if (width[0] > 0 && height[0] > 0) {
            windowedX = x[0];
            windowedY = y[0];
            windowedWidth = width[0];
            windowedHeight = height[0];
            hasWindowedBounds = true;
        }
    }

    public static boolean prepareWindowedMode(long window, int[] bounds) {
        if (window == 0L || bounds == null || bounds.length < 4) {
            return false;
        }
        restoreWindowDecorations(window);
        if (managedWindow != window || !managedFullscreen || !hasWindowedBounds) {
            return false;
        }
        bounds[0] = windowedX;
        bounds[1] = windowedY;
        bounds[2] = windowedWidth;
        bounds[3] = windowedHeight;
        return true;
    }

    private static void applyBorderless(long window, long monitor, GLFWVidMode desktopMode) {
        int[] x = new int[1];
        int[] y = new int[1];
        GLFW.glfwGetMonitorPos(monitor, x, y);
        if (GLFW.glfwGetWindowMonitor(window) != 0L) {
            GLFW.glfwSetWindowMonitor(window, 0L, x[0], y[0],
                    desktopMode.width(), desktopMode.height(), GLFW.GLFW_DONT_CARE);
        }
        if (GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_DECORATED) != GLFW.GLFW_FALSE) {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        }
        int[] windowX = new int[1];
        int[] windowY = new int[1];
        int[] windowWidth = new int[1];
        int[] windowHeight = new int[1];
        GLFW.glfwGetWindowPos(window, windowX, windowY);
        GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);
        if (windowX[0] != x[0] || windowY[0] != y[0]) {
            GLFW.glfwSetWindowPos(window, x[0], y[0]);
        }
        if (windowWidth[0] != desktopMode.width() || windowHeight[0] != desktopMode.height()) {
            GLFW.glfwSetWindowSize(window, desktopMode.width(), desktopMode.height());
        }
        managedBorderless = true;
    }

    private static void applyNative(long window, long monitor, GLFWVidMode desktopMode) {
        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        GLFW.glfwSetWindowMonitor(window, monitor, 0, 0,
                desktopMode.width(), desktopMode.height(), desktopMode.refreshRate());
        managedBorderless = false;
    }

    private static long findCurrentMonitor(long window) {
        long attachedMonitor = GLFW.glfwGetWindowMonitor(window);
        if (attachedMonitor != 0L) {
            return attachedMonitor;
        }

        PointerBuffer monitors = GLFW.glfwGetMonitors();
        if (monitors == null || !monitors.hasRemaining()) {
            return GLFW.glfwGetPrimaryMonitor();
        }

        int[] windowX = new int[1];
        int[] windowY = new int[1];
        int[] windowWidth = new int[1];
        int[] windowHeight = new int[1];
        GLFW.glfwGetWindowPos(window, windowX, windowY);
        GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);

        long bestMonitor = 0L;
        long greatestOverlap = 0L;
        for (int index = monitors.position(); index < monitors.limit(); index++) {
            long monitor = monitors.get(index);
            GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);
            if (mode == null) {
                continue;
            }
            int[] monitorX = new int[1];
            int[] monitorY = new int[1];
            GLFW.glfwGetMonitorPos(monitor, monitorX, monitorY);
            long overlap = overlapArea(
                    monitorX[0], monitorY[0], mode.width(), mode.height(),
                    windowX[0], windowY[0], windowWidth[0], windowHeight[0]);
            if (overlap > greatestOverlap) {
                bestMonitor = monitor;
                greatestOverlap = overlap;
            }
        }

        long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
        return bestMonitor != 0L
                ? bestMonitor
                : primaryMonitor != 0L ? primaryMonitor : monitors.get(monitors.position());
    }

    private static long overlapArea(
            int firstX, int firstY, int firstWidth, int firstHeight,
            int secondX, int secondY, int secondWidth, int secondHeight
    ) {
        long left = Math.max((long) firstX, secondX);
        long top = Math.max((long) firstY, secondY);
        long right = Math.min((long) firstX + firstWidth,
                (long) secondX + Math.max(0, secondWidth));
        long bottom = Math.min((long) firstY + firstHeight,
                (long) secondY + Math.max(0, secondHeight));
        return Math.max(0L, right - left) * Math.max(0L, bottom - top);
    }

    private static void restoreWindowDecorations(long window) {
        if (GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_DECORATED) == GLFW.GLFW_FALSE) {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        }
        managedBorderless = false;
    }
    /*?}*/
}
