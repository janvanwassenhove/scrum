# SCRUM CLI Reference

Complete reference for the SCRUM Programming Language Command Line Interface.

## Table of Contents

- [Overview](#overview)
- [Command Syntax](#command-syntax)
- [Options Reference](#options-reference)
- [Usage Examples](#usage-examples)
- [Development Workflow](#development-workflow)
- [CI/CD Integration](#cicd-integration)
- [Troubleshooting](#troubleshooting)

---

## Overview

The SCRUM CLI provides a comprehensive command-line interface for running SCRUM programs, validating syntax, debugging issues, and accessing help resources. It's designed to be developer-friendly and suitable for both interactive development and automated CI/CD workflows.

## Command Syntax

```bash
scrum [OPTIONS] <filename>
scrum [GLOBAL-OPTIONS]
```

### Global Options (No filename required)
- `--version`, `-v` - Display version and environment information
- `--help`, `-h` - Show comprehensive help message
- `--examples` - View available examples and sample code

### File Processing Options (Require filename)
- `--debug`, `-d` - Enable debug mode with detailed error traces
- `--validate`, `-c` - Validate syntax only (no execution)
- `--syntax-check` - Alias for `--validate`

---

## Options Reference

### `--version` / `-v`
Display comprehensive version and environment information.

```bash
scrum --version
scrum -v
```

**Output includes:**
- SCRUM language version
- Build date  
- Java version and vendor
- Java home directory
- SCRUM_HOME installation path
- Copyright information

**Example Output:**
```
╔════════════════════════════════════════════════════════════════╗
║  SCRUM Programming Language                                    ║
╚════════════════════════════════════════════════════════════════╝

Version:        1.3.0
Build Date:     2025-12-27

Java Version:   25.0.1
Java Vendor:    Oracle Corporation
Java Home:      C:\Program Files\Java\jdk-25

SCRUM_HOME:     C:\scrum

Copyright (c) 2023-2025 Jan Van Wassenhove
License: See LICENSE file in distribution
```

### `--help` / `-h`
Display detailed help information including usage examples and feature overview.

```bash
scrum --help
scrum -h
```

**Includes:**
- Complete command syntax
- All available options
- Usage examples
- SCRUM language feature overview
- Documentation and support links

### `--examples`
View available example programs with descriptions and sample code.

```bash
scrum --examples
```

**Features:**
- Lists all example files with descriptions
- Shows example file locations
- Includes sample "Hello World" and AI-powered #INTENT code
- Provides quick-start commands

### `--debug` / `-d`
Enable debug mode for detailed error reporting and execution traces.

```bash
scrum --debug <filename>
scrum -d <filename>
```

**Features:**
- Detailed stack traces for all errors
- Enhanced error messages with context
- Internal execution information
- Useful for debugging complex SCRUM programs

**Example:**
```bash
scrum --debug examples/HelloWorld.scrum
scrum -d myprogram.scrum
```

### `--validate` / `-c` / `--syntax-check`
Validate SCRUM program syntax without executing the code.

```bash
scrum --validate <filename>
scrum -c <filename>
scrum --syntax-check <filename>
```

**Features:**
- Parse and validate syntax only
- No code execution (safe for untrusted code)
- Returns exit code 0 for valid syntax, 1 for errors
- Perfect for CI/CD validation pipelines

**Example Output:**
```bash
$ scrum --validate examples/HelloWorld.scrum
✅ Syntax validation successful for: examples/HelloWorld.scrum
   No syntax errors found. The file is ready for execution.
```

---

## Usage Examples

### Basic Program Execution

```bash
# Run a SCRUM program
scrum hello.scrum
scrum path/to/program.scrum

# Run example programs
scrum examples/HelloWorld.scrum
scrum examples/ApiExample.scrum
```

### Development Workflow

```bash
# 1. Check SCRUM installation
scrum --version

# 2. View available examples for reference
scrum --examples

# 3. Validate syntax while developing
scrum --validate myprogram.scrum

# 4. Run with debug info if issues occur
scrum --debug myprogram.scrum

# 5. Normal execution once working
scrum myprogram.scrum
```

### Getting Help and Information

```bash
# Get comprehensive help
scrum --help

# Check installation and environment
scrum --version

# Browse example programs and sample code
scrum --examples

# Find installation location
echo $SCRUM_HOME           # Linux/macOS
echo %SCRUM_HOME%          # Windows CMD
echo $env:SCRUM_HOME       # Windows PowerShell
```

### Error Diagnosis

```bash
# Basic error (concise output)
scrum problematic.scrum

# Detailed debugging (full stack traces)
scrum --debug problematic.scrum

# Syntax validation only (check without running)
scrum --validate problematic.scrum
```

---

## Development Workflow

### Recommended Development Process

1. **Setup and Verification**
   ```bash
   scrum --version                    # Verify installation
   scrum --examples                   # Browse available examples
   ```

2. **Development**
   ```bash
   scrum --validate mycode.scrum      # Check syntax frequently
   scrum --debug mycode.scrum         # Test with debug info
   ```

3. **Testing**
   ```bash
   scrum mycode.scrum                 # Normal execution
   scrum --validate *.scrum           # Validate all files
   ```

4. **Debugging**
   ```bash
   scrum --debug problematic.scrum    # Get detailed error info
   ```

### File Organization

```
my-project/
├── main.scrum                       # Entry point
├── modules/                         # Supporting modules
│   ├── utils.scrum
│   └── api.scrum
└── tests/                          # Test files
    ├── test-main.scrum
    └── test-utils.scrum
```

```bash
# Validate entire project
scrum --validate main.scrum
scrum --validate modules/*.scrum
scrum --validate tests/*.scrum

# Run with debug info
scrum --debug main.scrum
```

---

## CI/CD Integration

### Syntax Validation in CI/CD

The `--validate` option is perfect for CI/CD pipelines:

**GitHub Actions Example:**
```yaml
name: SCRUM Syntax Check
on: [push, pull_request]

jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '25'
          
      - name: Install SCRUM SDK
        run: |
          wget https://github.com/janvanwassenhove/scrum/releases/download/v1.3.0/scrum-1.3.0-sdk.tar.gz
          tar -xzf scrum-1.3.0-sdk.tar.gz
          cd scrum-1.3.0 && sudo ./installers/install.sh
          
      - name: Validate SCRUM Files
        run: |
          scrum --validate src/main.scrum
          scrum --validate src/modules/*.scrum
```

**Jenkins Pipeline Example:**
```groovy
pipeline {
    agent any
    stages {
        stage('Validate SCRUM Syntax') {
            steps {
                script {
                    sh 'scrum --validate src/**/*.scrum'
                }
            }
        }
    }
}
```

**Batch Validation Script:**
```bash
#!/bin/bash
# validate-all.sh

echo "Validating SCRUM programs..."

EXIT_CODE=0
for file in src/**/*.scrum; do
    if scrum --validate "$file"; then
        echo "✅ $file"
    else
        echo "❌ $file"
        EXIT_CODE=1
    fi
done

exit $EXIT_CODE
```

### Docker Integration

```dockerfile
FROM openjdk:21-jdk-slim

# Install SCRUM SDK
RUN curl -L https://github.com/janvanwassenhove/scrum/releases/download/v1.3.0/scrum-1.3.0-sdk.tar.gz \
    | tar -xzf - && cd scrum-1.3.0 && ./installers/install.sh

# Set environment
ENV SCRUM_HOME=/opt/scrum
ENV PATH="$SCRUM_HOME/bin:$PATH"

# Validate and run SCRUM programs
COPY . /app
WORKDIR /app
RUN scrum --validate main.scrum
CMD ["scrum", "main.scrum"]
```

---

## Troubleshooting

### Common Issues

#### Command Not Found
```bash
# Check if SCRUM is in PATH
which scrum           # Linux/macOS
where scrum          # Windows

# Verify SCRUM_HOME
echo $SCRUM_HOME

# Re-source profile (Linux/macOS)
source ~/.bashrc     # or ~/.zshrc
```

#### Java Issues
```bash
# Check Java version (requires 25+)
java -version

# Check Java in PATH
which java

# Update Java if needed
sudo apt update && sudo apt install openjdk-21-jdk
```

#### Permission Issues
```bash
# Linux/macOS: Ensure execute permissions
chmod +x $SCRUM_HOME/bin/scrum

# Windows: Run as Administrator if needed
```

#### Environment Variables
```bash
# Check all SCRUM-related variables
env | grep SCRUM      # Linux/macOS
set | findstr SCRUM   # Windows CMD

# Reset environment if needed
unset SCRUM_HOME      # Linux/macOS
set SCRUM_HOME=       # Windows CMD
```

### Debug Output Analysis

When using `--debug`, look for:

1. **Java Stack Traces** - Show exact error location
2. **SCRUM Context** - File name and line numbers  
3. **Parser Information** - Token and syntax details
4. **Execution Flow** - Step-by-step program execution

### Getting Help

If you encounter issues:

1. **Check version**: `scrum --version`
2. **View examples**: `scrum --examples`  
3. **Read documentation**: See `docs/` directory
4. **Report issues**: [GitHub Issues](https://github.com/janvanwassenhove/scrum/issues)

---

## Exit Codes

The SCRUM CLI uses standard exit codes:

| Code | Meaning |
|------|---------|
| 0    | Success |
| 1    | Syntax error, runtime error, or invalid usage |

**Example Usage in Scripts:**
```bash
if scrum --validate myprogram.scrum; then
    echo "Syntax is valid"
    scrum myprogram.scrum
else
    echo "Syntax errors found"
    exit 1
fi
```

---

## See Also

- [SDK Installation Guide](SDK-INSTALLATION.md) - Complete installation instructions
- [Quick Reference](SDK-QUICK-REFERENCE.md) - Condensed command reference  
- [Language Reference](LANGUAGE-REFERENCE.md) - SCRUM language syntax and features
- [Getting Started with Intents](GETTING-STARTED-INTENTS.md) - AI-powered programming guide

---

**Copyright (c) 2023-2025 Jan Van Wassenhove**  
Licensed under terms specified in [LICENSE](../LICENSE) file.