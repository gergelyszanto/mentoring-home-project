package com.mentoring.generator;

import com.mentoring.api.AbstractRequest;
import com.mentoring.utilities.UserUtils;
import io.qameta.allure.Step;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class User extends AbstractRequest {

    private static final String PASSWORD = "Test1234!";
    private static final String REGISTRATION_PATH_DEV = "/api/user";
    private static final String LOGIN_PATH_DEV = "/api/login";
    private static final String CHARACTER_PATH_DEV = "/api/character";
    private static final String LOGOUT_PATH_DEV = "/api/logout";
    private static final String FRIEND_REQUEST = "/api/friend/request";

    @Getter
    private String userName;

    @Getter
    private String password;

    @Getter
    private String email;

    @Getter
    private String userId;

    @Getter
    private String accessToken;

    public User() {
        this.userName = UserUtils.generateRandomUsername();
        this.password = PASSWORD;
        this.email = UserUtils.generateRandomEmail();
        registerUser(userName, password, email);
        Response response = loginUser(userName, password);
        this.userId = response.getDetailedCookie("userid").getValue();
        this.accessToken = response.getDetailedCookie("accesstokenid").getValue();
    }

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.userId = registerUser(userName, password, email).getDetailedCookie("userid").toString();
    }

    @Step("Posting user registration...")
    private Response registerUser(String username, String password, String email) {
        Map<String, String> regData = new HashMap<>();
        regData.put("username", username);
        regData.put("password", password);
        regData.put("email", email);

        return sendPostRequest(null, REGISTRATION_PATH_DEV, regData, 200);
    }

    @Step("Precondition step: Logging in with user: {userName} and password: {password})")
    public Response loginUser(String userName, String password) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("userName", userName);
        loginData.put("password", password);

        return sendPostRequest(null, LOGIN_PATH_DEV, loginData, 200);
    }

    @Step("Creating character: {characterName}")
    public void createCharacter(String characterName, String accessToken, String userId) {
        Cookie accessTokenCookie = new Cookie.Builder("accesstokenid", accessToken).setSecured(true).build();
        Cookie userIdToken = new Cookie.Builder("userid", userId).setSecured(true).build();
        Map<String, String> characterData = new HashMap<>();
        characterData.put("characterName", characterName);
        Cookies cookies = new Cookies(accessTokenCookie, userIdToken);

        sendPostRequest(cookies, CHARACTER_PATH_DEV, characterData, 200);
    }

    @Step("Send friend request from: {characterIdFrom} to: {characterIdTo}")
    public void sendFriendRequest(String characterIdFrom, String accessToken, String userId, String characterIdTo) {
        Map<String, String> character = new HashMap<>();
        character.put("value", characterIdTo);
        Cookie accessTokenCookie = new Cookie.Builder("accesstokenid", accessToken).setSecured(true).build();
        Cookie userIdToken = new Cookie.Builder("userid", userId).setSecured(true).build();
        Cookie characterId = new Cookie.Builder("characterid", characterIdFrom).setSecured(true).build();
        Cookies cookies = new Cookies(accessTokenCookie, userIdToken, characterId);
        sendPutRequest(cookies, character, FRIEND_REQUEST, 200);
    }


    private static String getRegistrationPath() {
        return REGISTRATION_PATH_DEV;
    }

    private static String getLoginPath() {
        return LOGIN_PATH_DEV;
    }

    private static String getCharacterPath() {
        return CHARACTER_PATH_DEV;
    }

    private static String getLogoutPath() {
        return LOGOUT_PATH_DEV;
    }

    private static String getFriendRequest() {
        return FRIEND_REQUEST;
    }
}
