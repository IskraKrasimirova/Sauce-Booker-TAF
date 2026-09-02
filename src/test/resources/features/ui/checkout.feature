@ui @checkout
Feature: Checkout

  As a logged-in user I want to complete the checkout process,
  so that I can purchase the products in my cart.

  Background:
    Given I am logged in
    And I am on the Products page

  @smoke
  Scenario: Successfully purchase a product
    When I add a product to the cart
    And I open the cart
    And I proceed to checkout
    And I enter valid checkout information
    And I continue to the checkout overview
    Then the checkout overview should contain the selected product
    And the order total should be correct
    And I finish the order successfully

  @regression @validation
  Scenario Outline: Checkout with missing mandatory field should show an error
    When I add a product to the cart
    And I open the cart
    And I proceed to checkout
    And I enter checkout information with missing "<field>"
    And I submit the invalid checkout information
    Then I should see the checkout error message "<errorMessage>"

    Examples:
      | field     | errorMessage                   |
      | firstName | Error: First Name is required  |
      | lastName  | Error: Last Name is required   |
      | zipCode   | Error: Postal Code is required |
      | all       | Error: First Name is required  |

  @regression @e2e
  Scenario: Successfully purchase multiple products
    When I add multiple products to the cart
    And I open the cart
    And I remove one product from the cart
    And I proceed to checkout
    And I enter valid checkout information
    And I continue to the checkout overview
    Then the checkout overview should contain the remaining products
    And the order total should be correct
    And I finish the order successfully