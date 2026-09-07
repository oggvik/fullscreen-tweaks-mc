// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("dev.kikugie.stonecutter")
    id("dev.isxander.modstitch.base")
    `maven-publish`
}

// ========== Versions & Project Info ==========
val mcVersion: String by project
val versionWithoutMC = property("modVersion")!!.toString()

val isAlpha = "alpha" in versionWithoutMC
val isBeta = "beta" in versionWithoutMC

val isFabric = modstitch.isLoom
val isNeoforge = modstitch.isModDevGradleRegular
val isForge = modstitch.isModDevGradleLegacy
val isForgeLike = modstitch.isModDevGradle

val loader = when {
    isFabric -> "fabric"
    isNeoforge -> "neoforge"
    isForge -> "forge"
    else -> error("Unknown loader")
}

val javaTargetVersion = when {
    stonecutter.eval(mcVersion, ">=26.1") -> 25
    stonecutter.eval(mcVersion, ">1.20.4") -> 21
    stonecutter.eval(mcVersion, ">=1.18") -> 17
    stonecutter.eval(mcVersion, ">=1.17") -> 16
    else -> 8
}
val resolvedModId = resolveProp("modId") ?: error("modId is required")

repositories {
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
    }
}

// ========== ModStitch Setup ==========
modstitch {
    minecraftVersion = mcVersion
    javaVersion = javaTargetVersion

    parchment {
        resolveProp("parchment.version")?.let { mappingsVersion = it }
        resolveProp("parchment.minecraft")?.let { minecraftVersion = it }
    }

    metadata {
        modId = resolvedModId
        modName = resolveProp("modName")
        modVersion = "$versionWithoutMC+${stonecutter.current.project}"
        modGroup = resolveProp("modGroup")
        modDescription = resolveProp("modDescription")
        modLicense = resolveProp("modLicense")
        modAuthor = resolveProp("modAuthor")

        val resourcePackFormats = mapOf(
            "1.14.4" to 4,
            "1.15.2" to 5,
            "1.16.5" to 6,
            "1.17.1" to 7,
            "1.18.2" to 8,
            "1.19.2" to 9,
            "1.19.4" to 13,
            "1.20.1" to 15,
            "1.20.6" to 32,
            "1.21.1" to 34,
            "1.21.11" to 75,
            "26.1.2" to 84,
            "26.2" to 88,
            "26.3-snapshot-1" to 89,
            "26.3-snapshot-2" to 90,
            "26.3-snapshot-3" to 91,
            "26.3-snapshot-4" to 92,
        )
        val resourcePackFormat = resourcePackFormats[mcVersion]
            ?: throw IllegalArgumentException("Please store the resource pack version for $mcVersion in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
        replacementProperties.put("pack_format", resourcePackFormat.toString())
        replacementProperties.put(
            "pack_metadata",
            packMetadata(resourcePackFormat, resolveProp("modDescription").orEmpty())
        )
        replacementProperties.put("java_version", javaTargetVersion.toString())

        // replacementProperties DSL
        fun setReplace(key: String, property: String) {
            resolveProp(property)?.let { replacementProperties.put(key, it) }
        }

        setReplace("repo_url", "repoUrl")
        setReplace("repo_issues_url", "repoIssuesUrl")
        setReplace("repo_sources_url", "repoSourcesUrl")
        setReplace("mc", "meta.mcDep")
        setReplace("fabricLoader", "deps.fabricLoader")
    }

    loom {
        resolveProp("deps.fabricLoader")?.let { fabricLoaderVersion = it }
    }

    moddevgradle {
        resolveProp("deps.neoforge")?.let { neoForgeVersion = it }
        resolveProp("deps.forge")?.let { forgeVersion = it }
        defaultRuns()

        modstitch.onEnable {
            tasks.named("createMinecraftArtifacts") {
                dependsOn("stonecutterGenerate")
            }
        }
    }

    mixin {
        addMixinsToModManifest = true
        configs.register(resolvedModId) {
            side.set(CLIENT)
        }
    }
}

// ========== Stonecutter ==========
stonecutter {
    constants {
        put("fabric", isFabric)
        put("neoforge", isNeoforge)
        put("forge", isForge)
        put("forgelike", isForgeLike)
        put("new_window_handle", stonecutter.eval(mcVersion, ">=1.21.11") || stonecutter.eval(mcVersion, ">=26.1"))
        put("new_set_screen", stonecutter.eval(mcVersion, ">=26.2"))
        put("old_minecraft_window_field", stonecutter.eval(mcVersion, "<=1.14.4"))
        put("legacy_string_button", stonecutter.eval(mcVersion, "<=1.15.2"))
        put("legacy_add_button", stonecutter.eval(mcVersion, "<=1.16.5"))
        put("button_builder", stonecutter.eval(mcVersion, ">=1.19.4"))
        put("component_factory", stonecutter.eval(mcVersion, ">=1.19"))
        put("render_extractor", stonecutter.eval(mcVersion, ">=26.1"))
        put("gui_graphics", stonecutter.eval(mcVersion, ">=1.20") && stonecutter.eval(mcVersion, "<26.1"))
        put("resource_location_factory", stonecutter.eval(mcVersion, ">=1.21") && !stonecutter.current.project.startsWith("1.21.11"))
        put("identifier", stonecutter.current.project.startsWith("1.21.11"))
        put("modern_menu_list_background", stonecutter.eval(mcVersion, ">=1.20.2"))
        put("render_background_delta", stonecutter.eval(mcVersion, ">1.20.1"))
        put("transparent_settings_background", stonecutter.current.project == "1.21.1-neoforge")
        put("options_screen_subpackage", stonecutter.eval(mcVersion, ">=1.21"))
        put("template_noop", resolveProp("templateNoop")?.toBoolean() == true)
    }
}

// ========== Dependencies ==========
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")

    if (isFabric) {
        resolveProp("deps.fabricApiBase")?.let { apiBaseVersion ->
            val apiBase = "net.fabricmc.fabric-api:fabric-api-base:$apiBaseVersion"
            add("modstitchModImplementation", apiBase)
            add("include", apiBase)
        }
        resolveProp("deps.fabricResourceLoader")?.let { resourceLoaderVersion ->
            val module = resolveProp("deps.fabricResourceLoaderModule")
                ?: error("Missing resource loader module for $mcVersion")
            val resourceLoader = "net.fabricmc.fabric-api:$module:$resourceLoaderVersion"
            add("modstitchModImplementation", resourceLoader)
            add("include", resourceLoader)
        }
    }
}

// ========== Tasks ==========
tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }

    withType<JavaCompile>().configureEach {
        dependsOn("stonecutterGenerate")
        if (javaTargetVersion == 8) {
            options.compilerArgs.add("-Xlint:-options")
        }
        if (isForge && mcVersion == "1.20.1") {
            options.compilerArgs.add("-Xlint:-removal")
        }
    }

    withType<ProcessResources>().configureEach {
        val mixinRefmapPlaceholder = "\"__mixin_refmap_placeholder__\": \"\","
        val settingsMixinPlaceholder = "\"__settings_mixin_placeholder__\": \"\","
        val mixinRefmapLine = if (isForge) {
            "\"refmap\": \"$resolvedModId.refmap.json\","
        } else {
            ""
        }
        val settingsMixinLine = if (resolveProp("templateNoop")?.toBoolean() == true) {
            ""
        } else {
            "\"OptionsScreenMixin\", \"ScreenLayoutMixin\", \"WidgetBoundsAccessor\","
        }

        inputs.property("mixin_refmap", mixinRefmapLine)
        inputs.property("settings_mixin", settingsMixinLine)
        filesMatching("$resolvedModId.mixins.json") {
            filter { line: String ->
                line.replace(mixinRefmapPlaceholder, mixinRefmapLine)
                    .replace(settingsMixinPlaceholder, settingsMixinLine)
                    .trimEnd()
            }
        }
    }

    named("generateModMetadata") {
        dependsOn("stonecutterGenerate")
    }
}

// ========== Helpers ==========
fun resolveProp(property: String): String? =
    System.getenv(property)?.takeIf { it.isNotBlank() }
        ?: findProperty(property)?.toString()?.takeIf { it.isNotBlank() }

fun packMetadata(packFormat: Int, description: String): String {
    val escapedDescription = jsonString(description)
    return if (packFormat > 64) {
        """
    "pack_format": $packFormat,
    "min_format": $packFormat,
    "max_format": $packFormat,
    "description": {
      "text": $escapedDescription
    }
        """.trimIndent()
    } else {
        """
    "description": $escapedDescription,
    "pack_format": $packFormat
        """.trimIndent()
    }
}

fun jsonString(value: String): String = buildString {
    append('"')
    value.forEach { char ->
        when (char) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(char)
        }
    }
    append('"')
}
