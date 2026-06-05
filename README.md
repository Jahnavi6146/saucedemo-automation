# SauceDemo Automation — Selenium + Java + TestNG

End-to-end UI automation for [SauceDemo](https://www.saucedemo.com/), covering
**20 positive**, **15 negative**, and **5 end-to-end** test cases derived from the
manual test suite.

## Framework Choice & Why

| Choice | Reason |
|--------|--------|
| **Selenium 4 (Java)** | Industry-standard, mature WebDriver; Selenium Manager auto-resolves the browser driver so there is no `chromedriver.exe` to maintain. |
| **TestNG** | Powerful runner — data providers (parameterised login/checkout cases), grouping, assertions, and built-in parallel execution. |
| **Page Object Model (POM)** | Each page is a class; locators live in one place. Tests read like business steps, so UI changes need a one-line fix, not a rewrite. |
| **ExtentReports** | Clean HTML report with pass/fail/skip and auto-attached screenshots on failure. |
| **Maven** | Standard build/dependency tool; one `mvn test` runs the whole suite locally and in CI. |

### Project Structure
src/main/java/com/saucedemo/
├── config/     ConfigReader (config.properties + -D overrides)
├── driver/     DriverFactory (ThreadLocal WebDriver, headless support)
├── base/       BasePage (shared waits & actions)
├── pages/      LoginPage, InventoryPage, CartPage, Checkout* pages
├── reports/    ExtentManager
├── listeners/  TestListener (reporting + screenshot on failure)
└── utils/      ScreenshotUtil
src/test/java/com/saucedemo/
├── base/       BaseTest (driver setup/teardown)
└── tests/      Positive / Negative / E2E test classes



## How to Run
--bash
# Run everything (headed)
mvn test

# Run headless (CI mode)
mvn test -Dheadless=true

# Run a single browser/suite
mvn test -Dbrowser=firefox

HTML report is generated at reports/extent-report.html.

Continuous Integration
A GitHub Actions workflow (.github/workflows/ci.yml) runs the full suite headless on every push,
then uploads the Extent report and failure screenshots as build artifacts.

Extension Plan
Parallelisation

Currently parallel="classes" with 3 threads via TestNG.
Next: scale thread-count and shard by suite; integrate Selenium Grid / Docker or a cloud grid (BrowserStack/LambdaTest) for true cross-browser, cross-OS parallel runs.
Reporting

ExtentReports HTML today.
Next: Allure reporting with history/trends, plus failure screenshots & video embedding; publish the report to GitHub Pages and post a pass/fail summary back to the PR.
Coverage / Maintainability

Externalise test data to JSON/Excel for data-driven expansion.
Add API-level setup (fast cart seeding) to keep UI tests focused and quick.
