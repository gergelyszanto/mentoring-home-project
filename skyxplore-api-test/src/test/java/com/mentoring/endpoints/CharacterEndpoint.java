package com.mentoring.endpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mentoring.api.AbstractRequest;
import com.mentoring.model.requestbody.CharacterRequest;
import com.mentoring.model.response.CharacterResponse;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class CharacterEndpoint extends AbstractRequest {

    private static final String CHARACTER_PATH = "/api/character";

    @Getter
    private CharacterResponse characterResponse;

    @Getter
    private List<CharacterResponse> allCharactersResponse;

    @Step("Getting user's character list. Expecting {expectedResponseCode} response code.")
    public Response getAllCharacters(Cookies cookies, int expectedResponseCode) {
        Response response =  sendGetRequest(cookies, CHARACTER_PATH, expectedResponseCode);
        setAllCharactersResponse(response);
        return response;
    }

    @Step("Sending create character request. Expecting {expectedResponseCode} response code.")
    public Response createCharacter(String characterName, Cookies cookies, int expectedResponseCode) {
        CharacterRequest request = CharacterRequest.builder().characterName(characterName).build();
        Response response =  sendPostRequest(cookies, CHARACTER_PATH, request, expectedResponseCode);
        setCharacterResponse(response);
        return response;
    }

    private void setCharacterResponse(Response response) {
        if (response.getBody() != null) {
            this.characterResponse = new Gson().fromJson(response.getBody().asString(), CharacterResponse.class);
        }
    }

    private void setAllCharactersResponse(Response response) {
        if (response.getBody() != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<CharacterResponse>>(){}.getType();
            this.allCharactersResponse = gson.fromJson(response.getBody().asString(), collectionType);
        }
    }
}
