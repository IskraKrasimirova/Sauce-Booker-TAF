package com.automation.api.steps;

import com.automation.api.clients.AuthClient;
import com.automation.api.models.AuthRequest;
import com.automation.api.models.AuthResponse;
import com.automation.api.utilities.ApiContextConstants;
import com.automation.config.ConfigReader;
import com.automation.context.ScenarioContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

public class AuthSteps {
    private final ScenarioContext scenarioContext;
    private final AuthClient authClient;

    public AuthSteps(ScenarioContext scenarioContext, AuthClient authClient) {
        this.scenarioContext = scenarioContext;
        this.authClient = authClient;
    }

    @Given("I authenticate with valid API credentials")
    public void iAuthenticateWithValidAPICredentials() {
        Response response = authClient.authenticateWithValidCredentials();
        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }

    @And("an authentication token should be returned")
    public void anAuthenticationTokenShouldBeReturned() {
        Response response = scenarioContext.get(ApiContextConstants.RESPONSE, Response.class);
        AuthResponse authResponse = response.as(AuthResponse.class);

        Assert.assertNotNull(authResponse.getToken(), "Authentication token should not be null.");
        Assert.assertFalse(authResponse.getToken().isBlank(), "Authentication token should not be empty.");

        scenarioContext.set(ApiContextConstants.TOKEN, authResponse.getToken());
    }

    @Given("I authenticate with invalid API credentials")
    public void iAuthenticateWithInvalidAPICredentials() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("invalidUsername");
        authRequest.setPassword("invalidPassword");

        Response response = authClient.authenticate(authRequest);

        scenarioContext.set(ApiContextConstants.RESPONSE, response);
    }
}
