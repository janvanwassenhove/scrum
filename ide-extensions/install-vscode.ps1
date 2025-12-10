# Install SCRUM Language Extension for VS Code

Write-Host "Installing SCRUM Language Extension for VS Code..." -ForegroundColor Cyan
Write-Host ""

$extensionDir = "$env:USERPROFILE\.vscode\extensions\scrum-language-0.1.0"
$sourceDir = Join-Path $PSScriptRoot "vscode-scrum"

# Remove existing installation
if (Test-Path $extensionDir) {
    Write-Host "Removing existing extension..." -ForegroundColor Yellow
    Remove-Item -Path $extensionDir -Recurse -Force
}

# Copy extension files
Write-Host "Copying extension files..." -ForegroundColor Yellow
Copy-Item -Path $sourceDir -Destination $extensionDir -Recurse -Force

if ($?) {
    Write-Host ""
    Write-Host "✓ SCRUM Language Extension installed successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Please restart VS Code for changes to take effect." -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "✗ Installation failed!" -ForegroundColor Red
    Write-Host ""
}

Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
