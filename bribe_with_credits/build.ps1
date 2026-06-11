# Compile script for Starsector mod
# Requires JDK 17 and Starsector installation

# Configuration - customize these paths as needed
$JDK_PATH = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { "C:\Program Files\Amazon Corretto\jdk17.0.19_10" }
$STARSECTOR_PATH = "D:\Starsector"
$SRC_DIR = "src"
$OUT_DIR = "out"
$JAR_DIR = "jars"
$JAR_NAME = "BribeWithCredits.jar"

# Set javac and jar paths
if ($JDK_PATH) {
    $javac = "$JDK_PATH\bin\javac.exe"
    $jar = "$JDK_PATH\bin\jar.exe"
} else {
    $javac = "javac"
    $jar = "jar"
}

# Check if javac exists
if ($JDK_PATH -and -not (Test-Path $javac)) {
    Write-Host "Error: javac not found at $javac" -ForegroundColor Red
    Write-Host "Please set JDK_PATH in this script or set JAVA_HOME environment variable" -ForegroundColor Yellow
    exit 1
}

# Create output directories
New-Item -ItemType Directory -Force -Path $OUT_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $JAR_DIR | Out-Null

Write-Host "Compiling Java sources..."

# Find all Java files
$javaFiles = Get-ChildItem -Path $SRC_DIR -Filter "*.java" -Recurse

# Build classpath (Starsector core jars)
$classpath = @(
    "$STARSECTOR_PATH\starsector-core\starfarer.api.jar",
    "$STARSECTOR_PATH\starsector-core\starfarer_obf.jar"
) -join ";"

# Compile
& $javac -cp $classpath -d $OUT_DIR $javaFiles.FullName

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Creating JAR file..."

# Create JAR
Set-Location $OUT_DIR
& $jar cf "..\$JAR_DIR\$JAR_NAME" *
Set-Location ..

Write-Host "Build complete! JAR created at $JAR_DIR\$JAR_NAME" -ForegroundColor Green
