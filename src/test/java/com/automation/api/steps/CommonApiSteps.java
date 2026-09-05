package com.automation.api.steps;

import com.automation.api.utilities.ApiContextConstants;
import com.automation.context.ScenarioContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

public class CommonApiSteps {
    private final ScenarioContext scenarioContext;
    private Response response;

    public CommonApiSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatusCode) {
        Response response = scenarioContext.get(ApiContextConstants.RESPONSE, Response.class);

        Assert.assertEquals(response.statusCode(), expectedStatusCode,
                "Unexpected response status code.");
    }

    @And("the response should contain the following message {string}")
    public void theResponseShouldContainTheFollowingMessage(String expectedMessage) {
        Response response = scenarioContext.get(ApiContextConstants.RESPONSE, Response.class);
        String actualResponse = response.getBody().asString();

        Assert.assertTrue(actualResponse.contains(expectedMessage),
                "Expected response to contain message '" + expectedMessage
                        + "', but actual response was: " + actualResponse
        );
    }
}
