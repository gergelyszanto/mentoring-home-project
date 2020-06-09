package com.mentoring.endpoints;

import com.mentoring.api.AbstractRequest;
import com.mentoring.model.requestbody.LoginRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class LoginEndpoint extends AbstractRequest {

    private static final String LOGIN_PATH = "/api/login";

    @Step("Sending login request. Expecting {expectedResponseCode} response code.")
    public Response sendLoginRequest(String username, String password, int expectedResponseCode) {
        LoginRequest loginRequest = LoginRequest.builder()
                .password(password)
                .userName(username)
                .build();
        return sendPostRequest(null, LOGIN_PATH, loginRequest, expectedResponseCode);
    }
}
