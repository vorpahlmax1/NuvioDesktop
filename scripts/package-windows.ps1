# Package MultiNutz Desktop for Windows (MSI + portable zip).
# Run on Windows with JDK 17+, WiX 3.x on PATH, and WebView2 SDK available.
#
# Usage:
#   .\scripts\package-windows.ps1 -WebView2Root "C:\path\to\Microsoft.Web.WebView2.1.0.x"
#   .\gradlew.bat :composeApp:packageReleaseMsi --rerun-tasks
#
# Outputs:
#   composeApp\build\compose\release-msis\MultiNutz-Windows-x64-<version>.msi
#   composeApp\build\compose\release-zips\MultiNutz-Windows-x64-<version>.zip

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$WebView2Root,

    [Parameter(Mandatory = $false)]
    [string]$Version = ""
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $repoRoot

if (-not $Version) {
    $versionFile = Join-Path $repoRoot "composeApp\Configuration\DesktopVersion.properties"
    $Version = (
        Get-Content $versionFile |
            Where-Object { $_ -match '^VERSION_NAME=(.+)$' } |
            ForEach-Object { $Matches[1].Trim() } |
            Select-Object -First 1
    )
}
if (-not $Version) {
    throw "Could not resolve desktop VERSION_NAME."
}

$loaderLibrary = Join-Path $WebView2Root "build\native\x64\WebView2Loader.dll.lib"
if (-not (Test-Path $loaderLibrary)) {
    throw "WebView2Loader.dll.lib was not found at $loaderLibrary"
}

Write-Host "Packaging MultiNutz $Version (MSI + distributable)..."

& .\gradlew.bat `
    :composeApp:packageReleaseMsi `
    :composeApp:createReleaseDistributable `
    "-Pnuvio.webview2.dir=$WebView2Root" `
    "-Pcompose.desktop.packaging.checkJdkVendor=false" `
    --no-configuration-cache `
    --no-daemon `
    --stacktrace
if ($LASTEXITCODE -ne 0) {
    throw "Gradle packaging failed with exit code $LASTEXITCODE."
}

$msiPath = Join-Path $repoRoot "composeApp\build\compose\release-msis\MultiNutz-Windows-x64-$Version.msi"
if (-not (Test-Path $msiPath)) {
    throw "Expected MSI was not produced: $msiPath"
}

$appRootCandidates = @(
    (Join-Path $repoRoot "composeApp\build\compose\binaries\main-release\app\MultiNutz"),
    (Join-Path $repoRoot "composeApp\build\compose\binaries\main-release\app\Nuvio")
)
$appRoot = $appRootCandidates | Where-Object { Test-Path $_ } | Select-Object -First 1
if (-not $appRoot) {
    throw "Expected createReleaseDistributable app folder under composeApp\build\compose\binaries\main-release\app\"
}

$exeCandidates = @(
    (Join-Path $appRoot "MultiNutz.exe"),
    (Join-Path $appRoot "Nuvio.exe")
)
$exePath = $exeCandidates | Where-Object { Test-Path $_ } | Select-Object -First 1
if (-not $exePath) {
    throw "Expected launcher exe under $appRoot"
}

$zipDir = Join-Path $repoRoot "composeApp\build\compose\release-zips"
New-Item -ItemType Directory -Force -Path $zipDir | Out-Null
$zipPath = Join-Path $zipDir "MultiNutz-Windows-x64-$Version.zip"
if (Test-Path $zipPath) {
    Remove-Item $zipPath -Force
}

# Zip the app folder so extraction yields MultiNutz\MultiNutz.exe (portable).
Compress-Archive -Path $appRoot -DestinationPath $zipPath -CompressionLevel Optimal

Write-Host "MSI: $msiPath"
Write-Host "ZIP: $zipPath"
Write-Host "Launcher in zip: $(Split-Path $appRoot -Leaf)\$(Split-Path $exePath -Leaf)"
Get-Item $msiPath, $zipPath | Format-List FullName, Length, LastWriteTime
