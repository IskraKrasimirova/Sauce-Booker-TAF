# Reflection

### If you had two more weeks, what would you improve or add first, and why?

I would prioritize the next improvements based on business needs, product risk,
and the impact of the defects already identified. Critical issues affecting
the correct functioning of the application would come before extending the
automation coverage.

Depending on those priorities, I would consider CI integration, improved
reporting, environment and browser support, and additional UI and API coverage
described in the README.

### How would you handle flaky tests in CI?

I would first investigate the failures using logs and test reports to identify
the actual cause rather than immediately adding retries.

For UI tests, I would check locator stability, synchronization, and whether
slow backend responses affect page or element loading. Since CI can be slower
than local execution, I would also consider environment-specific explicit-wait
timeouts.

For API tests, I would check service availability, response times,
authentication, test-data creation, and cleanup to determine whether the
failure is caused by the test itself or by instability in the test environment.

### What trade-offs did you consciously make given the time limit?

Given the time limit, I prioritized a clear and maintainable framework and
representative UI and API coverage over implementing every possible scenario.

For UI testing, I focused on the main user flows and left additional coverage,
such as product sorting and download validation, for future development.

For API testing, I left PATCH requests and the health-check endpoint for future
coverage. I also did not extend some validation tests for POST and PUT requests
because the application validation behaviour was inconsistent, making reliable
assertions difficult.

CI integration, parallel execution, cross-browser support, and advanced
reporting were left as future improvements.