@echo off
REM Script to bump the mod version on Windows
REM Usage: scripts\bump-version.bat [major|minor|patch]

setlocal enabledelayedexpansion

set "GRADLE_PROPERTIES=gradle.properties"
set "TYPE=%1"
if "%TYPE%"=="" set "TYPE=patch"

REM Read current version
for /f "tokens=2 delims==" %%a in ('findstr "mod_version=" %GRADLE_PROPERTIES%') do set "CURRENT_VERSION=%%a"

REM Parse version parts
for /f "tokens=1,2,3 delims=." %%a in ("%CURRENT_VERSION%") do (
    set "MAJOR=%%a"
    set "MINOR=%%b"
    set "PATCH=%%c"
)

REM Bump version based on type
if "%TYPE%"=="major" (
    set /a MAJOR+=1
    set "MINOR=0"
    set "PATCH=0"
) else if "%TYPE%"=="minor" (
    set /a MINOR+=1
    set "PATCH=0"
) else if "%TYPE%"=="patch" (
    set /a PATCH+=1
) else (
    echo Invalid version type. Use: major, minor, or patch
    exit /b 1
)

set "NEW_VERSION=%MAJOR%.%MINOR%.%PATCH%"

echo Bumping version from %CURRENT_VERSION% to %NEW_VERSION%

REM Update gradle.properties
powershell -Command "(Get-Content %GRADLE_PROPERTIES%) -replace 'mod_version=.*', 'mod_version=%NEW_VERSION%' | Set-Content %GRADLE_PROPERTIES%"

echo Version bumped to %NEW_VERSION%!
echo.
echo Next steps:
echo 1. Update CHANGELOG.md with changes
echo 2. Commit: git commit -am "Bump version to %NEW_VERSION%"
echo 3. Tag: git tag -a v%NEW_VERSION% -m "Release v%NEW_VERSION%"
echo 4. Push: git push ^&^& git push --tags
