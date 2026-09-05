package com.automation.api.steps;

import com.automation.api.builders.BookingBuilder;
import com.automation.api.clients.BookingClient;
import com.automation.api.models.Booking;
import com.automation.api.utilities.ApiContextConstants;
import com.automation.context.ScenarioContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class BookingManagementSteps {
    private final ScenarioContext scenarioContext;
    private final BookingClient bookingClient;

    public BookingManagementSteps(ScenarioContext scenarioContext, BookingClient bookingClient) {
        this.scenarioContext = scenarioContext;
        this.bookingClient = bookingClient;
    }

    @When("I update the created booking with valid details")
    public void iUpdateTheCreatedBookingWithValidDetails() {
        int bookingId = scenarioContext.get(ApiContextConstants.CREATED_BOOKING_ID, Integer.class);
        String token = scenarioContext.get(ApiContextConstants.TOKEN, String.class);

        Booking updatedBooking = new BookingBuilder().build();
        scenarioContext.set(ApiContextConstants.BOOKING, updatedBooking);

        Response response = bookingClient.updateBooking(bookingId, updatedBooking, token);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @And("the updated booking details should be returned")
    public void theUpdatedBookingDetailsShouldBeReturned() {
        Booking expectedBooking = scenarioContext.get(ApiContextConstants.BOOKING, Booking.class);
        Response response = scenarioContext.get(ApiContextConstants.RESPONSE, Response.class);
        Booking actualBooking = response.getBody().as(Booking.class);

        validateBookingDetails(actualBooking, expectedBooking);
    }

    @And("I request the created booking by id")
    public void iRequestTheCreatedBookingById() {
        int bookingId = scenarioContext.get(ApiContextConstants.CREATED_BOOKING_ID, Integer.class);
        Response response = bookingClient.getBookingById(bookingId);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @And("the retrieved booking should contain the updated details")
    public void theRetrievedBookingShouldContainTheUpdatedDetails() {
        Booking expectedBooking = scenarioContext.get(ApiContextConstants.BOOKING, Booking.class);
        Response response = scenarioContext.get(ApiContextConstants.RESPONSE, Response.class);
        Booking actualBooking = response.getBody().as(Booking.class);

        validateBookingDetails(actualBooking, expectedBooking);
    }

    @When("I delete the created booking")
    public void iDeleteTheCreatedBooking() {
        int bookingId = scenarioContext.get(ApiContextConstants.CREATED_BOOKING_ID, Integer.class);
        String token = scenarioContext.get(ApiContextConstants.TOKEN, String.class);
        Response response = bookingClient.deleteBooking(bookingId, token);

        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @When("I update the created booking without authentication")
    public void iUpdateTheCreatedBookingWithoutAuthentication() {
        int bookingId = scenarioContext.get(ApiContextConstants.CREATED_BOOKING_ID, Integer.class);

        Booking updatedBooking = new BookingBuilder().build();
        scenarioContext.set(ApiContextConstants.BOOKING, updatedBooking);

        Response response = bookingClient.updateBookingWithoutAuthentication(bookingId, updatedBooking);
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @When("I delete the created booking without authentication")
    public void iDeleteTheCreatedBookingWithoutAuthentication() {
        int bookingId = scenarioContext.get(ApiContextConstants.CREATED_BOOKING_ID, Integer.class);
        Response response = bookingClient.deleteBookingWithoutAuthentication(bookingId);

        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    private void validateBookingDetails(Booking actualBooking, Booking expectedBooking) {
        Assert.assertNotNull(actualBooking, "Booking response should not be null.");

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

        softAssert.assertEquals(actualBooking.getAdditionalneeds(),
                expectedBooking.getAdditionalneeds(),
                "Additional needs do not match.");

        softAssert.assertAll();
    }
}
