#!/usr/bin/env bash

# SCRUM Programming Language SDK Installer (Unix/Linux/macOS)
# Supports: Linux, macOS, BSD

set -e

echo "================================================================"
echo "  SCRUM Programming Language SDK Installer"
echo "================================================================"
echo ""

# Determine SDK root directory (parent of installers folder)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK_ROOT="$(dirname "$SCRIPT_DIR")"
echo "Installing SCRUM SDK from: $SDK_ROOT"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed or not in PATH"
    echo ""
    echo "SCRUM requires Java 21 or higher."
    echo "Installation instructions:"
    echo "  - Ubuntu/Debian: sudo apt install openjdk-21-jdk"
    echo "  - Fedora/RHEL:   sudo dnf install java-21-openjdk"
    echo "  - macOS:         brew install openjdk@21"
    echo "  - Or download:   https://adoptium.net/"
    echo ""
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2)
JAVA_MAJOR=$(echo "$JAVA_VERSION" | cut -d'.' -f1)

if [ "$JAVA_MAJOR" -lt 21 ]; then
    echo "[ERROR] SCRUM requires Java 21 or higher"
    echo "Current Java version: $JAVA_VERSION"
    echo "Download Java 21+ from: https://adoptium.net/"
    echo ""
    exit 1
fi

echo "[OK] Java $JAVA_VERSION detected"
echo ""

# Determine default installation directory
if [ "$EUID" -eq 0 ] || [ "$(id -u)" -eq 0 ]; then
    # Running as root, suggest system-wide installation
    DEFAULT_INSTALL_DIR="/opt/scrum"
    IS_SYSTEM_INSTALL=true
else
    # Running as regular user, suggest user installation
    DEFAULT_INSTALL_DIR="$HOME/.local/scrum"
    IS_SYSTEM_INSTALL=false
fi

# Ask for installation directory
echo "Installation directory [$DEFAULT_INSTALL_DIR]: "
read -r INSTALL_DIR
if [ -z "$INSTALL_DIR" ]; then
    INSTALL_DIR="$DEFAULT_INSTALL_DIR"
fi

echo ""
echo "Installation directory: $INSTALL_DIR"
echo ""

# Check write permissions
if [ ! -d "$INSTALL_DIR" ]; then
    PARENT_DIR="$(dirname "$INSTALL_DIR")"
    if [ ! -w "$PARENT_DIR" ]; then
        echo "[ERROR] No write permission for $PARENT_DIR"
        echo "Please run with sudo or choose a different location"
        echo ""
        exit 1
    fi
fi

# Create installation directory
if [ ! -d "$INSTALL_DIR" ]; then
    mkdir -p "$INSTALL_DIR"
    echo "[OK] Created installation directory"
else
    echo "[INFO] Installation directory already exists"
fi
echo ""

# Copy SDK files
echo "Copying SDK files..."
cp -r "$SDK_ROOT/bin" "$INSTALL_DIR/"
cp -r "$SDK_ROOT/lib" "$INSTALL_DIR/"
cp -r "$SDK_ROOT/examples" "$INSTALL_DIR/"
cp -r "$SDK_ROOT/docs" "$INSTALL_DIR/"
cp "$SDK_ROOT/LICENSE" "$INSTALL_DIR/"
cp "$SDK_ROOT/README.md" "$INSTALL_DIR/"

# Make scripts executable
chmod +x "$INSTALL_DIR/bin/scrum.sh"
chmod +x "$INSTALL_DIR/bin/scrum.bat"

echo "[OK] SDK files copied"
echo ""

# Determine shell configuration file
SHELL_RC=""
if [ -n "$BASH_VERSION" ]; then
    if [ -f "$HOME/.bashrc" ]; then
        SHELL_RC="$HOME/.bashrc"
    elif [ -f "$HOME/.bash_profile" ]; then
        SHELL_RC="$HOME/.bash_profile"
    fi
elif [ -n "$ZSH_VERSION" ]; then
    SHELL_RC="$HOME/.zshrc"
else
    # Try to detect from SHELL environment variable
    case "$SHELL" in
        */bash)
            SHELL_RC="$HOME/.bashrc"
            ;;
        */zsh)
            SHELL_RC="$HOME/.zshrc"
            ;;
        */fish)
            SHELL_RC="$HOME/.config/fish/config.fish"
            ;;
    esac
fi

# Set environment variables
echo "Setting environment variables..."

ENV_SETUP="
# SCRUM Programming Language SDK
export SCRUM_HOME=\"$INSTALL_DIR\"
export PATH=\"\$SCRUM_HOME/bin:\$PATH\"
"

if [ -n "$SHELL_RC" ]; then
    # Check if already configured
    if grep -q "SCRUM_HOME" "$SHELL_RC" 2>/dev/null; then
        echo "[INFO] SCRUM environment variables already configured in $SHELL_RC"
    else
        # Append to shell config
        echo "$ENV_SETUP" >> "$SHELL_RC"
        echo "[OK] Added SCRUM environment variables to $SHELL_RC"
    fi
else
    echo "[WARNING] Could not detect shell configuration file"
    echo "Please add the following to your shell configuration file manually:"
    echo "$ENV_SETUP"
fi
echo ""

# Create symbolic link (system-wide installation)
if [ "$IS_SYSTEM_INSTALL" = true ]; then
    echo "Creating symbolic link in /usr/local/bin..."
    if [ -w "/usr/local/bin" ] || [ "$EUID" -eq 0 ]; then
        ln -sf "$INSTALL_DIR/bin/scrum.sh" /usr/local/bin/scrum
        echo "[OK] Created symlink: /usr/local/bin/scrum"
    else
        echo "[WARNING] No write permission for /usr/local/bin"
        echo "Run the following command manually:"
        echo "  sudo ln -sf \"$INSTALL_DIR/bin/scrum.sh\" /usr/local/bin/scrum"
    fi
    echo ""
fi

# Create uninstall script
echo "Creating uninstall script..."
cat > "$INSTALL_DIR/uninstall.sh" << 'EOF'
#!/usr/bin/env bash

echo "Uninstalling SCRUM Programming Language SDK..."
echo ""

# Remove installation directory
INSTALL_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -d "$INSTALL_DIR" ]; then
    rm -rf "$INSTALL_DIR"
    echo "[OK] Removed installation directory: $INSTALL_DIR"
else
    echo "[WARNING] Installation directory not found"
fi

# Remove symbolic link
if [ -L "/usr/local/bin/scrum" ]; then
    sudo rm -f /usr/local/bin/scrum
    echo "[OK] Removed symbolic link: /usr/local/bin/scrum"
fi

echo ""
echo "[INFO] Uninstallation complete."
echo "[INFO] Please remove SCRUM environment variables from your shell configuration file manually"
echo ""
EOF

chmod +x "$INSTALL_DIR/uninstall.sh"
echo "[OK] Uninstall script created"
echo ""

# Installation complete
echo "================================================================"
echo "  Installation Complete!"
echo "================================================================"
echo ""
echo "SCRUM has been installed to: $INSTALL_DIR"
echo ""
echo "IMPORTANT: Please restart your terminal or run:"
if [ -n "$SHELL_RC" ]; then
    echo "  source $SHELL_RC"
else
    echo "  export SCRUM_HOME=\"$INSTALL_DIR\""
    echo "  export PATH=\"\$SCRUM_HOME/bin:\$PATH\""
fi
echo ""
echo "You can now run SCRUM programs with:"
echo "  scrum examples/HelloWorld.scrum"
echo ""
echo "To check the installation:"
echo "  scrum --version"
echo ""
echo "To uninstall, run:"
echo "  $INSTALL_DIR/uninstall.sh"
echo ""
