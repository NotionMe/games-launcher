#!/usr/bin/env bash
set -euo pipefail

# Always run from the project root so Maven picks up pom.xml and resources.
cd "$(dirname "$0")"

MVN_BIN="${MVN_BIN:-mvn}"

if ! command -v "$MVN_BIN" >/dev/null 2>&1; then
  echo "Maven не знайдено. Встановіть Maven або передайте шлях у змінній MVN_BIN." >&2
  exit 1
fi

# Launches the JavaFX app defined in pom.xml (ua.notion.App).
exec "$MVN_BIN" -DskipTests javafx:run
