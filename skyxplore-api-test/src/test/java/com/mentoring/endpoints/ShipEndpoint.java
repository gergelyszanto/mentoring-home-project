package com.mentoring.endpoints;

import com.google.gson.Gson;
import com.mentoring.api.AbstractRequest;
import com.mentoring.model.response.ShipResponse;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import lombok.Getter;

public class ShipEndpoint extends AbstractRequest {

    private static final String SHIP_PATH = "/api/ship";

    @Getter
    private ShipResponse responseContent;

    @Step("Getting character's ship details. Expecting {expectedResponseCode} response code.")
    public Response getShip(Cookies cookies, String characterId, int expectedResponseCode) {
        Cookies updatedCookies = addCookie(cookies, "characterid", characterId);
        Response response = sendGetRequest(updatedCookies, SHIP_PATH, expectedResponseCode);
        setResponseContent(response);
        return response;
    }

    private void setResponseContent(Response response) {
        if (response.getBody() != null) {
            this.responseContent = new Gson().fromJson(response.getBody().asString(), ShipResponse.class);
        }
    }
}
