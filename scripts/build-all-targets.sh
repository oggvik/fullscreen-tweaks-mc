#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

echo "==> Building Stonecutter targets"
./gradlew build "$@"

mapfile -d '' port_wrappers < <(find ports -mindepth 2 -maxdepth 2 -name gradlew -type f -print0 | sort -z)

for wrapper in "${port_wrappers[@]}"; do
    port_dir="$(dirname "$wrapper")"
    echo "==> Building ${port_dir}"
    gradle_java_args=()
    if [[ -f "${port_dir}/gradle.properties" ]]; then
        java_version="$(sed -n 's/^org\.gradle\.java\.version=//p' "${port_dir}/gradle.properties" | head -n 1)"
        java_home="$(sed -n 's/^org\.gradle\.java\.home=//p' "${port_dir}/gradle.properties" | head -n 1)"
        if [[ -n "$java_version" ]]; then
            java_home_env="JAVA_HOME_${java_version}_X64"
            if [[ -n "${!java_home_env:-}" ]]; then
                gradle_java_args=("-Dorg.gradle.java.home=${!java_home_env}")
            fi
        elif [[ -n "${JAVA_HOME_17_X64:-}" && -n "$java_home" ]]; then
            gradle_java_args=("-Dorg.gradle.java.home=${JAVA_HOME_17_X64}")
        fi
    fi
    "$wrapper" "${gradle_java_args[@]}" -p "$port_dir" build "$@"
done
