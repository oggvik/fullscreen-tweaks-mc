#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/java-build.sh"

use_build_jvm 21

build_port 1.20.6-forge "$@"
build_port bta-babric-8.0 "$@"
