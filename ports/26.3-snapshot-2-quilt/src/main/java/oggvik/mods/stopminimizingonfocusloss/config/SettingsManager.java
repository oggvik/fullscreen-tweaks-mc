// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.config;

import oggvik.mods.stopminimizingonfocusloss.StopMinimizingOnFocusLoss;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/** Owns the process-wide settings snapshot and durable, atomic persistence. */
public final class SettingsManager {
    private static final String CONFIG_DIRECTORY_PROPERTY =
            "stop_minimizing_on_focus_loss.configDirectory";
    private static final String CONFIG_FILE_NAME = "stop-minimizing-on-focus-loss.properties";

    private static boolean initialized;
    private static FullscreenSettings settings = FullscreenSettings.defaults();

    private SettingsManager() {
    }

    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        settings = load(configPath());
    }

    public static synchronized FullscreenSettings get() {
        initialize();
        return settings;
    }

    public static synchronized void set(FullscreenSettings value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        initialize();
        settings = value;
        save(configPath(), value);
    }

    static FullscreenSettings load(Path path) {
        if (!Files.isRegularFile(path)) {
            return FullscreenSettings.defaults();
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
            return FullscreenSettings.load(properties);
        } catch (IOException | RuntimeException exception) {
            warn("Could not read " + path + "; using safe defaults", exception);
            return FullscreenSettings.defaults();
        }
    }

    static void save(Path path, FullscreenSettings value) {
        Path parent = path.toAbsolutePath().getParent();
        if (parent == null) {
            return;
        }
        Path temporary = parent.resolve(path.getFileName().toString() + ".tmp");
        try {
            Files.createDirectories(parent);
            try (OutputStream output = Files.newOutputStream(temporary)) {
                value.toProperties().store(output, "Stop Minimizing on Focus Loss client settings");
            }
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            warn("Could not write " + path, exception);
            try {
                Files.deleteIfExists(temporary);
            } catch (IOException ignored) {
            }
        }
    }

    private static Path configPath() {
        String directory = System.getProperty(CONFIG_DIRECTORY_PROPERTY, "config");
        return Paths.get(directory).resolve(CONFIG_FILE_NAME);
    }

    private static void warn(String message, Throwable throwable) {
        System.err.println("[" + StopMinimizingOnFocusLoss.MOD_NAME + "] " + message
                + ": " + throwable.getMessage());
    }
}
