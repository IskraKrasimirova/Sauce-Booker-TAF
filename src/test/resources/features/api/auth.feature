@api @authentication
Feature: Authentication API

  @smoke
  Scenario: Authenticate with valid credentials
    Given I authenticate with valid API credentials
    Then the response status should be 200
    And an authentication token should be returned

  @negative
  Scenario: Authenticate with invalid credentials
    Given I authenticate with invalid API credentials
    Then the response status should be 200
    And the response should contain the following message "Bad credentials"