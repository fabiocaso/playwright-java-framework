Playwright Java Test Framework

Overview
--------
Small Playwright-based Java test framework (JUnit 5 + Maven) intended for UI smoke and e2e tests.
This repo contains test helpers, a simple test-config loader, and example tests.

Quick facts
-----------
- Language: Java 17
- Build: Maven
- Test framework: JUnit 5
- Browser automation: Microsoft Playwright for Java

Project layout
--------------
- src/test/java/framework/base - Base test classes (BaseTest)
- src/test/java/framework/factory - Browser factory (PlaywrightFactory)
- src/test/java/framework/config - TestConfig loader (test-config.properties)
- src/test/java/framework/utils - Utilities (WaitUtils)
- src/test/java/tests - Test classes (SmokeTest)
- src/test/resources - Test resources (test-config.properties)

Configuration
-------------
Primary config file: `src/test/resources/test-config.properties`.
Example keys already present in that file:
- env=dev                        # active environment (dev, qa, prod)
- dev.base.url=...               # environment-specific base URLs
- qa.base.url=...
- prod.base.url=...
- timeout.seconds=30
- test.user=alice
- headless=true                  # whether to launch browser headless (can be overridden by -Dheadless)

Resolution order for any key (via TestConfig.get):
1. System property (JVM) - pass `-Dkey=value` when running Maven
2. Environment variable - key converted to UPPER_SNAKE (e.g., `BASE_URL` or `DEV_BASE_URL`)
3. `test-config.properties` value

Selecting an environment
------------------------
- Default: the `env` value inside `test-config.properties` (e.g., `env=dev`).
- Override for a single run with a system property:
  - PowerShell: mvn -Denv=qa -Dtest="tests.SmokeTest" test
- Or set an OS env var for the session:
  - PowerShell: $env:ENV = 'qa' ; mvn -Dtest="tests.SmokeTest" test
- You can also bypass environment selection entirely by specifying the base URL directly:
  - PowerShell: mvn -Dbase.url="https://qa.example.com" -Dtest="tests.SmokeTest" test

Headless mode
-------------
The browser launch headless behavior is now configurable via the `headless` key in `test-config.properties` or the `-Dheadless` system property. Default is `true`.
- Example in `test-config.properties`: `headless=false`
- One-off override for a run:
  - PowerShell: mvn -Dheadless=false -Dtest="tests.SmokeTest" test

Running tests
-------------
- Run the SmokeTest (example):
  - PowerShell: mvn -Dtest="tests.SmokeTest" test
- Run all tests:
  - PowerShell: mvn test
- Use the convenience script `run-tests.ps1` (PowerShell) to run tests per environment and toggle headless:
  - PowerShell: .\run-tests.ps1 -Env qa -Headless false -Test "tests.SmokeTest"
  - To run all tests: .\run-tests.ps1 -Env qa -Headless true -All

Utilities
---------
- `framework.utils.WaitUtils` — helpful explicit-wait helpers (waitForVisible, waitForHidden, waitForEnabled, waitForTextContains, waitForTitleContains, waitUntil).
- `framework.config.TestConfig` now offers typed helpers: `getInt(key, default)` and `getBoolean(key, default)`.

Notes & tips
------------
- `TestConfig` supports using system properties or environment variables to override any key. For CI, prefer passing `-Denv=...` so each pipeline run is explicit.
- `headless` can be configured in `test-config.properties` or via `-Dheadless` for easier debugging.
- If you want me to add Maven profiles (e.g., `-Pqa`) or additional run scripts, tell me which envs to expose and I will add it.

Contact / Next steps
---------------------
If you'd like, I can:
- Add additional typed helpers to `TestConfig` (e.g., getLong/getDuration)
- Add a small PowerShell helper script `run-tests.ps1` (already added) to simplify running tests for specific environments.
