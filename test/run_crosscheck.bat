
if not exist "%~dp0..\crosscheck\build\install\crosscheck\bin\crosscheck.bat" (
cd ..
run gradlew :crosscheck:installDist
) else (
cd %~dp0..\crosscheck\build\install\crosscheck\bin\
crosscheck -fmu 

)

PAUSE
