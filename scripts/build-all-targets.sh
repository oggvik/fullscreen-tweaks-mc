#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

"$script_dir/build-jvm-8.sh" "$@"
"$script_dir/build-jvm-17.sh" "$@"
"$script_dir/build-jvm-21.sh" "$@"
"$script_dir/build-jvm-25.sh" "$@"
