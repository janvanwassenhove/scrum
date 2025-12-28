#!/usr/bin/env pwsh
<#
.SYNOPSIS
    SCRUM Language Release Automation Script

.DESCRIPTION
    Automates the release process for new SCRUM SDK versions including:
    - Version bumping in pom.xml
    - Building and testing
    - Git commit and tagging
    - Pushing to GitHub to trigger automated release

.PARAMETER Version
    The new version number in format MAJOR.MINOR.PATCH (e.g., 1.4.0)

.PARAMETER SkipTests
    Skip running Maven tests during build

.PARAMETER SkipInstallTest
    Skip local installation test

.EXAMPLE
    .\release.ps1 -Version 1.4.0

.EXAMPLE
    .\release.ps1 -Version 1.4.0 -SkipInstallTest

.NOTES
    Author: Jan Van Wassenhove
    Prerequisites: Git, Maven, Java 21+
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory=$false, Position=0)]
    [ValidatePattern('^\d+\.\d+\.\d+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$')]
    [string]$Version,
    
    [Parameter()]
    [switch]$SkipTests,
    
    [Parameter()]
    [switch]$SkipInstallTest
)

$ErrorActionPreference = "Stop"

# Colors for output
function Write-ColorOutput {
    param(
        [string]$Message,
        [string]$Color = "White"
    )
    Write-Host $Message -ForegroundColor $Color
}

function Write-Success { Write-ColorOutput $args[0] "Green" }
function Write-Error { Write-ColorOutput $args[0] "Red" }
function Write-Warning { Write-ColorOutput $args[0] "Yellow" }
function Write-Info { Write-ColorOutput $args[0] "Cyan" }
function Write-Step { Write-ColorOutput $args[0] "Blue" }

# Main script
try {
    Write-Host ""
    Write-ColorOutput "========================================"
    Write-ColorOutput "SCRUM Language Release Automation"
    Write-ColorOutput "========================================"
    Write-Host ""
    
    # Get version if not provided as parameter
    if (-not $Version) {
        do {
            $Version = Read-Host "Enter version number (e.g., 1.4.0, 1.4.0-beta.1)"
            if (-not $Version) {
                Write-Error "Version number cannot be empty"
            }
            elseif ($Version -notmatch '^\d+\.\d+\.\d+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$') {
                Write-Error "Invalid version format. Use MAJOR.MINOR.PATCH[-PRERELEASE] (e.g., 1.4.0, 1.4.0-alpha, 1.4.0-beta.1)"
                $Version = $null
            }
        } while (-not $Version)
    }
    
    Write-Info "Target Version: $Version"
    Write-Host ""

    # Step 1: Prerequisites
    Write-Step "Step 1: Checking Prerequisites..."
    
    # Check Git
    if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
        Write-Error "Error: Git not found"
        exit 1
    }
    Write-Success "  [OK] Git installed"
    
    # Check Maven
    if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
        Write-Error "Error: Maven not found"
        exit 1
    }
    Write-Success "  [OK] Maven installed"
    
    # Check Java
    if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
        Write-Error "Error: Java not found"
        exit 1
    }
    Write-Success "  [OK] Java installed"
    Write-Host ""
    
    # Check for uncommitted changes
    $gitStatus = git status --porcelain
    if ($gitStatus) {
        Write-Warning "Warning: You have uncommitted changes"
        $continue = Read-Host "Continue anyway? (y/N)"
        if ($continue -ne 'y' -and $continue -ne 'Y') {
            Write-Info "Release cancelled"
            exit 0
        }
    }
    
    # Check current branch
    $currentBranch = git rev-parse --abbrev-ref HEAD
    if ($currentBranch -ne "master") {
        Write-Warning "Warning: Not on master branch (current: $currentBranch)"
        $continue = Read-Host "Continue anyway? (y/N)"
        if ($continue -ne 'y' -and $continue -ne 'Y') {
            Write-Info "Release cancelled"
            exit 0
        }
    }
    Write-Host ""
    
    # Step 2: Update version
    Write-Step "Step 2: Updating Version in pom.xml..."
    
    $pomPath = "pom.xml"
    if (-not (Test-Path $pomPath)) {
        Write-Error "Error: pom.xml not found"
        exit 1
    }
    
    # Backup pom.xml
    Copy-Item $pomPath "$pomPath.bak"
    
    try {
        # Update version in pom.xml (target only the project version, not plugin versions)
        $pomContent = Get-Content $pomPath -Raw
        $pomContent = $pomContent -replace '(<artifactId>scrum-language</artifactId>\s*\r?\n\s*<version>)[^<]+', "`${1}$Version"
        Set-Content $pomPath $pomContent -NoNewline
        
        Write-Success "  [OK] Version updated to $Version"
    }
    catch {
        Write-Error "Error: Failed to update pom.xml"
        Copy-Item "$pomPath.bak" $pomPath
        Remove-Item "$pomPath.bak"
        exit 1
    }
    finally {
        Remove-Item "$pomPath.bak" -ErrorAction SilentlyContinue
    }
    Write-Host ""
    
    # Step 3: Build
    Write-Step "Step 3: Building SDK..."
    Write-Host ""
    
    $mvnArgs = @("clean", "package")
    if ($SkipTests) {
        $mvnArgs += "-DskipTests"
        Write-Warning "  Skipping tests (as requested)"
    }
    
    & mvn $mvnArgs
    if ($LASTEXITCODE -ne 0) {
        Write-Error "`nError: Build failed"
        exit 1
    }
    
    Write-Host ""
    Write-Success "  [OK] Build successful"
    Write-Host ""
    
    # Verify SDK zip
    $sdkZip = "target\scrum-language-$Version-sdk.zip"
    if (-not (Test-Path $sdkZip)) {
        Write-Error "Error: SDK zip not found"
        Write-Error "Expected: $sdkZip"
        exit 1
    }
    Write-Success "  [OK] SDK zip created: $sdkZip"
    Write-Host ""
    
    # Step 4: Test installation (optional)
    if (-not $SkipInstallTest) {
        Write-Step "Step 4: Testing Installation..."
        $testInstall = Read-Host "Test installation locally? (y/N)"
        
        if ($testInstall -eq 'y' -or $testInstall -eq 'Y') {
            Write-Host ""
            Write-Info "Installing to C:\scrum-test..."
            
            # Clean test directory
            if (Test-Path "C:\scrum-test") {
                Remove-Item -Recurse -Force "C:\scrum-test"
            }
            
            # Extract and install
            Push-Location target
            try {
                Expand-Archive -Path "scrum-language-$Version-sdk.zip" -DestinationPath . -Force
                Push-Location "scrum-$Version"
                try {
                    & .\installers\install.ps1 -InstallDir "C:\scrum-test"
                    if ($LASTEXITCODE -ne 0) {
                        throw "Installation failed"
                    }
                }
                finally {
                    Pop-Location
                }
            }
            finally {
                Pop-Location
            }
            
            Write-Success "  [OK] Installation test successful"
            Write-Host ""
        }
    }
    else {
        Write-Warning "Step 4: Skipping installation test (as requested)"
        Write-Host ""
    }
    
    # Step 5: Commit
    Write-Step "Step 5: Committing Changes..."
    
    git add pom.xml
    git commit -m "Bump version to $Version"
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Error: Git commit failed"
        exit 1
    }
    
    Write-Success "  [OK] Changes committed"
    Write-Host ""
    
    # Step 6: Create tag
    Write-Step "Step 6: Creating Git Tag..."
    
    # Check if tag exists
    $existingTag = git tag -l "v$Version"
    if ($existingTag) {
        Write-Warning "Warning: Tag v$Version already exists"
        $deleteTag = Read-Host "Delete existing tag? (y/N)"
        if ($deleteTag -eq 'y' -or $deleteTag -eq 'Y') {
            git tag -d "v$Version"
            git push origin ":refs/tags/v$Version" 2>$null
        }
        else {
            Write-Info "Release cancelled"
            exit 0
        }
    }
    
    git tag -a "v$Version" -m "Release version $Version"
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Error: Failed to create tag"
        exit 1
    }
    
    Write-Success "  [OK] Tag v$Version created"
    Write-Host ""
    
    # Step 7: Push
    Write-Step "Step 7: Pushing to GitHub..."
    Write-Host ""
    
    Write-Warning "This will push the commit and tag to GitHub, triggering the release workflow."
    $pushConfirm = Read-Host "Push to GitHub? (y/N)"
    
    if ($pushConfirm -ne 'y' -and $pushConfirm -ne 'Y') {
        Write-Host ""
        Write-Info "Release process stopped before pushing"
        Write-Host ""
        Write-Info "To complete manually:"
        Write-Info "  git push origin master"
        Write-Info "  git push origin v$Version"
        exit 0
    }
    
    # Push commit
    git push origin master
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Error: Failed to push commit"
        exit 1
    }
    
    # Push tag
    git push origin "v$Version"
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Error: Failed to push tag"
        exit 1
    }
    
    # Success!
    Write-Host ""
    Write-ColorOutput "========================================" "Green"
    Write-ColorOutput "Release Process Complete!" "Green"
    Write-ColorOutput "========================================" "Green"
    Write-Host ""
    Write-Info "Version: $Version"
    Write-Info "Tag: v$Version"
    Write-Host ""
    Write-Info "Next Steps:"
    Write-Info "1. Monitor GitHub Actions workflow:"
    Write-Info "   https://github.com/janvanwassenhove/scrum/actions"
    Write-Host ""
    Write-Info "2. Verify release when workflow completes:"
    Write-Info "   https://github.com/janvanwassenhove/scrum/releases"
    Write-Host ""
    Write-Info "3. Test installation from GitHub:"
    Write-Info "   Download and install SDK from release page"
    Write-Host ""
    
    exit 0
}
catch {
    Write-Error "An error occurred: $_"
    Write-Error $_.ScriptStackTrace
    exit 1
}
