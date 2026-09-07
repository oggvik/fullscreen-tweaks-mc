#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

output_dir="${1:-release-jars}"
rm -rf "$output_dir"
mkdir -p "$output_dir"

declare -A seen_names=()
declare -A copied_versions=()
count=0

while IFS= read -r -d '' jar; do
    name="$(basename "$jar")"

    case "$name" in
        *-sources.jar|*-dev.jar|*-javadoc.jar)
            continue
            ;;
    esac

    if [[ ! "$name" =~ -([0-9]+[.][0-9]+[.][0-9]+(-[0-9A-Za-z][0-9A-Za-z.-]*)?)([+][^/]+)?[.]jar$ ]]; then
        echo "Unable to determine mod version from jar name: $name" >&2
        echo "Expected a name like modid-0.1.1+target.jar" >&2
        exit 1
    fi

    mod_version="${BASH_REMATCH[1]}"
    version_dir="$output_dir/$mod_version"
    output_name="$mod_version/$name"

    if [[ -n "${seen_names[$output_name]:-}" ]]; then
        echo "Refusing to overwrite duplicate jar name: $output_name" >&2
        echo "First: ${seen_names[$output_name]}" >&2
        echo "Second: $jar" >&2
        exit 1
    fi

    mkdir -p "$version_dir"
    seen_names[$output_name]="$jar"
    copied_versions[$mod_version]=1
    cp "$jar" "$version_dir/$name"
    count=$((count + 1))
done < <(find versions ports -path '*/build/libs/*.jar' -type f -print0 | sort -z)

if (( count == 0 )); then
    echo "No release jars found. Build targets first, for example: scripts/build-all-targets.sh" >&2
    exit 1
fi

if (( ${#copied_versions[@]} == 1 )); then
    for mod_version in "${!copied_versions[@]}"; do
        echo "Copied $count release jars to $output_dir/$mod_version/"
    done
else
    echo "Copied $count release jars across ${#copied_versions[@]} mod-version directories under $output_dir/"
fi
