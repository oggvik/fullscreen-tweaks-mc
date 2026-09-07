#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/java-build.sh"

use_build_jvm 25

echo "==> Building Stonecutter targets"
"$repo_root/gradlew" -p "$repo_root" build "$@"

ports=(
    1.14.4-quilt
    1.15.2-quilt
    1.16.5-quilt
    1.17.1-quilt
    1.18.2-quilt
    1.19.2-quilt
    1.19.4-quilt
    1.20.1-neoforge
    1.20.1-quilt
    1.20.6-quilt
    1.21.1-quilt
    1.21.11-quilt
    26.1.2-quilt
    26.2-quilt
    26.3-snapshot-1-quilt
    26.3-snapshot-2-quilt
    26.3-snapshot-3-quilt
    bta-babric-7.3
)

for port in "${ports[@]}"; do
    build_port "$port" "$@"
done
