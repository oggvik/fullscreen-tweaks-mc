#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

use_build_jvm() {
    local expected="$1"
    local home_variable="JAVA_HOME_${expected}_X64"
    local selected_home="${!home_variable:-}"

    if [[ -z "$selected_home" && -n "${JAVA_HOME:-}" ]] && matches_build_jvm "$expected" "$JAVA_HOME"; then
        selected_home="$JAVA_HOME"
    fi

    if [[ -z "$selected_home" ]]; then
        local candidate
        for candidate in "$HOME"/.jdks/* "$HOME"/.gradle/jdks/* /usr/lib/jvm/*; do
            if matches_build_jvm "$expected" "$candidate"; then
                selected_home="$candidate"
                break
            fi
        done
    fi

    if [[ -z "$selected_home" || ! -x "$selected_home/bin/java" ]]; then
        echo "JDK $expected is required. Set $home_variable or install it under ~/.jdks, ~/.gradle/jdks, or /usr/lib/jvm." >&2
        exit 1
    fi

    local version_line
    version_line="$("$selected_home/bin/java" -version 2>&1 | head -n 1)"
    if ! matches_build_jvm "$expected" "$selected_home"; then
        echo "Expected JDK $expected at $selected_home, but found: $version_line" >&2
        exit 1
    fi

    export JAVA_HOME="$selected_home"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "==> Using $version_line"
}

matches_build_jvm() {
    local expected="$1"
    local candidate="$2"
    [[ -x "$candidate/bin/java" ]] || return 1

    local version_line
    version_line="$("$candidate/bin/java" -version 2>&1 | head -n 1)" || return 1
    if [[ "$expected" == 8 ]]; then
        [[ "$version_line" == *'"1.8.'* ]]
    else
        [[ "$version_line" == *"\"$expected."* || "$version_line" == *"\"$expected\""* ]]
    fi
}

build_port() {
    local port="$1"
    shift
    echo "==> Building ports/$port"
    "$repo_root/ports/$port/gradlew" -p "$repo_root/ports/$port" build "$@"
}
