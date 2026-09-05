@api @booking
Feature: Booking Management API

  Background:
    Given I create a new booking with valid details

  @regression @auth @cleanupBooking
  Scenario: Update a newly created booking with valid data
    When I update the created booking with valid details
    Then the response status should be 200
    And the updated booking details should be returned


  @e2e @auth @cleanupBooking
  Scenario: Create, update and retrieve a booking
    When I update the created booking with valid details
    Then the response status should be 200
    And I request the created booking by id
    And the response status should be 200
    And the retrieved booking should contain the updated details

  @regression @auth
  Scenario: Delete a newly created booking
    When I delete the created booking
    Then the response status should be 201
    And I request the created booking by id
    And the response status should be 404
    And the response should contain the following message "Not Found"

  @negative @cleanupBooking
  Scenario: Update a booking without authentication
    When I update the created booking without authentication
    Then the response status should be 403
    And the response should contain the following message "Forbidden"

  @negative @cleanupBooking
  Scenario: Delete a booking without authentication
    When I delete the created booking without authentication
    Then the response status should be 403
    And the response should contain the following message "Forbidden"