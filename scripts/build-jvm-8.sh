#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/java-build.sh"

use_build_jvm 8

for port in 1.7.10-forge 1.8.9-forge 1.12.2-forge; do
    if [[ -x "$repo_root/ports/$port/gradlew" ]]; then
        build_port "$port" "$@"
    else
        echo "==> Skipping ports/$port (not present in this checkout)"
    fi
done
