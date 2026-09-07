#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

use_build_jvm() {
    local expected="$1"
    local home_variable="JAVA_HOME_${expected}_X64"
    local selected_home="${!home_variable:-${JAVA_HOME:-}}"

    if [[ -z "$selected_home" || ! -x "$selected_home/bin/java" ]]; then
        echo "JDK $expected is required. Set $home_variable or JAVA_HOME to its installation directory." >&2
        exit 1
    fi

    local version_line
    version_line="$("$selected_home/bin/java" -version 2>&1 | head -n 1)"
    if [[ "$expected" == 8 ]]; then
        [[ "$version_line" == *'"1.8.'* ]] || {
            echo "Expected JDK 8 at $selected_home, but found: $version_line" >&2
            exit 1
        }
    elif [[ "$version_line" != *"\"$expected."* && "$version_line" != *"\"$expected\""* ]]; then
        echo "Expected JDK $expected at $selected_home, but found: $version_line" >&2
        exit 1
    fi

    export JAVA_HOME="$selected_home"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "==> Using $version_line"
}

build_port() {
    local port="$1"
    shift
    echo "==> Building ports/$port"
    "$repo_root/ports/$port/gradlew" -p "$repo_root/ports/$port" build "$@"
}
