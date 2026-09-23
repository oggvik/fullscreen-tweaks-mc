#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/java-build.sh"

use_build_jvm 25

echo "==> Building Stonecutter targets"
"$repo_root/gradlew" -p "$repo_root" build "$@"

ports=(
    1.20.1-neoforge
    bta-babric-7.3
)

for port in "${ports[@]}"; do
    build_port "$port" "$@"
done
