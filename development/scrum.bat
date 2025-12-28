@echo off
for %%i in ("%~dp0.") do set "SCRUM_HOME=%%~fi"

REM Try development layout first (scrum.jar in same directory)
if exist "%SCRUM_HOME%\scrum.jar" (
    java -jar "%SCRUM_HOME%\scrum.jar" %*
    exit /b %ERRORLEVEL%
)

REM Try SDK layout (scrum-*.jar in ../lib directory)
for %%f in ("%SCRUM_HOME%\..\lib\scrum-*.jar") do (
    if exist "%%f" (
        java -jar "%%f" %*
        exit /b %ERRORLEVEL%
    )
)

echo Error: scrum.jar not found
echo Expected: %SCRUM_HOME%\scrum.jar
echo       or: %SCRUM_HOME%\..\lib\scrum-*.jar
exit /b 1
