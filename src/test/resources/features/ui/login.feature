@ui @login
Feature: Login

  As a user I want to log in to SauceDemo so I can access the products page.
  When non-valid credentials are provided I expect to receive appropriate message.

  @smoke
  Scenario: Successful login with valid credentials
    Given I am on the Login page
    When I log in with valid credentials
    Then I should be redirected to the Products page

  @regression @validation
  Scenario Outline: Login with invalid credentials should show an error message
    Given I am on the Login page
    When I log in with "<username>" and "<password>"
    Then I should see the error message "<errorMessage>"

    Examples:
      | username      | password      | errorMessage                                                              |
      |               |               | Epic sadface: Username is required                                        |
      | standard_user |               | Epic sadface: Password is required                                        |
      | standard_user | invalid_pass  | Epic sadface: Username and password do not match any user in this service |
      | invalidUser   | secret_sauce  | Epic sadface: Username and password do not match any user in this service |
      | invalid_User  | invalidPass1# | Epic sadface: Username and password do not match any user in this service |
