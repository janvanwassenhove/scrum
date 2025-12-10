#!/usr/bin/env bash
# Build and Install SCRUM Language Plugin for IntelliJ IDEA (Linux/Mac)

echo "Building SCRUM Language Plugin for IntelliJ IDEA..."
echo ""

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PLUGIN_DIR="$SCRIPT_DIR/intellij-scrum"

cd "$PLUGIN_DIR"

# Check if Gradle wrapper exists
if [ -f "./gradlew" ]; then
    echo "Using Gradle wrapper to build plugin..."
    
    ./gradlew buildPlugin
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✓ Plugin built successfully!"
        echo ""
        
        DIST_DIR="$PLUGIN_DIR/build/distributions"
        if [ -d "$DIST_DIR" ]; then
            ZIP_FILE=$(find "$DIST_DIR" -name "*.zip" -type f | head -n 1)
            if [ -n "$ZIP_FILE" ]; then
                echo "Plugin package created: $ZIP_FILE"
                echo ""
            fi
        fi
        
        echo "To install the plugin:"
        echo "1. Open IntelliJ IDEA"
        echo "2. Go to Settings (Ctrl+Alt+S on Linux, Cmd+, on Mac) -> Plugins"
        echo "3. Click the gear icon ⚙️ -> Install Plugin from Disk"
        echo "4. Select the .zip file from: build/distributions/"
        echo "5. Restart IntelliJ IDEA"
        echo ""
    else
        echo ""
        echo "✗ Build failed!"
        echo "Check the output above for errors."
        echo ""
    fi
else
    echo "Gradle wrapper not found."
    echo ""
    echo "To build the plugin, you have several options:"
    echo ""
    echo "Option 1: Use IntelliJ IDEA (Recommended)"
    echo "  1. Open the 'intellij-scrum' folder in IntelliJ IDEA"
    echo "  2. Ensure 'Plugin DevKit' plugin is installed"
    echo "  3. Run -> Run 'Plugin' to test in sandbox IDE"
    echo "  4. Build -> Build Project"
    echo "  5. Build -> Prepare Plugin Module for Deployment"
    echo ""
    echo "Option 2: Install Gradle"
    echo "  1. Install Gradle from your package manager or https://gradle.org/install/"
    echo "  2. Run: gradle wrapper"
    echo "  3. Run: ./gradlew buildPlugin"
    echo ""
    echo "Option 3: Manual Compilation"
    echo "  See README.md for manual compilation instructions"
    echo ""
fi

cd "$SCRIPT_DIR"

read -p "Press any key to continue..."
