package com.automation.api.steps;

import com.automation.api.utilities.ApiContextConstants;
import com.automation.context.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class CommonApiSteps {
    private static final Logger logger = LoggerFactory.getLogger(CommonApiSteps.class);
    private final ScenarioContext scenarioContext;

    public CommonApiSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatusCode) {
        Response response = scenarioContext.get(ApiContextConstants.RESPONSE, Response.class);

        if (response.statusCode() != expectedStatusCode) {
            logger.error("Unexpected API response. Status: {}, Body: {}",
                    response.statusCode(),
                    response.getBody().asString());
        }

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
