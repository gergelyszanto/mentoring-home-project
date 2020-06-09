package com.mentoring.endpoints;

import com.mentoring.api.AbstractRequest;
import com.mentoring.model.requestbody.CreateUserRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserEndpoint extends AbstractRequest {

    private static final String USER_PATH = "/api/user";

    @Step("Sending user registration request. Expecting {expectedResponseCode} response code.")
    public Response sendRegistrationRequest(String email, String username, String password, int expectedResponseCode) {
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
        return sendRegistrationRequest(userRequest, expectedResponseCode);
    }

    public Response sendRegistrationRequest(CreateUserRequest requestBody, int expectedResponseCode) {
        return sendPostRequest(null, USER_PATH, requestBody, expectedResponseCode);
    }
}
