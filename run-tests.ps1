param(
    [string]$Env = "dev",
    [string]$Test = "tests.SmokeTest",
    [string]$Headless = "true",
    [switch]$All
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
