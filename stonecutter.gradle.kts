// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

plugins {
    base

    id("dev.kikugie.stonecutter")

    val modstitchVersion = "0.8.5" // Source: https://github.com/isXander/modstitch
    id("dev.isxander.modstitch.base") version modstitchVersion apply false
}
stonecutter active "1.21.1-fabric"

allprojects {
    repositories {
        maven("https://maven.isxander.dev/releases")
        maven("https://maven.isxander.dev/snapshots")
    }
}
