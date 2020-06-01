package com.mentoring.endpoints;

import com.mentoring.model.requestbody.LoginRequest;
import io.restassured.http.Cookies;
import io.restassured.response.Response;

public class Login extends AbstractRequest {

    private static final String LOGIN_PATH = "/api/login";

    public Response sendLoginRequest(String username, String password, int expectedResponseCode) {
        LoginRequest loginRequest = LoginRequest.builder()
                .password(password)
                .userName(username)
                .build();
        return sendPostRequest(loginRequest, LOGIN_PATH, expectedResponseCode);
    }

    //TODO: add session handling instead
    public Cookies getAccessCookies(String username, String password, int expectedResponseCode) {
        return sendLoginRequest(username, password, expectedResponseCode).getDetailedCookies();
    }
}
