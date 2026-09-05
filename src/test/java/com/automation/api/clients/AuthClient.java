package com.automation.api.clients;

import com.automation.api.models.AuthRequest;
import com.automation.api.utilities.ApiEndpoints;
import com.automation.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthClient {
    private final String baseUrl;

    public AuthClient() {
        this.baseUrl = ConfigReader.getSettings().api.baseUrl;
    }

    public Response authenticate(AuthRequest authRequest) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(authRequest)
                .when()
                .post(ApiEndpoints.AUTH);
    }

    public Response authenticateWithValidCredentials() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(ConfigReader.getSettings().api.username);
        authRequest.setPassword(ConfigReader.getSettings().api.password);

        return authenticate(authRequest);
    }
}
