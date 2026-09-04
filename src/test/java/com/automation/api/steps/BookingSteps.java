package com.automation.api.steps;

import com.automation.api.builders.BookingBuilder;
import com.automation.api.clients.BookingClient;
import com.automation.api.models.Booking;
import com.automation.api.models.BookingId;
import com.automation.api.models.BookingResponse;
import com.automation.api.utilities.ApiContextConstants;
import com.automation.api.utilities.SelectionHelper;
import com.automation.context.ScenarioContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.List;

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

    @Given("I create a new booking with valid details")
    public void iCreateANewBookingWithValidDetails() {
        Booking booking = new BookingBuilder().build();
        scenarioContext.set(ApiContextConstants.BOOKING, booking);

        response = bookingClient.createBooking(booking);
    }

    @When("I request the selected booking by id")
    public void iRequestTheSelectedBookingById() {
        int selectedBookingId = scenarioContext.get(ApiContextConstants.SELECTED_BOOKING_ID, Integer.class);
        response = bookingClient.getBookingById(selectedBookingId);
    }

    @Then("the booking response status should be {int}")
    public void theBookingResponseStatusShouldBe(int expectedStatusCode) {
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Unexpected booking response status code.");
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
