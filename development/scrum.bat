@echo off
for %%i in ("%~dp0.") do set "SCRUM_HOME=%%~fi"
echo Path to scrum is: %SCRUM_HOME%

java -jar %SCRUM_HOME%\scrum.jar %*
