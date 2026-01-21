param(
    [string]$Env = "dev",
    [string]$Test = "tests.SmokeTest",
    [string]$Headless = "true",
    [switch]$All,
    [switch]$Report
)

# Set the ENV for this session
$env:ENV = $Env

# Build the -Dargs
$props = "-Denv=$Env -Dheadless=$Headless"
if ($All) {
    Write-Host "Running all tests for env=$Env (headless=$Headless)..."
    mvn $props test
} else {
    Write-Host "Running test '$Test' for env=$Env (headless=$Headless)..."
    mvn $props -Dtest="$Test" test
}

if ($Report) {
    Write-Host "Generating Surefire HTML report..."
    mvn surefire-report:report
    Write-Host "HTML report available at target/site/surefire-report.html"
    Write-Host "JUnit XML reports are under target/surefire-reports/"
}
