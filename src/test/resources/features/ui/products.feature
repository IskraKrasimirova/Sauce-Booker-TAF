@ui @products
Feature: Products

  As a logged-in user I want to browse and interact with the available products,
  to view product information and add products to my cart.

  Background:
  Given I am logged in
    And I am on the Products page

  @smoke
  Scenario: Add a product to the cart
    When I add a product to the cart
    Then the selected product should be marked as added
    And the cart should contain the selected product

  @regression
  Scenario: Added product state is preserved across product pages and cart
    When I open the details of a product
    And I add the product to the cart from the product details page
    Then the product should be marked as added on the product details page
    And the product details should match the selected product
    And the product should be marked as added on the Products page
    And I open the cart
    And the cart should contain the selected product
    And the product is removed from the cart
    And the product should be marked as not added on the Products page