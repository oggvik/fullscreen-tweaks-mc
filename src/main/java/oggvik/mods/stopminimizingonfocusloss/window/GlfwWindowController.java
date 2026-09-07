// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.window;

import oggvik.mods.stopminimizingonfocusloss.config.FullscreenMode;
import oggvik.mods.stopminimizingonfocusloss.config.FullscreenSettings;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
/*? if !template_noop {*/
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
/*?}*/

/** Applies the two-setting fullscreen policy to Minecraft's GLFW window. */
public final class GlfwWindowController {
    private static long managedWindow;
    private static boolean managedBorderless;

    private GlfwWindowController() {
    }

    public static void apply(long window, boolean minecraftFullscreen) {
        /*? if template_noop {*/
        /*return;
        *//*?} else {*/
        if (window == 0L) {
            return;
        }

        FullscreenSettings settings = SettingsManager.get();
        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_AUTO_ICONIFY,
                settings.isPreventAutoIconify() ? GLFW.GLFW_FALSE : GLFW.GLFW_TRUE);

        if (!minecraftFullscreen) {
            restoreWindowDecorations(window);
            return;
        }

        long monitor = findCurrentMonitor(window);
        GLFWVidMode desktopMode = monitor == 0L ? null : GLFW.glfwGetVideoMode(monitor);
        if (desktopMode == null) {
            return;
        }

        managedWindow = window;
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

    private static void applyBorderless(long window, long monitor, GLFWVidMode desktopMode) {
        int[] x = new int[1];
        int[] y = new int[1];
        GLFW.glfwGetMonitorPos(monitor, x, y);
        if (GLFW.glfwGetWindowMonitor(window) != 0L) {
            GLFW.glfwSetWindowMonitor(window, 0L, x[0], y[0],
                    desktopMode.width(), desktopMode.height(), GLFW.GLFW_DONT_CARE);
        }
        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwSetWindowPos(window, x[0], y[0]);
        GLFW.glfwSetWindowSize(window, desktopMode.width(), desktopMode.height());
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
        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        if (managedWindow == window) {
            managedBorderless = false;
        }
    }
    /*?}*/
}
