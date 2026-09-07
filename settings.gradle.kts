// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.isxander.dev/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.4" // Source: https://codeberg.org/stonecutter/stonecutter
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        fun mc(mcVersion: String, name: String = mcVersion, loaders: Iterable<String>) =
            loaders.forEach { version("$name-$it", mcVersion) }

        mc("1.14.4", loaders = listOf("fabric"))
        mc("1.15.2", loaders = listOf("fabric"))
        mc("1.16.5", loaders = listOf("fabric"))
        mc("1.17.1", loaders = listOf("fabric", "forge"))
        mc("1.18.2", loaders = listOf("fabric", "forge"))
        mc("1.19.2", loaders = listOf("fabric", "forge"))
        mc("1.19.4", loaders = listOf("fabric", "forge"))
        mc("1.20.1", loaders = listOf("fabric", "forge"))
        mc("1.20.6", loaders = listOf("fabric", "neoforge"))
        mc("1.21.1", loaders = listOf("fabric", "neoforge"))
        mc("1.21.11", loaders = listOf("fabric", "neoforge"))
        mc("26.1.2", loaders = listOf("fabric", "neoforge"))
        mc("26.2", loaders = listOf("fabric", "neoforge"))
        mc("26.3-snapshot-1", loaders = listOf("fabric"))
        mc("26.3-snapshot-2", loaders = listOf("fabric"))
        mc("26.3-snapshot-3", loaders = listOf("fabric"))
        mc("26.3-snapshot-4", loaders = listOf("fabric"))
    }
}
rootProject.name = "Stop Minimizing on Focus Loss"
