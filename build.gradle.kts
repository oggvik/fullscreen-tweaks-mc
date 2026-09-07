// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

import org.gradle.api.tasks.compile.JavaCompile
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

val fabricModMenuRuntimeVersions = mapOf(
    "1.14.4" to "1.7.17",
    "1.15.2" to "1.10.7",
    "1.16.5" to "1.16.23",
    "1.17.1" to "2.0.17",
    "1.18.2" to "3.2.5",
    "1.19.2" to "4.2.0-beta.2",
    "1.19.4" to "6.3.1",
    "1.20.1" to "7.2.2",
    "1.20.6" to "10.0.0",
    "1.21.1" to "11.0.4",
    "1.21.11" to "17.0.0",
    "26.1.2" to "18.0.0-beta.1",
    "26.2" to "20.0.0-beta.4",
)

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
        put("template_noop", resolveProp("templateNoop")?.toBoolean() == true)
    }
}

// ========== Dependencies ==========
dependencies {
    if (isFabric) {
        fabricModMenuRuntimeVersions[mcVersion]?.let { modMenuVersion ->
            add("modstitchLocalRuntime", "maven.modrinth:modmenu:$modMenuVersion")
        }
    }
}

// ========== Tasks ==========
tasks {
    withType<JavaCompile>().configureEach {
        dependsOn("stonecutterGenerate")
    }

    withType<ProcessResources>().configureEach {
        val mixinRefmapPlaceholder = "\"__mixin_refmap_placeholder__\": \"\","
        val mixinRefmapLine = if (isForge) {
            "\"refmap\": \"$resolvedModId.refmap.json\","
        } else {
            ""
        }

        inputs.property("mixin_refmap", mixinRefmapLine)
        filesMatching("$resolvedModId.mixins.json") {
            filter { line: String -> line.replace(mixinRefmapPlaceholder, mixinRefmapLine) }
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
