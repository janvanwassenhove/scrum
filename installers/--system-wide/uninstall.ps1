# SCRUM Programming Language SDK Uninstaller
Write-Host "Uninstalling SCRUM Programming Language SDK..." -ForegroundColor Cyan
Write-Host ""

# Remove installation directory
if (Test-Path "--system-wide") {
    Remove-Item -Path "--system-wide" -Recurse -Force
    Write-Host "[OK] Removed installation directory" -ForegroundColor Green
} else {
    Write-Host "[WARNING] Installation directory not found" -ForegroundColor Yellow
}

# Remove SCRUM_HOME environment variable
try {
    [Environment]::SetEnvironmentVariable("SCRUM_HOME", $null, "User")
    Write-Host "[OK] Removed SCRUM_HOME environment variable" -ForegroundColor Green
} catch {
    Write-Host "[WARNING] Failed to remove SCRUM_HOME: $_" -ForegroundColor Yellow
}

# Remove from PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$newPath = $currentPath -replace "[;]?--system-wide\\bin[;]?", ""
try {
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "[OK] Removed from PATH" -ForegroundColor Green
} catch {
    Write-Host "[WARNING] Failed to remove from PATH: $_" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "[INFO] Uninstallation complete. Please restart your terminal." -ForegroundColor Cyan
Write-Host ""
Read-Host "Press Enter to exit"
