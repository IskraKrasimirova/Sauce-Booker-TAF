@api @booking
Feature: Booking API

  @smoke
  Scenario: Get an existing booking by id
    Given I request all existing bookings
    And I select an existing booking id
    When I request the selected booking by id
    Then the response status should be 200
    And the booking details should be returned

  @regression @negative
  Scenario Outline: Get booking with invalid or non-existing id
    Given I request a booking with invalid id "<bookingId>"
    Then the response status should be 404
    And the response should contain the following message "Not Found"

    Examples:
      | bookingId |
      | 0         |
      | -1        |
      | 999999999 |
      | null      |
      | abc       |


  @smoke @cleanupBooking
  Scenario: Create a new booking with valid data
    Given I create a new booking with valid details
    Then the response status should be 200
    And the created booking details should be returned


  @negative
  Scenario Outline: Create booking with missing required field
    Given I create a new booking without the "<field>" field
    Then the response status should be 500
    And the response should contain the following message "Internal Server Error"

    Examples:
      | field        |
      | firstname    |
      | lastname     |
      | totalprice   |
      | depositpaid  |
      | bookingdates |


  @negative
  Scenario Outline: Create booking with null required field
    Given I create a new booking with null value for the "<field>" field
    Then the response status should be 500
    And the response should contain the following message "Internal Server Error"

    Examples:
      | field        |
      | firstname    |
      | lastname     |
      | totalprice   |
      | depositpaid  |
      | bookingdates |


  @negative
  Scenario Outline: Create booking with missing required booking date field
    Given I create a new booking without the "<field>" booking date field
    Then the response status should be 500
    And the response should contain the following message "Internal Server Error"

    Examples:
      | field    |
      | checkin  |
      | checkout |


  @negative
  Scenario Outline: Create booking with null required booking date field
    Given I create a new booking with null value for the "<field>" booking date field
    Then the response status should be 500
    And the response should contain the following message "Internal Server Error"

    Examples:
      | field    |
      | checkin  |
      | checkout |


  @negative
  Scenario: Create booking without request body
    Given I create a new booking without request body
    Then the response status should be 500
    And the response should contain the following message "Internal Server Error"

