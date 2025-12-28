# SCRUM Programming Language SDK Installer (PowerShell)
# Requires: PowerShell 5.0+, Administrator privileges for system-wide installation

param(
    [string]$InstallDir = "",
    [switch]$SystemWide = $false
)

Write-Host "================================================================" -ForegroundColor Cyan
Write-Host "  SCRUM Programming Language SDK Installer" -ForegroundColor Cyan
Write-Host "================================================================" -ForegroundColor Cyan
Write-Host ""

# Determine SDK root directory (parent of installers folder)
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$SdkRoot = Split-Path -Parent $ScriptDir
Write-Host "Installing SCRUM SDK from: $SdkRoot"
Write-Host ""

# Check if Java is installed
try {
    $javaVersion = & java -version 2>&1 | Select-String "version" | ForEach-Object { $_ -replace '.*"([^"]+)".*', '$1' }
    $javaMajor = ($javaVersion -split '\.')[0]
    
    if ([int]$javaMajor -lt 25) {
        Write-Host "[ERROR] SCRUM requires Java 25 or higher" -ForegroundColor Red
        Write-Host "Current Java version: $javaVersion" -ForegroundColor Yellow
        Write-Host "Download Java 25+ from: https://adoptium.net/"
        Write-Host ""
        Read-Host "Press Enter to exit"
        exit 1
    }
    
    Write-Host "[OK] Java $javaVersion detected" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "[ERROR] Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "SCRUM requires Java 25 or higher."
    Write-Host "Download from: https://adoptium.net/"
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Determine default installation directory
if ($SystemWide) {
    $DefaultInstallDir = "$env:ProgramFiles\SCRUM"
} else {
    $DefaultInstallDir = "$env:LOCALAPPDATA\Programs\SCRUM"
}

# Ask for installation directory if not provided
if ([string]::IsNullOrWhiteSpace($InstallDir)) {
    $response = Read-Host "Installation directory [$DefaultInstallDir]"
    if ([string]::IsNullOrWhiteSpace($response)) {
        $InstallDir = $DefaultInstallDir
    } else {
        $InstallDir = $response
    }
}

Write-Host ""
Write-Host "Installation directory: $InstallDir"
Write-Host ""

# Check if running as Administrator for system-wide or Program Files installation
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if ($InstallDir -like "$env:ProgramFiles*" -and -not $isAdmin) {
    Write-Host "[WARNING] Installation to Program Files requires Administrator privileges" -ForegroundColor Yellow
    Write-Host "Please run this script as Administrator or choose a different location" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Create installation directory
if (-not (Test-Path $InstallDir)) {
    try {
        New-Item -ItemType Directory -Path $InstallDir -Force | Out-Null
        Write-Host "[OK] Created installation directory" -ForegroundColor Green
    } catch {
        Write-Host "[ERROR] Failed to create installation directory: $_" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
} else {
    Write-Host "[INFO] Installation directory already exists" -ForegroundColor Yellow
}
Write-Host ""

# Copy SDK files
Write-Host "Copying SDK files..." -ForegroundColor Cyan
try {
    # Create directories first
    $null = New-Item -ItemType Directory -Path "$InstallDir\bin" -Force
    $null = New-Item -ItemType Directory -Path "$InstallDir\lib" -Force
    $null = New-Item -ItemType Directory -Path "$InstallDir\examples" -Force
    $null = New-Item -ItemType Directory -Path "$InstallDir\docs" -Force
    
    # Copy files
    Copy-Item -Path "$SdkRoot\bin\*" -Destination "$InstallDir\bin\" -Recurse -Force
    Copy-Item -Path "$SdkRoot\lib\*" -Destination "$InstallDir\lib\" -Recurse -Force
    Copy-Item -Path "$SdkRoot\examples\*" -Destination "$InstallDir\examples\" -Recurse -Force
    Copy-Item -Path "$SdkRoot\docs\*" -Destination "$InstallDir\docs\" -Recurse -Force
    Copy-Item -Path "$SdkRoot\LICENSE" -Destination "$InstallDir\" -Force
    Copy-Item -Path "$SdkRoot\README.md" -Destination "$InstallDir\" -Force
    Write-Host "[OK] SDK files copied" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Failed to copy SDK files: $_" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}
Write-Host ""

# Determine environment variable scope
$envScope = if ($SystemWide -or $isAdmin) { "Machine" } else { "User" }
Write-Host "Setting environment variables (Scope: $envScope)..." -ForegroundColor Cyan

# Set SCRUM_HOME environment variable
try {
    [Environment]::SetEnvironmentVariable("SCRUM_HOME", $InstallDir, $envScope)
    Write-Host "[OK] SCRUM_HOME set to: $InstallDir" -ForegroundColor Green
} catch {
    Write-Host "[WARNING] Failed to set SCRUM_HOME: $_" -ForegroundColor Yellow
}

# Add to PATH
$binPath = "$InstallDir\bin"
$currentPath = [Environment]::GetEnvironmentVariable("Path", $envScope)

if ($currentPath -notlike "*$binPath*") {
    try {
        $newPath = "$currentPath;$binPath"
        [Environment]::SetEnvironmentVariable("Path", $newPath, $envScope)
        Write-Host "[OK] Added $binPath to PATH" -ForegroundColor Green
    } catch {
        Write-Host "[WARNING] Failed to add to PATH: $_" -ForegroundColor Yellow
        Write-Host "You may need to add '$binPath' to your PATH manually" -ForegroundColor Yellow
    }
} else {
    Write-Host "[OK] Already in PATH" -ForegroundColor Green
}
Write-Host ""

# Create uninstall script
Write-Host "Creating uninstall script..." -ForegroundColor Cyan
$uninstallScript = @"
# SCRUM Programming Language SDK Uninstaller
Write-Host "Uninstalling SCRUM Programming Language SDK..." -ForegroundColor Cyan
Write-Host ""

# Remove installation directory
if (Test-Path "$InstallDir") {
    Remove-Item -Path "$InstallDir" -Recurse -Force
    Write-Host "[OK] Removed installation directory" -ForegroundColor Green
} else {
    Write-Host "[WARNING] Installation directory not found" -ForegroundColor Yellow
}

# Remove SCRUM_HOME environment variable
try {
    [Environment]::SetEnvironmentVariable("SCRUM_HOME", `$null, "$envScope")
    Write-Host "[OK] Removed SCRUM_HOME environment variable" -ForegroundColor Green
} catch {
    Write-Host "[WARNING] Failed to remove SCRUM_HOME: `$_" -ForegroundColor Yellow
}

# Remove from PATH
`$currentPath = [Environment]::GetEnvironmentVariable("Path", "$envScope")
`$newPath = `$currentPath -replace "[;]?$([regex]::Escape("$binPath"))[;]?", ""
try {
    [Environment]::SetEnvironmentVariable("Path", `$newPath, "$envScope")
    Write-Host "[OK] Removed from PATH" -ForegroundColor Green
} catch {
    Write-Host "[WARNING] Failed to remove from PATH: `$_" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "[INFO] Uninstallation complete. Please restart your terminal." -ForegroundColor Cyan
Write-Host ""
Read-Host "Press Enter to exit"
"@

Set-Content -Path "$InstallDir\uninstall.ps1" -Value $uninstallScript
Write-Host "[OK] Uninstall script created" -ForegroundColor Green
Write-Host ""

# Installation complete
Write-Host "================================================================" -ForegroundColor Green
Write-Host "  Installation Complete!" -ForegroundColor Green
Write-Host "================================================================" -ForegroundColor Green
Write-Host ""
Write-Host "SCRUM has been installed to: $InstallDir"
Write-Host ""
Write-Host "IMPORTANT: Please restart your terminal or PowerShell session" -ForegroundColor Yellow
Write-Host "           for the PATH changes to take effect." -ForegroundColor Yellow
Write-Host ""
Write-Host "You can now run SCRUM programs with:"
Write-Host "  scrum examples\HelloWorld.scrum" -ForegroundColor Cyan
Write-Host ""
Write-Host "To check the installation:"
Write-Host "  scrum --version" -ForegroundColor Cyan
Write-Host ""
Write-Host "To uninstall, run:"
Write-Host "  $InstallDir\uninstall.ps1" -ForegroundColor Cyan
Write-Host ""
Read-Host "Press Enter to exit"
