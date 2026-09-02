package com.automation.api.clients;

import com.automation.api.utilities.ApiEndpoints;
import com.automation.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookingClient {
    private final String baseUrl;

    public BookingClient() {
        this.baseUrl = ConfigReader.getSettings().api.baseUrl;
    }

    public Response getAllBookings() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .when()
                .get(ApiEndpoints.BOOKING);
    }

    public Response getBookingById(int bookingId) {
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get(ApiEndpoints.BOOKING + "/" + bookingId);
    }
}
