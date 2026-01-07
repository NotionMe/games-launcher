#!/bin/bash

# Script to launch Windows executables using Proton
# Arguments:
# $1: Full path to the Proton version root directory (e.g. /home/user/.local/share/Steam/compatibilitytools.d/GE-Proton8-9)
# $2: Full path to the executable (.exe) to launch
# $3: Full path to the Wine prefix (pfx directory) to use/create
# $4: WINEDLLOVERRIDES string (optional, e.g. "ver=n,b")

PROTON_ROOT="$1"
EXE_PATH="$2"
PREFIX="$3"
DLL_OVERRIDES="$4"

# Basic validation
if [ -z "$PROTON_ROOT" ] || [ -z "$EXE_PATH" ] || [ -z "$PREFIX" ]; then
    echo "Usage: $0 <proton_root> <exe_path> <prefix_path> [dll_overrides]"
    echo "Example: $0 /path/to/proton /path/to/game.exe /path/to/pfx \"winmm=n,b\""
    exit 1
fi

# Ensure the prefix directory parent exists
mkdir -p "$(dirname "$PREFIX")"

# Environment Variables required by Proton
export STEAM_COMPAT_CLIENT_INSTALL_PATH="$HOME/.local/share/Steam"
export STEAM_COMPAT_DATA_PATH="$PREFIX"
# AppID is often required by Proton logic. Using 0 or a dummy value.
export STEAM_COMPAT_APP_ID=0 
export PROTON_LOG=1

if [ -n "$DLL_OVERRIDES" ]; then
    export WINEDLLOVERRIDES="$DLL_OVERRIDES"
fi
# Determine the proton executable
PROTON_EXEC="$PROTON_ROOT/proton"

if [ ! -f "$PROTON_EXEC" ]; then
    echo "Error: Proton executable not found at $PROTON_EXEC"
    exit 1
fi

echo "-----------------------------------------------------"
echo "Launching EXE: $EXE_PATH"
echo "Proton Root:   $PROTON_ROOT"
echo "Wine Prefix:   $PREFIX"
if [ -n "$DLL_OVERRIDES" ]; then
    echo "DLL Overrides: $DLL_OVERRIDES"
fi
echo "-----------------------------------------------------"

# Execute
"$PROTON_EXEC" run "$EXE_PATH"
