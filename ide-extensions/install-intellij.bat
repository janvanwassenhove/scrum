@echo off
REM Build and Install SCRUM Language Plugin for IntelliJ IDEA

echo Building SCRUM Language Plugin for IntelliJ IDEA...
echo.

cd /d "%~dp0intellij-scrum"

REM Check if Gradle wrapper exists
if exist gradlew.bat (
    echo Using Gradle wrapper...
    call gradlew.bat buildPlugin
    if %errorlevel% neq 0 (
        echo.
        echo ✗ Build failed!
        echo.
        pause
        exit /b 1
    )
    echo.
    echo ✓ Plugin built successfully!
    echo.
    echo Plugin JAR location: build\distributions\
    echo.
    echo To install:
    echo 1. Open IntelliJ IDEA
    echo 2. Go to Settings ^(Ctrl+Alt+S^) -^> Plugins
    echo 3. Click the gear icon ⚙️ -^> Install Plugin from Disk
    echo 4. Select the .zip file from build\distributions\
    echo 5. Restart IntelliJ IDEA
    echo.
) else (
    echo.
    echo Gradle wrapper not found. Manual compilation required.
    echo.
    echo To build manually:
    echo 1. Open 'intellij-scrum' folder in IntelliJ IDEA
    echo 2. Ensure Plugin DevKit is installed
    echo 3. Run -^> Run 'Plugin' to test in sandbox
    echo 4. Build -^> Build Project
    echo 5. Create plugin artifact for distribution
    echo.
    echo Or install Gradle and run: gradle buildPlugin
    echo.
)

pause
