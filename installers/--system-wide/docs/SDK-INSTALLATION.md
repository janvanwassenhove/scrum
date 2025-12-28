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

- **Java**: Version 21 or higher (LTS recommended)
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
sudo dnf install java-21-openjdk
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

```bash
# Check version
scrum --version

# Expected output:
# ╔════════════════════════════════════════════════════════════════╗
# ║  SCRUM Programming Language                                    ║
# ╚════════════════════════════════════════════════════════════════╝
# 
# Version:        1.3.0
# Build Date:     2025-12-27
# ...
```

### Run Example Program

```bash
# Run HelloWorld
scrum $SCRUM_HOME/examples/HelloWorld.scrum

# Expected output:
# Hello world!
```

Or navigate to the examples directory:

```bash
cd $SCRUM_HOME/examples
scrum HelloWorld.scrum
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

### Automated Upgrade

1. **Uninstall Current Version**
   
   **Windows**:
   ```cmd
   %SCRUM_HOME%\uninstall.bat
   ```
   
   **Linux/macOS**:
   ```bash
   $SCRUM_HOME/uninstall.sh
   ```

2. **Download New Version**
   - Get latest SDK from [GitHub Releases](https://github.com/janvanwassenhove/scrum/releases)

3. **Install New Version**
   - Follow installation steps for your platform

### Manual Upgrade

1. **Backup Configuration**
   - Save any custom environment variables (API keys, etc.)

2. **Remove Old Installation**
   - Delete `$SCRUM_HOME` directory
   - Remove old `SCRUM_HOME` from environment variables
   - Remove old `bin` directory from PATH

3. **Install New Version**
   - Follow fresh installation steps

4. **Restore Configuration**
   - Re-set API keys and custom environment variables

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

**Solution**: Install Java 21+ and ensure it's in PATH:
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

### "Error: SCRUM requires Java 21 or higher"

**Solution**: Upgrade Java:
- Download from [https://adoptium.net/](https://adoptium.net/)
- Install Java 21 LTS or later
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

- Java 21 JDK
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
