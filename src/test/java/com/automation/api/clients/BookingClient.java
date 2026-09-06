package com.automation.api.clients;

import com.automation.api.utilities.ApiEndpoints;
import com.automation.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

    public Response getBookingById(String bookingId) {
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get(ApiEndpoints.BOOKING + "/" + bookingId);
    }

    public Response createBooking(Object booking) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post(ApiEndpoints.BOOKING);
    }

    public Response createBookingWithoutBody() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .post(ApiEndpoints.BOOKING);
    }

    public Response updateBooking(int bookingId, Object booking, String token) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(booking)
                .when()
                .put(ApiEndpoints.BOOKING + "/" + bookingId);
    }

    public Response updateBookingWithoutAuthentication(int bookingId, Object booking) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .put(ApiEndpoints.BOOKING + "/" + bookingId);
    }

    public Response deleteBooking(int bookingId, String token) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .delete(ApiEndpoints.BOOKING + "/" + bookingId);
    }

    public Response deleteBookingWithoutAuthentication(int bookingId) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .when()
                .delete(ApiEndpoints.BOOKING + "/" + bookingId);
    }
}
