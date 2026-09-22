// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.config;

import net.fabricmc.loader.api.FabricLoader;
import oggvik.mods.fullscreentweaks.FullscreenTweaks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;

public final class SettingsManager {
	private static final String CONFIG_FILE_NAME = "fullscreen-tweaks.properties";

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
		Objects.requireNonNull(value, "value");
		initialize();
		settings = value;
		save(configPath(), value);
	}

	private static FullscreenSettings load(Path path) {
		if (!Files.isRegularFile(path)) {
			return FullscreenSettings.defaults();
		}
		Properties properties = new Properties();
		try (InputStream input = Files.newInputStream(path)) {
			properties.load(input);
			return FullscreenSettings.load(properties);
		} catch (IOException | RuntimeException exception) {
			FullscreenTweaks.LOGGER.warn("Could not read {}; using defaults", path, exception);
			return FullscreenSettings.defaults();
		}
	}

	private static void save(Path path, FullscreenSettings value) {
		Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
		try {
			Files.createDirectories(path.getParent());
			try (OutputStream output = Files.newOutputStream(temporary)) {
				value.toProperties().store(output, "Fullscreen Tweaks client settings");
			}
			try {
				Files.move(
					temporary,
					path,
					StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING
				);
			} catch (AtomicMoveNotSupportedException exception) {
				Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException exception) {
			FullscreenTweaks.LOGGER.warn("Could not write {}", path, exception);
			try {
				Files.deleteIfExists(temporary);
			} catch (IOException cleanupException) {
				exception.addSuppressed(cleanupException);
			}
		}
	}

	private static Path configPath() {
		return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);
	}
}
