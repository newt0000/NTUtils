@echo off
setlocal enabledelayedexpansion

set "FILE=%~dp0src\main\resources\version.properties"
set "TEMP=%~dp0temp.properties"

echo running build upgrade...
echo version file location: %FILE%

if not exist "%FILE%" (
    echo major=1 > "%FILE%"
    echo minor=0 >> "%FILE%"
    echo patch=0 >> "%FILE%"
    echo build=0 >> "%FILE%"
)

set "BUILD=0"

for /f "tokens=1,2 delims==" %%A in ('findstr /b "build=" "%FILE%"') do (
    set "BUILD=%%B"
)
echo Current build: %BUILD%
set /a BUILD=BUILD+1

> "%TEMP%" (
    for /f "usebackq delims=" %%L in ("%FILE%") do (
        set "LINE=%%L"
        echo !LINE! | findstr /b "build=" >nul
        if errorlevel 1 (
            echo %%L
        ) else (
            echo build=!BUILD!
        )
    )
)

move /y "%TEMP%" "%FILE%" >nul

echo ✔ Next Build incremented to !BUILD!
endlocal