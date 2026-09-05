package com.automation.api.steps;

import com.automation.api.builders.BookingBuilder;
import com.automation.api.clients.BookingClient;
import com.automation.api.models.Booking;
import com.automation.api.models.BookingId;
import com.automation.api.models.BookingResponse;
import com.automation.api.utilities.ApiContextConstants;
import com.automation.api.utilities.SelectionHelper;
import com.automation.context.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class BookingSteps {
    private final ScenarioContext scenarioContext;
    private final BookingClient bookingClient;
    private Response response;

    public BookingSteps(ScenarioContext scenarioContext, BookingClient bookingClient) {
        this.scenarioContext = scenarioContext;
        this.bookingClient = bookingClient;
    }

    @Given("I request all existing bookings")
    public void iRequestAllExistingBookings() {
        response = bookingClient.getAllBookings();

        Assert.assertEquals(response.statusCode(), 200, "Failed to retrieve existing bookings.");
    }

    @And("I select an existing booking id")
    public void iSelectAnExistingBookingId() {
        List<BookingId> bookings = response.jsonPath()
                .getList("$", BookingId.class);

        Assert.assertNotNull(bookings, "Bookings response should not be null.");
        Assert.assertFalse(bookings.isEmpty(), "Bookings list should not be empty.");

        int randomIndex = SelectionHelper.getRandomIndex(bookings.size());
        int selectedBookingId = bookings.get(randomIndex).getBookingid();
        scenarioContext.set(ApiContextConstants.SELECTED_BOOKING_ID, selectedBookingId);

        Assert.assertTrue(selectedBookingId > 0, "Selected booking id should be greater than zero.");
    }

    @Given("I request a booking with invalid id {string}")
    public void iRequestABookingWithInvalidId(String bookingId) {
        response = bookingClient.getBookingById(bookingId);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @Given("I create a new booking with valid details")
    public void iCreateANewBookingWithValidDetails() {
        Booking booking = new BookingBuilder().build();
        scenarioContext.set(ApiContextConstants.BOOKING, booking);

        response = bookingClient.createBooking(booking);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);

        BookingResponse bookingResponse = response.as(BookingResponse.class);
        scenarioContext.set(ApiContextConstants.CREATED_BOOKING_ID, bookingResponse.getBookingid());
    }

    @Given("I create a new booking without the {string} field")
    public void iCreateANewBookingWithoutTheField(String field) {
        Booking booking = new BookingBuilder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> bookingPayload = objectMapper.convertValue(
                booking,
                new TypeReference<Map<String, Object>>() {}
        );

        bookingPayload.remove(field);

        response = bookingClient.createBooking(bookingPayload);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @Given("I create a new booking with null value for the {string} field")
    public void iCreateANewBookingWithNullValueForTheField(String field) {
        Booking booking = new BookingBuilder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> bookingPayload = objectMapper.convertValue(
                booking,
                new TypeReference<Map<String, Object>>() {}
        );

        bookingPayload.put(field, null);

        response = bookingClient.createBooking(bookingPayload);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @Given("I create a new booking without the {string} booking date field")
    public void iCreateANewBookingWithoutTheBookingDateField(String field) {
        Booking booking = new BookingBuilder().build();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> bookingPayload = objectMapper.convertValue(
                booking,
                new TypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> bookingDates = objectMapper.convertValue(
                bookingPayload.get("bookingdates"),
                new TypeReference<Map<String, Object>>() {}
        );

        bookingDates.remove(field);
        bookingPayload.put("bookingdates", bookingDates);

        response = bookingClient.createBooking(bookingPayload);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @Given("I create a new booking with null value for the {string} booking date field")
    public void iCreateANewBookingWithNullValueForTheBookingDateField(String field) {
        Booking booking = new BookingBuilder().build();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> bookingPayload = objectMapper.convertValue(
                booking,
                new TypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> bookingDates = objectMapper.convertValue(
                bookingPayload.get("bookingdates"),
                new TypeReference<Map<String, Object>>() {}
        );

        bookingDates.put(field, null);
        bookingPayload.put("bookingdates", bookingDates);

        response = bookingClient.createBooking(bookingPayload);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @Given("I create a new booking without request body")
    public void iCreateANewBookingWithoutRequestBody() {
        response = bookingClient.createBookingWithoutBody();
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @When("I request the selected booking by id")
    public void iRequestTheSelectedBookingById() {
        int selectedBookingId = scenarioContext.get(ApiContextConstants.SELECTED_BOOKING_ID, Integer.class);
        response = bookingClient.getBookingById(selectedBookingId);

        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @And("the booking details should be returned")
    public void theBookingDetailsShouldBeReturned() {
        Booking booking = response.as(Booking.class);

        Assert.assertNotNull(booking, "Booking response should not be null.");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(booking.getFirstname(), "Firstname should not be null.");
        softAssert.assertNotNull(booking.getLastname(), "Lastname should not be null.");
        softAssert.assertNotNull(booking.getBookingdates(), "Booking dates should not be null.");

        if (booking.getBookingdates() != null) {
            softAssert.assertNotNull(booking.getBookingdates().getCheckin(),
                    "Check-in date should not be null.");
            softAssert.assertNotNull(booking.getBookingdates().getCheckout(),
                    "Check-out date should not be null.");
        }

        softAssert.assertAll();
    }

    @And("the created booking details should be returned")
    public void theCreatedBookingDetailsShouldBeReturned() {
        BookingResponse bookingResponse = response.as(BookingResponse.class);

        Assert.assertNotNull(bookingResponse, "Booking response should not be null.");
        Assert.assertTrue(bookingResponse.getBookingid() > 0,
                "Created booking id should be greater than zero.");
        Assert.assertNotNull(bookingResponse.getBooking(),
                "Created booking should not be null.");

        Booking expectedBooking = scenarioContext.get(ApiContextConstants.BOOKING, Booking.class);
        Booking actualBooking = bookingResponse.getBooking();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actualBooking.getFirstname(), expectedBooking.getFirstname(),
                "Firstname does not match.");
        softAssert.assertEquals(actualBooking.getLastname(), expectedBooking.getLastname(),
                "Lastname does not match.");
        softAssert.assertEquals(actualBooking.getTotalprice(), expectedBooking.getTotalprice(),
                "Total price does not match.");
        softAssert.assertEquals(actualBooking.isDepositpaid(), expectedBooking.isDepositpaid(),
                "Deposit paid value does not match.");
        softAssert.assertEquals(actualBooking.getBookingdates().getCheckin(),
                expectedBooking.getBookingdates().getCheckin(),
                "Check-in date does not match.");
        softAssert.assertEquals(actualBooking.getBookingdates().getCheckout(),
                expectedBooking.getBookingdates().getCheckout(),
                "Check-out date does not match.");
        softAssert.assertEquals(actualBooking.getAdditionalneeds(), expectedBooking.getAdditionalneeds(),
                "Additional needs do not match.");

        softAssert.assertAll();
    }
}
