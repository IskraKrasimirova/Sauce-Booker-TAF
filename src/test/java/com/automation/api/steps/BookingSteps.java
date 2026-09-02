package com.automation.api.steps;

import com.automation.api.clients.BookingClient;
import com.automation.api.models.Booking;
import com.automation.api.models.BookingId;
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
    private final BookingClient bookingClient;
    private Response response;
    private int selectedBookingId;

    public BookingSteps(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @Given("I request all existing bookings")
    public void iRequestAllExistingBookings() {
        response = bookingClient.getAllBookings();

        Assert.assertEquals(
                response.statusCode(),
                200,
                "Failed to retrieve existing bookings."
        );
    }

    @And("I select an existing booking id")
    public void iSelectAnExistingBookingId() {
        List<BookingId> bookings = response.jsonPath()
                .getList("$", BookingId.class);

        Assert.assertNotNull(bookings, "Bookings response should not be null.");
        Assert.assertFalse(bookings.isEmpty(), "Bookings list should not be empty.");

        selectedBookingId = bookings.get(0).getBookingid();

        Assert.assertTrue(selectedBookingId > 0, "Selected booking id should be greater than zero.");
    }

    @When("I request the selected booking by id")
    public void iRequestTheSelectedBookingById() {
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
}
