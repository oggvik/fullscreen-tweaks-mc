// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class StopMinimizingOnFocusLoss {
    public static final String MOD_ID = "stop_minimizing_on_focus_loss";

    private static final int GLFW_AUTO_ICONIFY = 0x00020006;
    private static final int GLFW_FALSE = 0;
    private static final String[] DISPLAY_CLASS_NAMES = {
            "org.lwjgl.opengl.Display",
            "org.lwjglx.opengl.Display"
    };
    private static final String[] HANDLE_MEMBER_NAMES = {
            "window",
            "windowHandle",
            "currentWindow",
            "handle",
            "getHandle",
            "getHandleKt"
    };
    private static boolean loggedSuccess;
    private static boolean loggedNoHandle;
    private static boolean loggedFailure;

    private StopMinimizingOnFocusLoss() {
    }

    public static void disableAutoIconifyIfSupported() {
        Long handle = findGlfwWindowHandle();
        if (handle == null || handle == 0L) {
            logNoHandle();
            return;
        }

        disableAutoIconify(handle);
    }

    public static void disableAutoIconify(long handle) {
        if (handle == 0L) {
            logNoHandle();
            return;
        }

        try {
            Class<?> glfw = Class.forName("org.lwjgl.glfw.GLFW");
            Method setWindowAttrib = glfw.getMethod("glfwSetWindowAttrib", long.class, int.class, int.class);
            setWindowAttrib.invoke(null, handle, GLFW_AUTO_ICONIFY, GLFW_FALSE);
            logSuccess();
        } catch (ReflectiveOperationException | RuntimeException | LinkageError ignored) {
            logFailure();
        }
    }

    private static Long findGlfwWindowHandle() {
        for (String className : DISPLAY_CLASS_NAMES) {
            try {
                Class<?> display = Class.forName(className);
                Long methodHandle = findHandleByMethod(display);
                if (methodHandle != null) {
                    return methodHandle;
                }
                Long fieldHandle = findHandleByField(display);
                if (fieldHandle != null) {
                    return fieldHandle;
                }
                Long windowHandle = findHandleFromDisplayWindow(display);
                if (windowHandle != null) {
                    return windowHandle;
                }
            } catch (ReflectiveOperationException | LinkageError ignored) {
            }
        }
        return null;
    }

    private static Long findHandleByMethod(Class<?> display) throws ReflectiveOperationException {
        for (String name : HANDLE_MEMBER_NAMES) {
            try {
                Method method = display.getDeclaredMethod(name);
                if (!Modifier.isStatic(method.getModifiers()) || method.getReturnType() != long.class) {
                    continue;
                }
                method.setAccessible(true);
                return (Long) method.invoke(null);
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    private static Long findHandleFromDisplayWindow(Class<?> display) throws ReflectiveOperationException {
        Method getWindow;
        try {
            getWindow = display.getDeclaredMethod("getWindow");
        } catch (NoSuchMethodException ignored) {
            return null;
        }
        if (!Modifier.isStatic(getWindow.getModifiers())) {
            return null;
        }
        getWindow.setAccessible(true);
        Object window = getWindow.invoke(null);
        if (window == null) {
            return null;
        }
        Method getHandle = window.getClass().getDeclaredMethod("getHandle");
        if (getHandle.getReturnType() != long.class) {
            return null;
        }
        getHandle.setAccessible(true);
        return (Long) getHandle.invoke(window);
    }

    private static Long findHandleByField(Class<?> display) throws ReflectiveOperationException {
        for (String name : HANDLE_MEMBER_NAMES) {
            try {
                Field field = display.getDeclaredField(name);
                if (!Modifier.isStatic(field.getModifiers()) || field.getType() != long.class) {
                    continue;
                }
                field.setAccessible(true);
                return field.getLong(null);
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    private static void logSuccess() {
        if (!loggedSuccess) {
            loggedSuccess = true;
            System.out.println("[Stop Minimizing on Focus Loss] Disabled GLFW auto-iconify.");
        }
    }

    private static void logNoHandle() {
        if (!loggedNoHandle) {
            loggedNoHandle = true;
            System.out.println("[Stop Minimizing on Focus Loss] Could not find a GLFW window handle.");
        }
    }

    private static void logFailure() {
        if (!loggedFailure) {
            loggedFailure = true;
            System.out.println("[Stop Minimizing on Focus Loss] GLFW auto-iconify is not available in this runtime.");
        }
    }
}
