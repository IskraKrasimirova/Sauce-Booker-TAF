@api @booking
Feature: Booking API

  @smoke
  Scenario: Get an existing booking by id
    Given I request all existing bookings
    And I select an existing booking id
    When I request the selected booking by id
    Then the booking response status should be 200
    And the booking details should be returned

  @smoke
  Scenario: Create a new booking
    Given I create a new booking with valid details
    Then the booking response status should be 200
    And the created booking details should be returned