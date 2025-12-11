# Build and Install SCRUM Language Plugin for IntelliJ IDEA

Write-Host "Building SCRUM Language Plugin for IntelliJ IDEA..." -ForegroundColor Cyan
Write-Host ""

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$pluginDir = Join-Path $scriptDir "intellij-scrum"

Set-Location $pluginDir

# Check if Gradle wrapper exists
$gradleWrapper = Join-Path $pluginDir "gradlew.bat"

if (Test-Path $gradleWrapper) {
    Write-Host "Using Gradle wrapper to build plugin..." -ForegroundColor Yellow
    
    & .\gradlew.bat buildPlugin
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ Plugin built successfully!" -ForegroundColor Green
        Write-Host ""
        
        $distDir = Join-Path $pluginDir "build\distributions"
        if (Test-Path $distDir) {
            $zipFile = Get-ChildItem -Path $distDir -Filter "*.zip" | Select-Object -First 1
            if ($zipFile) {
                Write-Host "Plugin package created: $($zipFile.FullName)" -ForegroundColor Green
                Write-Host ""
            }
        }
        
        Write-Host "To install the plugin:" -ForegroundColor Cyan
        Write-Host "1. Open IntelliJ IDEA"
        Write-Host "2. Go to Settings (Ctrl+Alt+S) -> Plugins"
        Write-Host "3. Click the gear icon ⚙️ -> Install Plugin from Disk"
        Write-Host "4. Select the .zip file from: build\distributions\"
        Write-Host "5. Restart IntelliJ IDEA"
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "✗ Build failed!" -ForegroundColor Red
        Write-Host "Check the output above for errors." -ForegroundColor Yellow
        Write-Host ""
    }
} else {
    Write-Host "Gradle wrapper not found." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To build the plugin, you have several options:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Option 1: Use IntelliJ IDEA (Recommended)" -ForegroundColor Yellow
    Write-Host "  1. Open the 'intellij-scrum' folder in IntelliJ IDEA"
    Write-Host "  2. Ensure 'Plugin DevKit' plugin is installed"
    Write-Host "  3. Run -> Run 'Plugin' to test in sandbox IDE"
    Write-Host "  4. Build -> Build Project"
    Write-Host "  5. Build -> Prepare Plugin Module for Deployment"
    Write-Host ""
    Write-Host "Option 2: Install Gradle" -ForegroundColor Yellow
    Write-Host "  1. Install Gradle: https://gradle.org/install/"
    Write-Host "  2. Run: gradle wrapper"
    Write-Host "  3. Run: .\gradlew.bat buildPlugin"
    Write-Host ""
    Write-Host "Option 3: Manual Compilation" -ForegroundColor Yellow
    Write-Host "  See README.md for manual compilation instructions"
    Write-Host ""
}

Set-Location $scriptDir

Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
