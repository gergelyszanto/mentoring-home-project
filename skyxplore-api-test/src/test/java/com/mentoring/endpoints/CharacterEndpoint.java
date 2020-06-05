package com.mentoring.endpoints;

import com.mentoring.api.AbstractRequest;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import io.restassured.response.Response;

public class CharacterEndpoint extends AbstractRequest {

    private static final String CHARACTER_PATH = "/api/character";

    @Step("Getting user's character list. Expecting {expectedResponseCode} response code.")
    public Response getCharacters(Cookies cookies, int expectedResponseCode) {
        return sendGetRequest(CHARACTER_PATH, cookies, expectedResponseCode);
    }
}
