package com.mentoring.endpoints;

import io.restassured.http.Cookies;
import io.restassured.response.Response;

public class Character extends AbstractRequest {

    private static final String CHARACTER_PATH = "/api/character";

    public Response getCharacters(Cookies cookies, int expectedResponseCode) {
        return sendGetRequest(CHARACTER_PATH, cookies, expectedResponseCode);
    }
}
