package com.automation.api.hooks;

import com.automation.api.clients.AuthClient;
import com.automation.api.clients.BookingClient;
import com.automation.api.models.AuthResponse;
import com.automation.api.utilities.ApiContextConstants;
import com.automation.context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.response.Response;
import org.testng.Assert;

public class ApiHooks {
    private final ScenarioContext scenarioContext;
    private final AuthClient authClient;
    private final BookingClient bookingClient;

    public ApiHooks(ScenarioContext scenarioContext, AuthClient authClient, BookingClient bookingClient) {
        this.scenarioContext = scenarioContext;
        this.authClient = authClient;
        this.bookingClient = bookingClient;
    }

    @Before("@auth or @cleanupBooking")
    public void authenticate() {
        Response response = authClient.authenticateWithValidCredentials();

        Assert.assertEquals(response.statusCode(), 200, "Authentication failed.");

        AuthResponse authResponse = response.as(AuthResponse.class);

        Assert.assertNotNull(authResponse.getToken(), "Authentication token should not be null.");
        Assert.assertFalse(authResponse.getToken().isBlank(), "Authentication token should not be empty.");

        scenarioContext.set(ApiContextConstants.TOKEN, authResponse.getToken());
    }

    @After("@cleanupBooking")
    public void cleanupBooking() {
        Integer bookingId = scenarioContext.get(ApiContextConstants.CREATED_BOOKING_ID, Integer.class);

        if (bookingId == null) {
            return;
        }

        String token = scenarioContext.get(ApiContextConstants.TOKEN, String.class);

        if (token == null || token.isBlank()) {
            return;
        }

        Response response = bookingClient.deleteBooking(bookingId, token);

        System.out.println("Cleanup booking id: " + bookingId + ", status: " + response.statusCode());
    }
}
