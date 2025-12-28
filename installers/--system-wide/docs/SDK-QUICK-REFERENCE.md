# SCRUM SDK Quick Reference

## Installation

### Windows
```cmd
# Extract SDK
# Run as Administrator:
installers\install.bat

# Or with PowerShell:
.\installers\install.ps1
```

### Linux / macOS
```bash
# Extract SDK
tar -xzf scrum-1.3.0-sdk.tar.gz
cd scrum-1.3.0

# System-wide (requires sudo)
sudo ./installers/install.sh

# User installation (no sudo)
./installers/install.sh
```

## Basic Commands

```bash
# Check version
scrum --version

# Run a program
scrum myprogram.scrum
scrum path/to/program.scrum

# Examples
scrum examples/HelloWorld.scrum
scrum examples/OddOrNot.scrum
```

## Environment Variables

```bash
# Required
SCRUM_HOME      # SDK installation directory

# Optional (for LLM features)
SCRUM_API_KEY              # LLM provider API key
SCRUM_LLM_PROVIDER         # Preferred provider (openai, anthropic, groq, ollama)
SCRUM_LLM_FALLBACK_CHAIN   # Provider fallback order (default: auto,groq,ollama)
```

## Building SDK from Source

```bash
# Clone repository
git clone https://github.com/janvanwassenhove/scrum.git
cd scrum

# Build SDK distributions
mvn clean package

# Output: target/scrum-language-<version>-sdk.{zip,tar.gz}
```

## Upgrade

```bash
# 1. Uninstall old version
# Windows:
%SCRUM_HOME%\uninstall.bat

# Linux/macOS:
$SCRUM_HOME/uninstall.sh

# 2. Download new version from GitHub Releases
# 3. Install using steps above
```

## Uninstall

```bash
# Windows
%SCRUM_HOME%\uninstall.bat
# Or:
"%ProgramFiles%\SCRUM\uninstall.bat"

# PowerShell
& "$env:SCRUM_HOME\uninstall.ps1"

# Linux/macOS
$SCRUM_HOME/uninstall.sh
# Or:
/opt/scrum/uninstall.sh
```

## Troubleshooting

```bash
# Verify Java version (requires 25+)
java -version

# Check SCRUM_HOME
echo $SCRUM_HOME           # Linux/macOS
echo %SCRUM_HOME%          # Windows cmd
echo $env:SCRUM_HOME       # Windows PowerShell

# Check PATH contains SCRUM
echo $PATH | grep scrum    # Linux/macOS
echo %PATH%                # Windows

# Reload environment (Linux/macOS)
source ~/.bashrc           # or ~/.zshrc

# Restart terminal (Windows)
# Close and reopen Command Prompt or PowerShell
```

## IDE Extensions

```bash
# Install VS Code extension
# Windows:
ide-extensions\install-vscode.bat

# Linux/macOS:
ide-extensions/install-vscode.sh

# Install IntelliJ IDEA plugin
# Windows:
ide-extensions\install-intellij.bat

# Linux/macOS:
ide-extensions/install-intellij.sh
```

## Example Programs

```bash
# Navigate to examples
cd $SCRUM_HOME/examples

# Run HelloWorld
scrum HelloWorld.scrum

# Run with API features
scrum ApiExample.scrum

# Run with Intent blocks (requires LLM API key)
export SCRUM_API_KEY="your-api-key"
scrum IntentComputeBirthYear.scrum
```

## Documentation

- **Installation Guide**: `docs/SDK-INSTALLATION.md`
- **Language Reference**: `docs/LANGUAGE-REFERENCE.md`
- **LLM Configuration**: `docs/LLM-CONFIGURATION.md`
- **API Definitions**: `docs/API-DEFINITIONS.md`
- **Getting Started**: `docs/GETTING-STARTED-INTENTS.md`

## URLs

- **GitHub Repository**: https://github.com/janvanwassenhove/scrum
- **Download Releases**: https://github.com/janvanwassenhove/scrum/releases
- **Report Issues**: https://github.com/janvanwassenhove/scrum/issues

## Version Information

```bash
# Display full version info
scrum --version

# Output includes:
# - SCRUM version
# - Build date
# - Java version
# - Java vendor
# - Java home
# - SCRUM_HOME path
```

## Quick Start

```bash
# 1. Download SDK
wget https://github.com/janvanwassenhove/scrum/releases/download/v1.3.0/scrum-1.3.0-sdk.tar.gz

# 2. Extract
tar -xzf scrum-1.3.0-sdk.tar.gz
cd scrum-1.3.0

# 3. Install
sudo ./installers/install.sh

# 4. Verify
scrum --version

# 5. Run example
scrum examples/HelloWorld.scrum
```

## Default Installation Paths

**Windows:**
- System-wide: `C:\Program Files\SCRUM`
- User: `%LOCALAPPDATA%\Programs\SCRUM`

**Linux:**
- System-wide: `/opt/scrum`
- User: `~/.local/scrum`

**macOS:**
- System-wide: `/opt/scrum`
- User: `~/.local/scrum`

---

**Need help?** See [SDK-INSTALLATION.md](SDK-INSTALLATION.md) for complete documentation.
