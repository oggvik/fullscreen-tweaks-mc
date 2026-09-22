// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.fullscreentweaks.mixin;

import net.minecraft.core.lang.I18n;
import net.minecraft.core.lang.Language;
import oggvik.mods.fullscreentweaks.FullscreenTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Mixin(I18n.class)
public abstract class I18nMixin {
	@Shadow
	private Language currentLanguage;

	@Inject(method = "reload", at = @At("RETURN"))
	private void fullscreenTweaks$loadTranslations(String languageId, CallbackInfo info) {
		fullscreenTweaks$loadInto(
			((LanguageAccessor) currentLanguage).fullscreenTweaks$getEntries(),
			"/lang/fullscreen_tweaks/" + currentLanguage.getId() + ".lang"
		);
		if (!"en_US".equals(currentLanguage.getId())) {
			fullscreenTweaks$loadInto(
				((LanguageAccessor) (Object) Language.Default.INSTANCE)
					.fullscreenTweaks$getEntries(),
				"/lang/fullscreen_tweaks/en_US.lang"
			);
		}
	}

	@Unique
	private static void fullscreenTweaks$loadInto(
		Properties entries,
		String resourcePath
	) {
		try (InputStream input = I18n.getResourceAsStream(resourcePath)) {
			if (input == null) {
				return;
			}
			try (InputStreamReader reader =
					 new InputStreamReader(input, StandardCharsets.UTF_8)) {
				entries.load(reader);
			}
		} catch (IOException exception) {
			FullscreenTweaks.LOGGER.error("Could not load translations from {}", resourcePath, exception);
		}
	}
}
