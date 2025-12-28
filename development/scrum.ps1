# SCRUM Programming Language Launcher Script (PowerShell)
# This script allows running SCRUM programs with: scrum program.scrum

param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$Arguments
)

# Determine SCRUM_HOME from script location
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$env:SCRUM_HOME = $ScriptDir

# Check if Java is available
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCmd) {
    Write-Host "Error: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "SCRUM requires Java 21 or higher"
    Write-Host "Download from: https://adoptium.net/"
    exit 1
}

# Check Java version (require 21+)
$javaVersionOutput = & java -version 2>&1
$versionLine = $javaVersionOutput | Select-String "version" | Select-Object -First 1
if ($versionLine -match '"(\d+)') {
    $javaMajor = [int]$matches[1]
    if ($javaMajor -lt 21) {
        Write-Host "Error: SCRUM requires Java 21 or higher" -ForegroundColor Red
        Write-Host "Current Java version: $($versionLine -replace '.*version "([^"]+)".*', '$1')"
        Write-Host "Download Java 21+ from: https://adoptium.net/"
        exit 1
    }
}

# Find JAR file - try development layout first, then SDK layout
$jarFile = Join-Path $ScriptDir "scrum.jar"
if (-not (Test-Path $jarFile)) {
    # Try SDK distribution layout (../lib/scrum-*.jar)
    $libPath = Join-Path (Split-Path $ScriptDir -Parent) "lib"
    if (Test-Path $libPath) {
        $jarFile = Get-ChildItem -Path $libPath -Filter "scrum-*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName
    }
    if (-not $jarFile -or -not (Test-Path $jarFile)) {
        Write-Host "Error: scrum.jar not found" -ForegroundColor Red
        Write-Host "Expected location: $ScriptDir\scrum.jar"
        Write-Host "Or: $(Split-Path $ScriptDir -Parent)\lib\scrum-*.jar"
        exit 1
    }
}

# Launch SCRUM with all provided arguments
& java -jar $jarFile @Arguments
exit $LASTEXITCODE
