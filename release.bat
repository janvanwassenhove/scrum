@echo off
REM SCRUM Language Release Script
REM Automates the release process for new SDK versions

setlocal enabledelayedexpansion

REM Color codes for output
set "COLOR_RESET=[0m"
set "COLOR_GREEN=[32m"
set "COLOR_RED=[31m"
set "COLOR_YELLOW=[33m"
set "COLOR_BLUE=[34m"

echo.
echo ========================================
echo SCRUM Language Release Automation
echo ========================================
echo.

REM Get version from user input
if not "%~1"=="" (
    set "NEW_VERSION=%~1"
) else (
    set /p "NEW_VERSION=Enter version number (e.g., 1.4.0): "
)

if "%NEW_VERSION%"=="" (
    echo %COLOR_RED%Error: Version number cannot be empty%COLOR_RESET%
    exit /b 1
)

echo Target Version: %NEW_VERSION%
echo.

REM Validate version format using PowerShell for better regex support
powershell -Command "if ('%NEW_VERSION%' -notmatch '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$') { exit 1 }"
if errorlevel 1 (
    echo %COLOR_RED%Error: Invalid version format%COLOR_RESET%
    echo Version must be in format: MAJOR.MINOR.PATCH[-PRERELEASE]
    echo Examples: 1.4.0, 1.4.0-alpha, 1.4.0-beta.1, 1.4.0-rc.2
    exit /b 1
)

echo %COLOR_BLUE%Step 1: Checking Prerequisites...%COLOR_RESET%

REM Check Git
cmd /c "git --version >nul 2>&1"
if errorlevel 1 (
    echo %COLOR_RED%Error: Git not found%COLOR_RESET%
    echo Please install Git and try again
    exit /b 1
)
echo   [OK] Git installed

REM Check Maven
cmd /c "mvn --version >nul 2>&1"
if errorlevel 1 (
    echo %COLOR_RED%Error: Maven not found%COLOR_RESET%
    echo Please install Maven and try again
    exit /b 1
)
echo   [OK] Maven installed

REM Check Java
cmd /c "java -version >nul 2>&1"
if errorlevel 1 (
    echo %COLOR_RED%Error: Java not found%COLOR_RESET%
    echo Please install Java 21+ and try again
    exit /b 1
)
echo   [OK] Java installed
echo.

REM Check for uncommitted changes
git diff --quiet
if errorlevel 1 (
    echo %COLOR_YELLOW%Warning: You have uncommitted changes%COLOR_RESET%
    set /p CONTINUE="Continue anyway? (y/N): "
    if /i not "!CONTINUE!"=="y" (
        echo Release cancelled
        exit /b 1
    )
)

REM Check if on master branch
for /f "tokens=*" %%i in ('git rev-parse --abbrev-ref HEAD') do set CURRENT_BRANCH=%%i
if not "%CURRENT_BRANCH%"=="master" (
    echo %COLOR_YELLOW%Warning: Not on master branch (current: %CURRENT_BRANCH%)%COLOR_RESET%
    set /p CONTINUE="Continue anyway? (y/N): "
    if /i not "!CONTINUE!"=="y" (
        echo Release cancelled
        exit /b 1
    )
)
echo.

echo %COLOR_BLUE%Step 2: Updating Version in pom.xml...%COLOR_RESET%

REM Backup pom.xml
copy pom.xml pom.xml.bak >nul

REM Update version in pom.xml (target only the project version)
powershell -Command "(Get-Content pom.xml -Raw) -replace '(<artifactId>scrum-language</artifactId>\s*\r?\n\s*<version>)[^<]+', \"`$${1}%NEW_VERSION%\" | Set-Content pom.xml -NoNewline"

if errorlevel 1 (
    echo %COLOR_RED%Error: Failed to update pom.xml%COLOR_RESET%
    copy pom.xml.bak pom.xml >nul
    del pom.xml.bak
    exit /b 1
)

echo   [OK] Version updated to %NEW_VERSION%
del pom.xml.bak
echo.

echo %COLOR_BLUE%Step 3: Building SDK...%COLOR_RESET%
echo.

call mvn clean package
if errorlevel 1 (
    echo.
    echo %COLOR_RED%Error: Build failed%COLOR_RESET%
    echo Please fix build errors and try again
    exit /b 1
)

echo.
echo %COLOR_GREEN%  [OK] Build successful%COLOR_RESET%
echo.

REM Verify SDK zip exists
if not exist "target\scrum-language-%NEW_VERSION%-sdk.zip" (
    echo %COLOR_RED%Error: SDK zip not found%COLOR_RESET%
    echo Expected: target\scrum-language-%NEW_VERSION%-sdk.zip
    exit /b 1
)
echo   [OK] SDK zip created: target\scrum-language-%NEW_VERSION%-sdk.zip
echo.

echo %COLOR_BLUE%Step 4: Testing Installation (Optional)...%COLOR_RESET%
set /p TEST_INSTALL="Test installation locally? (y/N): "

if /i "%TEST_INSTALL%"=="y" (
    echo.
    echo Installing to C:\scrum-test...
    
    REM Clean test directory
    if exist C:\scrum-test rmdir /s /q C:\scrum-test
    
    REM Extract and install
    cd target
    powershell -Command "Expand-Archive -Path scrum-language-%NEW_VERSION%-sdk.zip -DestinationPath . -Force"
    cd scrum-%NEW_VERSION%
    call installers\install.bat C:\scrum-test
    cd ..\..
    
    if errorlevel 1 (
        echo %COLOR_RED%Error: Installation test failed%COLOR_RESET%
        exit /b 1
    )
    
    echo   [OK] Installation test successful
    echo.
)

echo %COLOR_BLUE%Step 5: Committing Changes...%COLOR_RESET%

git add pom.xml
git commit -m "Bump version to %NEW_VERSION%"

if errorlevel 1 (
    echo %COLOR_RED%Error: Git commit failed%COLOR_RESET%
    exit /b 1
)

echo   [OK] Changes committed
echo.

echo %COLOR_BLUE%Step 6: Creating Git Tag...%COLOR_RESET%

REM Check if tag already exists
git tag | findstr "v%NEW_VERSION%" >nul
if not errorlevel 1 (
    echo %COLOR_YELLOW%Warning: Tag v%NEW_VERSION% already exists%COLOR_RESET%
    set /p DELETE_TAG="Delete existing tag? (y/N): "
    if /i "!DELETE_TAG!"=="y" (
        git tag -d v%NEW_VERSION%
        git push origin :refs/tags/v%NEW_VERSION%
    ) else (
        echo Release cancelled
        exit /b 1
    )
)

git tag -a v%NEW_VERSION% -m "Release version %NEW_VERSION%"

if errorlevel 1 (
    echo %COLOR_RED%Error: Failed to create tag%COLOR_RESET%
    exit /b 1
)

echo   [OK] Tag v%NEW_VERSION% created
echo.

echo %COLOR_BLUE%Step 7: Pushing to GitHub...%COLOR_RESET%
echo.

echo This will push the commit and tag to GitHub, triggering the release workflow.
set /p PUSH_CONFIRM="Push to GitHub? (y/N): "

if /i not "%PUSH_CONFIRM%"=="y" (
    echo.
    echo Release process stopped before pushing
    echo.
    echo To complete manually:
    echo   git push origin master
    echo   git push origin v%NEW_VERSION%
    exit /b 0
)

REM Push commit
git push origin master
if errorlevel 1 (
    echo %COLOR_RED%Error: Failed to push commit%COLOR_RESET%
    exit /b 1
)

REM Push tag
git push origin v%NEW_VERSION%
if errorlevel 1 (
    echo %COLOR_RED%Error: Failed to push tag%COLOR_RESET%
    exit /b 1
)

echo.
echo %COLOR_GREEN%========================================%COLOR_RESET%
echo %COLOR_GREEN%Release Process Complete!%COLOR_RESET%
echo %COLOR_GREEN%========================================%COLOR_RESET%
echo.
echo Version: %NEW_VERSION%
echo Tag: v%NEW_VERSION%
echo.
echo Next Steps:
echo 1. Monitor GitHub Actions workflow:
echo    https://github.com/janvanwassenhove/scrum/actions
echo.
echo 2. Verify release when workflow completes:
echo    https://github.com/janvanwassenhove/scrum/releases
echo.
echo 3. Test installation from GitHub:
echo    Download and install SDK from release page
echo.

endlocal
exit /b 0
