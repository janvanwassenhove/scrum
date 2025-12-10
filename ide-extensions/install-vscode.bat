@echo off
REM Install SCRUM Language Extension for VS Code

echo Installing SCRUM Language Extension for VS Code...
echo.

set EXTENSION_DIR=%USERPROFILE%\.vscode\extensions\scrum-language-0.1.0

if exist "%EXTENSION_DIR%" (
    echo Removing existing extension...
    rmdir /s /q "%EXTENSION_DIR%"
)

echo Copying extension files...
xcopy /E /I /Y "%~dp0vscode-scrum" "%EXTENSION_DIR%"

if %errorlevel% equ 0 (
    echo.
    echo ✓ SCRUM Language Extension installed successfully!
    echo.
    echo Please restart VS Code for changes to take effect.
    echo.
) else (
    echo.
    echo ✗ Installation failed!
    echo.
)

pause
