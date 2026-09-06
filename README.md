# EnduroSat Test Automation Framework

Test automation framework developed as part of the EnduroSat Automation
QA technical assignment.

The project covers both **UI** and **API** automated testing and is
built with Java, Maven, TestNG, Cucumber, Selenium WebDriver, and
RestAssured.

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

## Project Structure

``` text
src/test
├── java/com/automation
│   ├── api
│   │   ├── builders
│   │   ├── clients
│   │   ├── hooks
│   │   ├── models
│   │   ├── steps
│   │   └── utilities
│   ├── config
│   │   └── models
│   ├── context
│   ├── runners
│   └── ui
│       ├── driver
│       ├── factories
│       ├── hooks
│       ├── models
│       ├── pages
│       ├── steps
│       └── utilities
└── resources
    ├── features
    │   ├── api
    │   └── ui
    ├── appsettings.json
    └── logback.xml
```

## Framework Design

The framework separates UI automation, API automation, configuration,
shared scenario state, and test execution concerns.

### UI Layer

The UI implementation uses Page Object Model classes to keep element
locators and page interactions separate from Cucumber step definitions.

`DriverFactory` is responsible for WebDriver creation and cleanup.
Browser type and headless execution are controlled through external
configuration.

Reusable explicit-wait based actions are provided through UI utilities.
No fixed `Thread.sleep()` waits are used.

### API Layer

API requests are encapsulated in reusable client classes such as
`AuthClient` and `BookingClient`. Cucumber step definitions use these
clients instead of making raw RestAssured calls.

Request and response data is represented by typed models.
`BookingBuilder` and DataFaker are used to generate reusable dynamic
booking data.

API hooks handle authentication where required and clean up bookings
created during scenarios.

### Scenario Context

`ScenarioContext` provides shared scenario-level state between Cucumber
steps. It is used for values such as selected products, API responses,
booking IDs, authentication tokens, and expected test data.

### Configuration

Runtime configuration is externalized in:

``` text
src/test/resources/appsettings.json
```

It contains settings for UI/API base URLs, browser, headless mode,
explicit-wait timeout, and test credentials.

For example, headless execution can be enabled by changing:

``` json
"headless": true
```

The configuration is loaded by `ConfigReader`.

## Prerequisites

Before running the tests, install:

- Java 17 or later;
- Apache Maven 3.9+;
- Google Chrome.

Verify the installations:

``` bash
java -version
mvn -version
```

## Running the Tests

Run the commands from the project root directory.

### Run all tests

``` bash
mvn test
```

### Run API tests

``` bash
mvn test "-Dcucumber.filter.tags=@api"
```

### Run UI tests

``` bash
mvn test "-Dcucumber.filter.tags=@ui"
```

### Run smoke tests

``` bash
mvn test "-Dcucumber.filter.tags=@smoke"
```

### Combine Cucumber tags

For example, run only API smoke scenarios:

``` bash
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
| `@negative`       | Negative API scenarios                      |
| `@e2e`            | End-to-end scenarios                        |
| `@auth`           | Scenarios requiring authenticated API setup |
| `@cleanupBooking` | Scenarios requiring booking cleanup         |

Feature-specific tags such as `@login`, `@products`, `@checkout`,
`@authentication`, and `@booking` provide additional filtering.

## Test Reports

Cucumber generates both HTML and JSON reports after execution:

``` text
target/cucumber-report.html
target/cucumber-report.json
```

The HTML report is the primary human-readable execution report.

A generated full-suite HTML report is also included with the assignment
deliverables so the latest complete execution can be reviewed without
rerunning the tests.

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

## Notes

The public demo APIs and websites used by this project may occasionally
respond slowly or be temporarily unavailable. Such external availability
is outside the framework's control.

The framework intentionally focuses on readability, separation of
responsibilities, reusable components, and maintainable test flows
rather than unnecessary complexity.

## Future Improvements

Given additional development time, the framework could be extended with:

- **CI pipeline** – configure automated execution on pull requests and/or scheduled runs,
  with separate UI and API suites.

- **Allure reporting in CI** – add richer reporting with history, attachments, failure
  details, and published CI artifacts.

- **Environment-specific configuration** – introduce configuration files such as
  `appsettings.stage.json` and select the target environment at runtime.

- **API health check** – use the Restful Booker `/ping` endpoint before API execution
  to detect service availability problems before running the test suite.

- **PATCH endpoint coverage** – add automated tests for partial booking updates using
  `PATCH /booking/{id}`, including authenticated and negative scenarios.

- **Extended API validation coverage** – add further boundary and business-rule
  validation tests, particularly around booking dates, invalid formats, price values,
  and inconsistent API validation behaviour observed during exploratory testing.

- **UI download validation** – extend the UI coverage to validate the order/download
  functionality where applicable, including verification of the downloaded file.

- **Parallel execution** – evaluate parallel execution for independent scenarios to
  reduce the overall suite execution time while ensuring WebDriver and scenario state
  remain thread-safe.