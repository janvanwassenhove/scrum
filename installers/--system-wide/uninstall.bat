@echo off
REM SCRUM Programming Language SDK Uninstaller
echo Uninstalling SCRUM Programming Language SDK...
echo.

REM Remove installation directory
if exist "--system-wide" (
    echo Removing installation directory...
    rmdir /s /q "--system-wide"
    echo [OK] Removed installation directory
) else (
    echo [WARNING] Installation directory not found
)

REM Remove SCRUM_HOME environment variable
echo Removing SCRUM_HOME environment variable...
setx SCRUM_HOME "" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Removed SCRUM_HOME environment variable
) else (
    echo [WARNING] Failed to remove SCRUM_HOME environment variable
)

REM Remove from PATH
echo Removing from PATH...
for /f "tokens=2*" %%i in ('reg query "HKCU\Environment" /v PATH 2^>nul') do set "currentPath=%%j"
if defined currentPath (
    set "newPath=%currentPath:;--system-wide\bin=%"
    set "newPath=%newPath:--system-wide\bin;=%"
    set "newPath=%newPath:--system-wide\bin=%"
    setx PATH "%newPath%" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [OK] Removed from PATH
    ) else (
        echo [WARNING] Failed to remove from PATH
    )
) else (
    echo [WARNING] Could not read current PATH
)

echo.
echo [INFO] Uninstallation complete. Please restart your terminal.
echo.
pause