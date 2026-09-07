<!--
SPDX-FileCopyrightText: 2026 Oggvik
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Stop Minimizing on Focus Loss - Babric b1.7.3 Port

Standalone Babric b1.7.3 build using `babric-loom`.

Build:

```bash
./gradlew build
```

Run client:

```bash
./gradlew runClient
```

Note: vanilla b1.7.3 does not expose the relevant native fullscreen behavior in normal use. This port is kept as an experimental no-op build for compatibility testing rather than a meaningful fix target.
