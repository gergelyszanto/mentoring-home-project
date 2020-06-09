package com.mentoring.api;

import io.restassured.http.Cookies;

import io.restassured.response.Response;

public interface RequestInterface {
    Response sendPostRequest(Cookies cookies, Object requestBody, String path, int expectedStatusCode);
}
