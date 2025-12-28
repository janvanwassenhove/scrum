#!/usr/bin/env bash

# SCRUM Programming Language Launcher Script
# Determines SCRUM_HOME dynamically and launches SCRUM programs

# Determine SCRUM_HOME from script location
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export SCRUM_HOME="$SCRIPT_DIR"

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "SCRUM requires Java 21 or higher"
    echo "Download from: https://adoptium.net/"
    exit 1
fi

# Check Java version (require 21+)
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "Error: SCRUM requires Java 21 or higher"
    echo "Current Java version: $(java -version 2>&1 | head -1)"
    echo "Download Java 21+ from: https://adoptium.net/"
    exit 1
fi

# Check if JAR file exists
JAR_FILE="$SCRUM_HOME/scrum.jar"
if [ ! -f "$JAR_FILE" ]; then
    # Try lib directory (SDK distribution layout)
    JAR_FILE="$SCRUM_HOME/../lib/scrum-"*.jar
    if [ ! -f $JAR_FILE ]; then
        echo "Error: scrum.jar not found"
        echo "Expected location: $SCRUM_HOME/scrum.jar"
        echo "Or: $SCRUM_HOME/../lib/scrum-*.jar"
        exit 1
    fi
fi

# Launch SCRUM with all provided arguments
java -jar "$JAR_FILE" "$@"
