#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/lib/java-build.sh"

use_build_jvm 8

build_port 1.7.10-forge "$@"
build_port 1.8.9-forge "$@"
build_port 1.12.2-forge "$@"
