// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.language.jvm.tasks.ProcessResources
import net.neoforged.nfrtgradle.NeoFormRuntimeTask

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
val modMenuVersion = when (stonecutter.current.project) {
    "1.14.4-fabric" -> "1.7.18"
    "1.15.2-fabric" -> "1.10.7"
    "1.16.5-fabric" -> "1.16.23"
    "1.17.1-fabric" -> "2.0.17"
    "1.18.2-fabric" -> "3.2.5"
    "1.19.2-fabric" -> "4.1.2"
    "1.19.4-fabric" -> "6.3.1"
    "1.20.1-fabric" -> "7.2.2"
    "1.20.6-fabric" -> "10.0.0"
    "1.21.1-fabric" -> "11.0.3"
    "1.21.11-fabric" -> "17.0.1-beta.1"
    "26.1.2-fabric" -> "18.0.1"
    "26.2-fabric", "26.3-snapshot-1-fabric", "26.3-snapshot-2-fabric",
    "26.3-snapshot-3-fabric" -> "20.0.2"
    "26.3-fabric" -> "21.0.0-beta.1"
    else -> null
}

repositories {
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
    }
}

val mcpConfigManifest = resolveProp("deps.mcpConfig")?.let { mcpConfigVersion ->
    configurations.create("mcpConfigManifest") {
        isCanBeConsumed = false
        isCanBeResolved = true
    }.also { configuration ->
        dependencies.add(
            configuration.name,
            "de.oceanlabs.mcp:mcp_config:$mcpConfigVersion@zip"
        )
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

        val resourcePackFormats: Map<String, Number> = mapOf(
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
            "26.3" to 97.1,
        )
        val resourcePackFormat = resourcePackFormats[mcVersion]
            ?: throw IllegalArgumentException("Please store the resource pack version for $mcVersion in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
        replacementProperties.put("pack_format", resourcePackFormat.toString())
        replacementProperties.put(
            "pack_metadata",
            packMetadata(resourcePackFormat, resolveProp("modDescription").orEmpty())
        )
        replacementProperties.put("java_version", javaTargetVersion.toString())
        replacementProperties.put(
            "neoforge_icon_property",
            if (stonecutter.eval(mcVersion, ">=26.3")) "iconFile" else "logoFile"
        )

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
        put("modmenu_114", stonecutter.current.project == "1.14.4-fabric")
        put("legacy_modmenu", isFabric && stonecutter.eval(mcVersion, "<=1.15.2"))
        put("neoforge", isNeoforge)
        put("forge", isForge)
        put("forgelike", isForgeLike)
        put("forge_config_gui_handler", isForge && stonecutter.eval(mcVersion, "<1.18"))
        put("forge_config_gui", isForge && stonecutter.eval(mcVersion, ">=1.18") && stonecutter.eval(mcVersion, "<1.19.2"))
        put("forge_config_screen", isForge && stonecutter.eval(mcVersion, ">=1.19.2"))
        put("new_window_handle", stonecutter.eval(mcVersion, ">=1.21.11") || stonecutter.eval(mcVersion, ">=26.1"))
        put("new_set_screen", stonecutter.eval(mcVersion, ">=26.2"))
        put("new_gui_owner", stonecutter.eval(mcVersion, ">=26.2"))
        put("old_minecraft_window_field", stonecutter.eval(mcVersion, "<=1.14.4"))
        put("legacy_string_button", stonecutter.eval(mcVersion, "<=1.15.2"))
        put("legacy_add_button", stonecutter.eval(mcVersion, "<=1.16.5"))
        put("button_builder", stonecutter.eval(mcVersion, ">=1.19.4"))
        put("component_factory", stonecutter.eval(mcVersion, ">=1.19"))
        put("render_extractor", stonecutter.eval(mcVersion, ">=26.1"))
        put("sdl_fullscreen_option", stonecutter.current.project == "26.3-fabric"
                || stonecutter.current.project == "26.3-neoforge")
        put("styled_section_headings", stonecutter.current.project.startsWith("1.21.11")
                || stonecutter.current.project == "26.3-fabric"
                || stonecutter.current.project == "26.3-neoforge")
        put("defer_minimize_after_window_create",
                isFabric && stonecutter.eval(mcVersion, "<=1.16.5"))
        put("deferred_startup_minimize", stonecutter.current.project == "1.14.4-fabric")
        put("defer_loading_fullscreen_attach", stonecutter.eval(mcVersion, "<26.2"))
        put("minimize_after_window_policy", mcVersion == "26.2")
        put("reapply_loading_state_before_window_policy",
                !(isFabric && stonecutter.eval(mcVersion, "<=1.16.5"))
                        && mcVersion != "26.2")
        put("refresh_glfw_after_restore", mcVersion == "26.2")
        put("gui_graphics", stonecutter.eval(mcVersion, ">=1.20") && stonecutter.eval(mcVersion, "<26.1"))
        put("resource_location_factory", stonecutter.eval(mcVersion, ">=1.21") && !stonecutter.current.project.startsWith("1.21.11"))
        put("identifier", stonecutter.current.project.startsWith("1.21.11"))
        put("legacy_menu_list_background", stonecutter.eval(mcVersion, "<1.20"))
        put("legacy_render_system", stonecutter.eval(mcVersion, "<=1.16.5"))
        put("gl_state_manager", mcVersion == "1.14.4")
        put("modern_menu_list_background", stonecutter.eval(mcVersion, ">=1.20.2"))
        put("render_background_delta", stonecutter.eval(mcVersion, ">1.20.1"))
        put("vanilla_title_y20", mcVersion == "1.20.1")
        put("options_screen_subpackage", stonecutter.eval(mcVersion, ">=1.21"))
        put("template_noop", resolveProp("templateNoop")?.toBoolean() == true)
    }
}

// ========== Dependencies ==========
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")

    if (isFabric) {
        modMenuVersion?.let {
            val modMenu = "maven.modrinth:modmenu:$it"
            add("modstitchModCompileOnly", modMenu)
        }
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
    mcpConfigManifest?.let { configuration ->
        withType<NeoFormRuntimeTask>().configureEach {
            addArtifactsToManifest(configuration)
        }
    }

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
        val guiMixinPlaceholder = "\"__gui_mixin_placeholder__\": \"\","
        val mixinRefmapLine = if (isForge) {
            "\"refmap\": \"$resolvedModId.refmap.json\","
        } else {
            ""
        }
        val settingsMixinLine =
            "\"OptionsScreenMixin\", \"ScreenLayoutMixin\", \"WidgetBoundsAccessor\","
        val guiMixinLine = if (stonecutter.eval(mcVersion, ">=26.2")) "\"GuiMixin\"," else ""

        inputs.property("mixin_refmap", mixinRefmapLine)
        inputs.property("settings_mixin", settingsMixinLine)
        inputs.property("gui_mixin", guiMixinLine)
        filesMatching("$resolvedModId.mixins.json") {
            filter { line: String ->
                line.replace(mixinRefmapPlaceholder, mixinRefmapLine)
                    .replace(settingsMixinPlaceholder, settingsMixinLine)
                    .replace(guiMixinPlaceholder, guiMixinLine)
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

fun packMetadata(packFormat: Number, description: String): String {
    val escapedDescription = jsonString(description)
    return if (packFormat.toDouble() > 64) {
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
