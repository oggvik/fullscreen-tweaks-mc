#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Oggvik
# SPDX-License-Identifier: AGPL-3.0-or-later

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

versions=(
    1.14.4
    1.15.2
    1.16.5
    1.17.1
    1.18.2
    1.19.2
    1.19.4
    1.20.1
    1.20.6
    1.21.1
    1.21.11
    26.1.2
    26.2
    26.3-snapshot-1
    26.3-snapshot-2
    26.3-snapshot-3
)

for minecraft_version in "${versions[@]}"; do
    generated_root="versions/${minecraft_version}-fabric/build/generated/stonecutter/main/java/oggvik/mods/stopminimizingonfocusloss"
    generated_mixin="versions/${minecraft_version}-fabric/build/resources/main/stop_minimizing_on_focus_loss.mixins.json"
    port_root="ports/${minecraft_version}-quilt"
    target_root="${port_root}/src/main/java/oggvik/mods/stopminimizingonfocusloss"

    if [[ ! -d "$generated_root" || ! -f "$generated_mixin" ]]; then
        echo "Missing generated ${minecraft_version} sources; run ./gradlew build first" >&2
        exit 1
    fi

    echo "==> Synchronizing ${port_root}"
    rm -rf "$target_root/config" "$target_root/window" "$target_root/client"
    mkdir -p "$target_root/platform" "$target_root/mixins"
    cp -R "$generated_root/config" "$generated_root/window" "$generated_root/client" "$target_root/"
    cp "$generated_root/StopMinimizingOnFocusLoss.java" "$target_root/"
    cp "$generated_root/platform/MinecraftWindowBridge.java" "$target_root/platform/"
    cp "$generated_root/mixins/WindowMixin.java" "$generated_root/mixins/OptionsScreenMixin.java" \
        "$generated_root/mixins/ScreenLayoutMixin.java" "$generated_root/mixins/WidgetBoundsAccessor.java" \
        "$target_root/mixins/"
    cp "$generated_mixin" "$port_root/src/main/resources/stop_minimizing_on_focus_loss.mixins.json"
done
