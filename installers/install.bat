@echo off
setlocal enabledelayedexpansion

:args_done

REM Display usage if help requested
if "%~1"=="/?" goto show_usage
if "%~1"=="-h" goto show_usage
if "%~1"=="--help" goto show_usage
goto continue_install

:show_usage
echo Usage: install.bat [installation_directory] [--system-wide]
echo.
echo Options:
echo   installation_directory    Custom installation path
echo   --system-wide             Install to Program Files (requires admin)
echo.
echo Examples:
echo   install.bat                           ^(user install to AppData^)
echo   install.bat C:\scrum                  ^(custom directory^)
echo   install.bat --system-wide             ^(system install to Program Files^)
echo.
exit /b 0

:continue_install

REM Parse command line arguments
set "INSTALL_DIR="
set "SYSTEM_WIDE=false"

:parse_args
if "%~1"=="" goto args_done
if /i "%~1"=="--system-wide" (
    set "SYSTEM_WIDE=true"
    shift
    goto parse_args
)
if "%INSTALL_DIR%"=="" (
    set "INSTALL_DIR=%~1"
    shift
    goto parse_args
)
shift
goto parse_args
:args_done

echo ================================================================
echo   SCRUM Programming Language SDK Installer
echo ================================================================
echo.

REM Determine SDK root directory (parent of installers folder)
for %%i in ("%~dp0..") do set "SDK_ROOT=%%~fi"
echo Installing SCRUM SDK from: %SDK_ROOT%
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not in PATH
    echo.
    echo SCRUM requires Java 21 or higher.
    echo Download from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Check Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)
set JAVA_VERSION=%JAVA_VERSION:"=%
for /f "tokens=1 delims=." %%a in ("%JAVA_VERSION%") do set JAVA_MAJOR=%%a
if %JAVA_MAJOR% LSS 21 (
    echo [ERROR] SCRUM requires Java 21 or higher
    echo Current Java version: %JAVA_VERSION%
    echo Download Java 21+ from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

echo [OK] Java %JAVA_VERSION% detected
echo.

REM Determine default installation directory
if "%SYSTEM_WIDE%"=="true" (
    set "DEFAULT_INSTALL_DIR=%ProgramFiles%\SCRUM"
) else (
    set "DEFAULT_INSTALL_DIR=%LOCALAPPDATA%\Programs\SCRUM"
)

REM Ask for installation directory if not provided
if "%INSTALL_DIR%"=="" (
    set /p "INSTALL_DIR=Installation directory [%DEFAULT_INSTALL_DIR%]: "
    if "!INSTALL_DIR!"=="" set "INSTALL_DIR=%DEFAULT_INSTALL_DIR%"
) else (
    echo Using provided directory: %INSTALL_DIR%
)

echo.
echo Installation directory: %INSTALL_DIR%
echo.

REM Create installation directory
if not exist "%INSTALL_DIR%" (
    mkdir "%INSTALL_DIR%"
    if errorlevel 1 (
        echo [ERROR] Failed to create installation directory
        echo You may need to run this installer as Administrator
        echo.
        pause
        exit /b 1
    )
)

REM Copy SDK files
echo Copying SDK files...
xcopy /E /I /Y "%SDK_ROOT%\bin" "%INSTALL_DIR%\bin" >nul
xcopy /E /I /Y "%SDK_ROOT%\lib" "%INSTALL_DIR%\lib" >nul
xcopy /E /I /Y "%SDK_ROOT%\examples" "%INSTALL_DIR%\examples" >nul
xcopy /E /I /Y "%SDK_ROOT%\docs" "%INSTALL_DIR%\docs" >nul
copy /Y "%SDK_ROOT%\LICENSE" "%INSTALL_DIR%\" >nul
copy /Y "%SDK_ROOT%\README.md" "%INSTALL_DIR%\" >nul

echo [OK] SDK files copied
echo.

REM Set SCRUM_HOME environment variable (User level)
echo Setting SCRUM_HOME environment variable...
setx SCRUM_HOME "%INSTALL_DIR%" >nul
if errorlevel 1 (
    echo [WARNING] Failed to set SCRUM_HOME environment variable
    echo You may need to set it manually
) else (
    echo [OK] SCRUM_HOME set to: %INSTALL_DIR%
)
echo.

REM Add to PATH (User level)
echo Adding SCRUM to PATH...
for /f "tokens=2*" %%a in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "USER_PATH=%%b"

REM Check if already in PATH
echo %USER_PATH% | findstr /I /C:"%INSTALL_DIR%\bin" >nul
if errorlevel 1 (
    REM Not in PATH, add it
    if "%USER_PATH%"=="" (
        setx PATH "%INSTALL_DIR%\bin" >nul
    ) else (
        setx PATH "%USER_PATH%;%INSTALL_DIR%\bin" >nul
    )
    if errorlevel 1 (
        echo [WARNING] Failed to add SCRUM to PATH
        echo You may need to add "%INSTALL_DIR%\bin" to your PATH manually
    ) else (
        echo [OK] Added %INSTALL_DIR%\bin to PATH
    )
) else (
    echo [OK] Already in PATH
)
echo.

REM Create uninstall script
echo Creating uninstall script...
(
    echo @echo off
    echo echo Uninstalling SCRUM Programming Language SDK...
    echo.
    echo REM Remove installation directory
    echo if exist "%INSTALL_DIR%" (
    echo     rmdir /S /Q "%INSTALL_DIR%"
    echo     echo [OK] Removed installation directory
    echo ^) else (
    echo     echo [WARNING] Installation directory not found
    echo ^)
    echo.
    echo REM Remove SCRUM_HOME environment variable
    echo reg delete "HKCU\Environment" /v SCRUM_HOME /f ^>nul 2^>^&1
    echo if errorlevel 1 (
    echo     echo [WARNING] SCRUM_HOME environment variable not found
    echo ^) else (
    echo     echo [OK] Removed SCRUM_HOME environment variable
    echo ^)
    echo.
    echo REM Note: PATH cleanup requires manual intervention or system restart
    echo echo [INFO] To complete uninstallation, remove "%INSTALL_DIR%\bin" from your PATH
    echo echo       and restart your terminal or computer
    echo.
    echo pause
) > "%INSTALL_DIR%\uninstall.bat"
echo [OK] Uninstall script created
echo.

echo ================================================================
echo   Installation Complete!
echo ================================================================
echo.
echo SCRUM has been installed to: %INSTALL_DIR%
echo.
echo IMPORTANT: Please restart your terminal or command prompt
echo            for the PATH changes to take effect.
echo.
echo You can now run SCRUM programs with:
echo   scrum examples\HelloWorld.scrum
echo.
echo To check the installation:
echo   scrum --version
echo.
echo To uninstall, run:
echo   "%INSTALL_DIR%\uninstall.bat"
echo.
pause
