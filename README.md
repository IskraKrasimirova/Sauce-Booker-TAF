# EnduroSat Test Automation Framework

A **BDD-first UI and API test automation framework** developed as part of the
EnduroSat Automation QA technical assignment.

The framework is built with Java, Maven, TestNG, Cucumber, Selenium WebDriver,
and RestAssured, with a focus on clear separation of responsibilities,
reusable components, and maintainable test scenarios.

## Applications Under Test

### UI

**SauceDemo**   
https://www.saucedemo.com/

SauceDemo is used for UI automation scenarios covering login, product interactions,
cart behaviour, and checkout flows.

### API

**Restful Booker**  
https://restful-booker.herokuapp.com/

API documentation:  
https://restful-booker.herokuapp.com/apidoc/index.html

Restful Booker is used for authentication, booking CRUD operations,
negative testing, and end-to-end API scenarios.

## Tech Stack

| Technology            | Purpose                                   |
|-----------------------|-------------------------------------------|
| Java 17               | Programming language                      |
| Maven 3.9+            | Build and dependency management           |
| TestNG 7+             | Test execution and assertions             |
| Cucumber 7+           | BDD scenarios and test runner integration |
| Selenium WebDriver 4+ | UI browser automation                     |
| RestAssured 6+        | REST API automation                       |
| Jackson               | JSON serialization and deserialization    |
| DataFaker             | Dynamic test data generation              |
| SLF4J + Logback       | Logging                                   |
| Cucumber HTML / JSON  | Test reporting                            |

## Test Coverage

### UI Tests

UI automation is implemented against **SauceDemo** and covers:

- successful login;
- invalid login validation using data-driven scenarios;
- adding products to the cart;
- product details and cart state validation;
- successful checkout;
- checkout mandatory-field validation;
- end-to-end purchase flow with multiple products.

The UI layer follows the **Page Object Model (POM)**. Page objects
contain page-specific locators and behaviour, while Cucumber step
definitions describe the test flow.

### API Tests

API automation is implemented against **Restful Booker** and covers:

- authentication with valid and invalid credentials;
- retrieving existing bookings;
- retrieving bookings with invalid or non-existing IDs;
- creating bookings;
- validation of missing and null required fields;
- updating bookings;
- deleting bookings;
- unauthenticated update and delete requests;
- an end-to-end create → update → retrieve booking lifecycle.

The API layer uses reusable client classes so that RestAssured calls are
kept outside the Cucumber step definitions.

## Architecture & Design

The framework follows a **BDD-first approach** with a layered architecture
that separates test scenarios, test logic, application interactions, and
infrastructure concerns.

- **BDD-first approach** - test scenarios are described in Gherkin and
  implemented through Cucumber step definitions.

- **Layered Test Architecture** - Features → Steps → Pages / API Clients →
  Infrastructure.

- **Dependency Injection with Cucumber PicoContainer** - used for constructor
  injection of scenario-scoped dependencies such as `ScenarioContext`.

- **Configuration Management** - runtime settings are externalized in
  `appsettings.json`.

- **Page Object Model** - UI locators and page-specific interactions are
  encapsulated in Page Object classes.

- **API Client Layer** - RestAssured requests are encapsulated in reusable API
  clients instead of being called directly from step definitions.

- **Builder Pattern** - used to create reusable and customizable API test data.

- **Factory Pattern** - used for reusable UI test data creation.

- **Shared Scenario Context** - scenario-specific data such as API responses,
  booking IDs, authentication tokens, and selected products is shared between
  steps without relying on global test state.

- **Extensible Design** - UI and API layers are separated, making it easier to
  add new pages, endpoints, browsers, and environments.

## Project Structure

```text
reports
└── cucumber-report.html    # Latest committed HTML test report

src/test
├── java/com/automation
│   ├── api
│   │   ├── builders        # Test data builders
│   │   ├── clients         # Reusable RestAssured API clients
│   │   ├── hooks           # API setup and cleanup
│   │   ├── models          # Request/response models
│   │   ├── steps           # API step definitions
│   │   └── utilities       # API constants and helpers
│   │
│   ├── config              # Configuration loading and models
│   ├── context             # Shared ScenarioContext
│   ├── runners             # Cucumber/TestNG test entry point
│   │
│   └── ui
│       ├── driver          # WebDriver creation and lifecycle
│       ├── factories       # UI test data creation
│       ├── hooks           # UI setup and teardown
│       ├── models          # UI domain models
│       ├── pages           # Page Object abstractions
│       ├── steps           # UI step definitions
│       └── utilities       # Waits and reusable UI helpers
│
└── resources
    ├── features
    │   ├── api             # API Gherkin feature files
    │   └── ui              # UI Gherkin feature files
    ├── appsettings.json    # Runtime configuration
    └── logback.xml         # Logging configuration
```

## Configuration

Runtime configuration is externalized in:

```text
src/test/resources/appsettings.json
```

It contains settings for UI/API base URLs, browser, headless mode,
explicit-wait timeout, and test credentials.

Configuration values can be changed directly in `appsettings.json` before
test execution. Environment-specific configuration files and runtime
environment selection are considered as a future improvement.

For example, headless execution can be enabled by changing:

```json
"headless": true
```

The configuration is loaded by `ConfigReader`.

## Prerequisites

Before running the tests, install:

- Java 17 or later;
- Apache Maven 3.9+;
- Google Chrome.

Verify the installations:

```bash
java -version
mvn -version
```

## Running the Tests

Run the commands from the project root directory.

### Run all tests

```bash
mvn test
```

### Run API tests

```bash
mvn test "-Dcucumber.filter.tags=@api"
```

### Run UI tests

```bash
mvn test "-Dcucumber.filter.tags=@ui"
```

### Run smoke tests

```bash
mvn test "-Dcucumber.filter.tags=@smoke"
```

### Combine Cucumber tags

For example, run only API smoke scenarios:

```bash
mvn test "-Dcucumber.filter.tags=@api and @smoke"
```

Other Cucumber tag expressions can be created using `and`, `or`, and
`not`.

## Cucumber Tags

| Tag               | Purpose                                     |
|-------------------|---------------------------------------------|
| `@ui`             | UI test suite                               |
| `@api`            | API test suite                              |
| `@smoke`          | Core smoke coverage                         |
| `@regression`     | Regression coverage                         |
| `@validation`     | UI validation scenarios                     |
| `@negative`       | Negative API scenarios                      |
| `@e2e`            | End-to-end scenarios                        |
| `@auth`           | Scenarios requiring authenticated API setup |
| `@cleanupBooking` | Scenarios requiring booking cleanup         |

Feature-specific tags such as `@login`, `@products`, `@checkout`,
`@authentication`, and `@booking` provide additional filtering.

## Test Reports

Cucumber generates HTML and JSON reports after test execution:

```text
target/cucumber-report.html
target/cucumber-report.json
```

In addition, the HTML report is generated in the `reports` directory:

```text
reports/cucumber-report.html
```

The report in the `reports` directory is included with the project so the
latest committed test execution can be reviewed without rerunning the tests.

## Logging

The framework uses **SLF4J with Logback** for basic execution and
diagnostic logging.

Examples include WebDriver startup/shutdown, API cleanup status, and
unexpected API response status/body when a status-code assertion fails.

Successful API requests are intentionally not logged in full to keep
console output concise. Credentials and authentication tokens are not
written to the logs.

## Data-Driven Testing

Cucumber `Scenario Outline` and `Examples` tables provide data-driven
coverage for invalid login combinations, invalid/non-existing booking
IDs, missing/null booking fields, and checkout mandatory-field
validation.

Dynamic booking and product data is also generated or selected at
runtime where appropriate.

## Future Improvements

Given additional development time, the framework could be extended with:

- **CI pipeline** - configure automated UI and API test execution on pull
  requests and scheduled runs.

- **Allure reporting in CI** - add richer reporting with test history,
  attachments, and detailed failure information.

- **Environment-specific configuration** - introduce configuration files such
  as `appsettings.stage.json` and allow the target environment to be selected
  at runtime.

- **Parallel execution** - evaluate parallel execution for independent scenarios
  to reduce the overall suite execution time while keeping WebDriver and
  scenario state thread-safe.

- **Cross-browser execution** - extend WebDriver support beyond Chrome and
  execute the UI test suite across multiple browsers.

- **Failure screenshots** - automatically capture screenshots when UI scenarios
  fail and attach them to the test report.

- **Download validation** - extend the UI coverage to validate the order
  download functionality, including verification of the downloaded file and
  its content.

- **Sorting validation** - extend the UI coverage to verify product sorting
  by name and price in both ascending and descending order.

- **API health check** - use the `/ping` endpoint before API test execution to
  distinguish service availability issues from test failures.

- **PATCH endpoint coverage** - add automated tests for partial booking updates,
  including positive and negative scenarios.

- **Extended API validation coverage** - add further boundary and business-rule
  validation tests for cases such as past booking dates, checkout before
  check-in, invalid data types for guest names, negative prices, and other
  inconsistent validation behaviour observed during testing.