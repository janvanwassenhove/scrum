# SCRUM SDK Installation Guide

Complete guide to installing, configuring, upgrading, and uninstalling the SCRUM Programming Language SDK.

## Table of Contents

- [System Requirements](#system-requirements)
- [Installation](#installation)
  - [Windows Installation](#windows-installation)
  - [Linux/macOS Installation](#linuxmacos-installation)
- [Verification](#verification)
- [Configuration](#configuration)
- [Upgrading](#upgrading)
- [Uninstallation](#uninstallation)
- [Troubleshooting](#troubleshooting)
- [Building from Source](#building-from-source)

---

## System Requirements

### Minimum Requirements

- **Java**: Version 25 or higher (LTS recommended)
- **Operating System**: 
  - Windows 10 or later
  - Linux (any modern distribution)
  - macOS 10.15 (Catalina) or later
- **Disk Space**: ~50 MB for SDK installation
- **Memory**: 512 MB RAM minimum, 1 GB recommended

### Java Installation

If Java is not installed, download from:
- **Eclipse Temurin (Recommended)**: [https://adoptium.net/](https://adoptium.net/)
- **Oracle JDK**: [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
- **OpenJDK**: [https://openjdk.org/](https://openjdk.org/)

#### Quick Install Commands

**Ubuntu/Debian**:
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

**Fedora/RHEL**:
```bash
sudo dnf install java-25-openjdk
```

**macOS (Homebrew)**:
```bash
brew install openjdk@21
```

**Windows (Chocolatey)**:
```powershell
choco install temurin21
```

---

## Installation

### Windows Installation

#### Option 1: Automated Installation (Recommended)

1. **Download the SDK**
   - Get `scrum-language-<version>-sdk.zip` from [GitHub Releases](https://github.com/janvanwassenhove/scrum/releases)

2. **Extract the Archive**
   - Right-click → Extract All
   - Choose a temporary location (e.g., `C:\Temp\scrum-language-1.3.0`)

3. **Run Installer**
   
   **Using Batch Script**:
   ```cmd
   cd C:\Temp\scrum-language-1.3.0
   
   REM User installation (AppData)
   installers\install.bat
   
   REM System-wide installation (requires admin)
   installers\install.bat --system-wide
   
   REM Custom directory
   installers\install.bat C:\scrum
   ```
   
   **Using PowerShell**:
   ```powershell
   cd C:\Temp\scrum-language-1.3.0
   
   # User installation (AppData)
   .\installers\install.ps1
   
   # System-wide installation (requires admin)
   .\installers\install.ps1 -SystemWide
   
   # Custom directory
   .\installers\install.ps1 -InstallDir C:\scrum
   ```

4. **Follow Prompts**
   - Default installation: `C:\Program Files\SCRUM`
   - Or specify custom location

5. **Restart Terminal**
   - Close and reopen Command Prompt or PowerShell
   - Environment variables take effect

#### Option 2: Manual Installation

1. **Extract SDK** to desired location (e.g., `C:\scrum`)

2. **Set Environment Variables**:
   - Open **System Properties** → **Environment Variables**
   - Add **User Variable**:
     - Name: `SCRUM_HOME`
     - Value: `C:\scrum` (your installation path)
   - Edit **PATH** variable:
     - Add: `%SCRUM_HOME%\bin`

3. **Verify**:
   ```cmd
   scrum --version
   ```

---

### Linux/macOS Installation

#### Option 1: Automated Installation (Recommended)

1. **Download the SDK**
   ```bash
   wget https://github.com/janvanwassenhove/scrum/releases/download/v1.3.0/scrum-language-1.3.0-sdk.zip
   ```

2. **Extract the Archive**
   ```bash
   unzip scrum-language-1.3.0-sdk.zip
   cd scrum-language-1.3.0
   ```

3. **Run Installer**
   
   **System-wide installation** (requires sudo):
   ```bash
   sudo ./installers/install.sh
   # Installs to: /opt/scrum
   # Creates symlink: /usr/local/bin/scrum
   ```
   
   **User installation** (no sudo):
   ```bash
   ./installers/install.sh
   # Installs to: ~/.local/scrum
   # Updates: ~/.bashrc or ~/.zshrc
   ```

4. **Reload Shell Configuration**
   ```bash
   source ~/.bashrc  # or ~/.zshrc for Zsh
   ```

#### Option 2: Manual Installation

1. **Extract SDK**:
   ```bash
   unzip scrum-language-1.3.0-sdk.zip
   sudo mv scrum-language-1.3.0 /opt/scrum
   ```

2. **Set Environment Variables**:
   
   Edit `~/.bashrc` (or `~/.zshrc` for Zsh):
   ```bash
   # SCRUM Programming Language SDK
   export SCRUM_HOME="/opt/scrum"
   export PATH="$SCRUM_HOME/bin:$PATH"
   ```

3. **Make Scripts Executable**:
   ```bash
   chmod +x /opt/scrum/bin/scrum.sh
   ```

4. **Create Symlink** (optional):
   ```bash
   sudo ln -sf /opt/scrum/bin/scrum.sh /usr/local/bin/scrum
   ```

5. **Reload Configuration**:
   ```bash
   source ~/.bashrc
   ```

---

## Verification

After installation, verify the SDK is correctly installed:

### Basic Verification

```bash
# Check version and environment info
scrum --version

# Expected output:
# ╔════════════════════════════════════════════════════════════════╗
# ║  SCRUM Programming Language                                    ║
# ╚════════════════════════════════════════════════════════════════╝
# 
# Version:        1.3.0
# Build Date:     2025-12-27
# Java Version:   25.0.1
# Java Vendor:    Oracle Corporation  
# Java Home:      C:\Program Files\Java\jdk-25
# SCRUM_HOME:     C:\scrum
# 
# Copyright (c) 2023-2025 Jan Van Wassenhove
# License: See LICENSE file in distribution
```

### Test CLI Features

```bash
# Get help information
scrum --help

# View available examples
scrum --examples

# Validate syntax of an example (without running)
scrum --validate $SCRUM_HOME/examples/HelloWorld.scrum

# Expected output:
# ✅ Syntax validation successful for: [path]/HelloWorld.scrum
#    No syntax errors found. The file is ready for execution.
```

### Run Example Program

```bash
# Run HelloWorld
scrum $SCRUM_HOME/examples/HelloWorld.scrum

# Expected output:
# Hello world!

# Run with debug mode for detailed output
scrum --debug $SCRUM_HOME/examples/HelloWorld.scrum
```

Or navigate to the examples directory:

```bash
cd $SCRUM_HOME/examples
scrum HelloWorld.scrum

# Test different examples
scrum OddOrNot.scrum
scrum --validate ApiExample.scrum
```

### Full Feature Test

```bash
# Test AI-powered features (requires API key)
export SCRUM_API_KEY="your-openai-api-key"
scrum IntentComputeBirthYear.scrum

# Test API integration
scrum ApiExample.scrum

# Test syntax validation for CI/CD workflows
scrum --validate *.scrum
```

---

## Configuration

### Environment Variables

The SCRUM SDK uses the following environment variables:

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `SCRUM_HOME` | SDK installation directory | **Yes** | None |
| `SCRUM_API_KEY` | LLM provider API key (for #INTENT blocks) | No | None |
| `SCRUM_LLM_PROVIDER` | Preferred LLM provider | No | `auto` |
| `SCRUM_LLM_FALLBACK_CHAIN` | Provider fallback chain | No | `auto,groq,ollama` |

For LLM configuration details, see [LLM-CONFIGURATION.md](LLM-CONFIGURATION.md).

### Debug Mode

Enable detailed error traces:

**Windows**:
```cmd
set JAVA_OPTS=-Dscrum.debug=true
scrum myprogram.scrum
```

**Linux/macOS**:
```bash
export JAVA_OPTS="-Dscrum.debug=true"
scrum myprogram.scrum
```

Or run directly:
```bash
java -Dscrum.debug=true -jar $SCRUM_HOME/lib/scrum-1.3.0.jar myprogram.scrum
```

---

## Upgrading

### Quick Upgrade (Recommended)

The easiest way to upgrade is to download the new version and run the installer, which will automatically handle the upgrade:

**Windows**:
```cmd
# Download new SDK and extract
cd scrum-language-<new-version>

# Run installer (will backup and replace existing installation)
installers\install.bat

# Verify upgrade
scrum --version
```

**Linux/macOS**:
```bash
# Download and extract new SDK
tar -xzf scrum-language-<new-version>-sdk.tar.gz
cd scrum-language-<new-version>

# Install (will upgrade existing installation)
sudo ./installers/install.sh

# Verify upgrade
scrum --version
```

### Side-by-Side Installation

For testing new versions alongside existing ones:

**Windows**:
```powershell
# Install to different directory
.\installers\install.ps1 -InstallDir "C:\scrum-beta"

# Switch versions by updating SCRUM_HOME
$env:SCRUM_HOME = "C:\scrum-beta"    # Use beta version
$env:SCRUM_HOME = "C:\scrum"         # Use stable version
```

**Linux/macOS**:
```bash
# Install to different directory
./installers/install.sh --install-dir /opt/scrum-beta

# Switch versions
export SCRUM_HOME="/opt/scrum-beta"  # Use beta version
export SCRUM_HOME="/opt/scrum"       # Use stable version
```

### Clean Upgrade (Manual)

1. **Check Current Version**
   ```bash
   scrum --version
   ```

2. **Backup Configuration**
   - Save any custom environment variables (API keys, etc.)
   - Note your current `SCRUM_HOME` path

3. **Uninstall Current Version**
   
   **Windows**:
   ```cmd
   %SCRUM_HOME%\uninstall.ps1
   ```
   
   **Linux/macOS**:
   ```bash
   $SCRUM_HOME/uninstall.sh
   ```

4. **Download New Version**
   - Get latest SDK from [GitHub Releases](https://github.com/janvanwassenhove/scrum/releases)

5. **Install New Version**
   - Follow installation steps for your platform

6. **Verify Upgrade**
   ```bash
   scrum --version
   scrum --help
   scrum --examples
   ```

7. **Restore Configuration**
   - Re-set API keys and custom environment variables

### Pre-Release Versions

To test alpha, beta, or release candidate versions:

```bash
# Download pre-release from GitHub Releases
# Look for versions like: 1.4.0-alpha, 1.4.0-beta, 1.4.0-rc1

# Install to separate directory
# Windows:
installers\install.ps1 -InstallDir "C:\scrum-alpha"

# Linux/macOS:
./installers/install.sh --install-dir /opt/scrum-alpha

# Use pre-release version
export SCRUM_HOME="/opt/scrum-alpha"  # Linux/macOS
$env:SCRUM_HOME = "C:\scrum-alpha"    # Windows

# Test new features
scrum --version
scrum --help
```

### Version Management

**List Installed Versions**:
```bash
# Windows:
dir C:\scrum*
dir "%LOCALAPPDATA%\Programs\SCRUM*"

# Linux/macOS:
ls /opt/scrum*
ls ~/.local/scrum*
```

**Switch Between Versions**:
```bash
# Temporarily switch (current session only)
# Windows PowerShell:
$env:SCRUM_HOME = "C:\scrum-1.3.0"
$env:SCRUM_HOME = "C:\scrum-1.4.0-beta"

# Linux/macOS:
export SCRUM_HOME="/opt/scrum-1.3.0"
export SCRUM_HOME="/opt/scrum-1.4.0-beta"

# Permanently switch by updating system environment variables
```

### New CLI Features (v1.4.0+)

Recent versions include enhanced command-line interface:

```bash
# Get comprehensive help
scrum --help

# View available examples with sample code
scrum --examples

# Validate syntax without execution (perfect for CI/CD)
scrum --validate myprogram.scrum

# Debug mode with detailed error traces
scrum --debug myprogram.scrum

# Version info with environment details
scrum --version
```

---

## Uninstallation

### Automated Uninstallation

**Windows**:
```cmd
%SCRUM_HOME%\uninstall.bat
```

**PowerShell**:
```powershell
& "$env:SCRUM_HOME\uninstall.ps1"
```

**Linux/macOS**:
```bash
$SCRUM_HOME/uninstall.sh
```

### Manual Uninstallation

1. **Remove Installation Directory**
   
   **Windows**:
   ```cmd
   rmdir /S /Q "C:\Program Files\SCRUM"
   ```
   
   **Linux/macOS**:
   ```bash
   sudo rm -rf /opt/scrum
   # or for user install:
   rm -rf ~/.local/scrum
   ```

2. **Remove Environment Variables**
   
   **Windows**:
   - System Properties → Environment Variables
   - Delete `SCRUM_HOME`
   - Remove `%SCRUM_HOME%\bin` from PATH
   
   **Linux/macOS**:
   - Edit `~/.bashrc` or `~/.zshrc`
   - Remove SCRUM-related lines

3. **Remove Symlink** (Linux/macOS system install):
   ```bash
   sudo rm /usr/local/bin/scrum
   ```

4. **Restart Terminal**

---

## Troubleshooting

### "Java is not installed or not in PATH"

**Solution**: Install Java 25+ and ensure it's in PATH:
```bash
java -version
```

If not found, add Java to PATH or reinstall Java.

---

### "scrum: command not found"

**Possible Causes**:
1. PATH not updated
2. Terminal not restarted
3. Installation incomplete

**Solutions**:

**Windows**:
```cmd
# Verify PATH
echo %PATH%

# Should contain: C:\Program Files\SCRUM\bin (or your install path)

# Manually set for current session
set PATH=%PATH%;C:\Program Files\SCRUM\bin
```

**Linux/macOS**:
```bash
# Verify PATH
echo $PATH

# Should contain: /opt/scrum/bin (or your install path)

# Reload shell config
source ~/.bashrc  # or ~/.zshrc

# Or manually set for current session
export PATH="/opt/scrum/bin:$PATH"
```

---

### "Error: SCRUM requires Java 25 or higher"

**Solution**: Upgrade Java:
- Download from [https://adoptium.net/](https://adoptium.net/)
- Install Java 25 LTS or later
- Verify: `java -version`

---

### "scrum.jar not found"

**Possible Causes**:
1. Incomplete installation
2. Corrupted download

**Solutions**:
1. Verify JAR file exists:
   ```bash
   ls $SCRUM_HOME/lib/scrum-*.jar
   ```

2. Re-extract SDK archive

3. Re-run installer

---

### LLM Provider Issues

For LLM-related errors (quota exceeded, rate limiting, etc.), see:
- [LLM-CONFIGURATION.md](LLM-CONFIGURATION.md)
- [FALLBACK-CHAIN-IMPLEMENTATION.md](FALLBACK-CHAIN-IMPLEMENTATION.md)

---

## Building from Source

To build the SDK from source:

### Prerequisites

- Java 25 JDK
- Apache Maven 3.8+
- Git

### Build Steps

1. **Clone Repository**:
   ```bash
   git clone https://github.com/janvanwassenhove/scrum.git
   cd scrum
   ```

2. **Build with Maven**:
   ```bash
   mvn clean package
   ```

3. **Generate SDK Distribution**:
   ```bash
   mvn assembly:single
   ```

4. **Locate Build Artifacts**:
   ```bash
   ls target/scrum-language-*-sdk.zip
   ```

5. **Extract and Install**:
   - Extract the generated archive
   - Follow installation steps above

### Development Build

For quick development testing without full SDK packaging:

```bash
# Build JAR only
mvn clean package -DskipTests

# Copy to development directory
mvn install

# Run from development directory
cd development
./scrum.bat examples/HelloWorld.scrum  # Windows
./scrum.sh examples/HelloWorld.scrum   # Linux/Mac
```

---

## IDE Extensions

After installing the SDK, you can install IDE extensions for syntax highlighting:

- **VS Code**: See [ide-extensions/vscode-scrum/README.md](../ide-extensions/vscode-scrum/README.md)
- **IntelliJ IDEA**: See [ide-extensions/intellij-scrum/README.md](../ide-extensions/intellij-scrum/README.md)

Installation scripts:
```bash
# Windows
ide-extensions\install-vscode.bat
ide-extensions\install-intellij.bat

# Linux/macOS
ide-extensions/install-vscode.sh
ide-extensions/install-intellij.sh
```

---

## Additional Resources

- **Language Reference**: [LANGUAGE-REFERENCE.md](LANGUAGE-REFERENCE.md)
- **Getting Started with Intents**: [GETTING-STARTED-INTENTS.md](GETTING-STARTED-INTENTS.md)
- **API Definitions**: [API-DEFINITIONS.md](API-DEFINITIONS.md)
- **LLM Configuration**: [LLM-CONFIGURATION.md](LLM-CONFIGURATION.md)
- **GitHub Repository**: [https://github.com/janvanwassenhove/scrum](https://github.com/janvanwassenhove/scrum)
- **Report Issues**: [https://github.com/janvanwassenhove/scrum/issues](https://github.com/janvanwassenhove/scrum/issues)

---

## Support

For issues, questions, or contributions:

- **Issues**: [GitHub Issues](https://github.com/janvanwassenhove/scrum/issues)
- **Discussions**: [GitHub Discussions](https://github.com/janvanwassenhove/scrum/discussions)
- **Email**: Check repository for contact information

---

**Copyright (c) 2025 Jan Van Wassenhove**  
Licensed under terms specified in [LICENSE](../LICENSE) file.
