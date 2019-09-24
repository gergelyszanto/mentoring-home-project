package com.mentoring.model;

import com.mentoring.framework.Config;
import com.mentoring.framework.utils.UserUtils;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Slf4j
public class User {

    private static final String PASSWORD = "Test1234!";
    private static final String REGISTRATION_PATH_DEV = "/api/user";
    private static final String REGISTRATION_PATH_PROD = "/user";
    private static final String LOGIN_PATH_DEV = "/api/login";
    private static final String LOGIN_PATH_PROD = "/login";
    private static final String CHARACTER_PATH_DEV = "/api/character";
    private static final String CHARACTER_PATH_PROD = "/character";
    private static final String LOGOUT_PATH_DEV = "/api/logout";
    private static final String LOGOUT_PATH_PROD = "/logout";

    @Getter
    private String userName;
    @Getter
    private String password;
    @Getter
    private String email;

    public User() {
        this.userName = UserUtils.generateRandomUsername();
        this.password = PASSWORD;
        this.email = UserUtils.generateRandomEmail();
        registerUser(userName, password, email);
    }

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        registerUser(userName, password, email);
    }

    @Step("Posting user registration...")
    private void registerUser(String username, String password, String email) {
        Map<String, String> regData = new HashMap<>();
        regData.put("username", username);
        regData.put("password", password);
        regData.put("email", email);

        RestAssured.baseURI = Config.getBaseUrl();
        given()
            .urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(regData)
            .when()
            .post(getRegistrationPath())
            .then()
            .statusCode(200);
        log.info("User registered:\n\tUsername: {}\n\tEmail: {}\n\tPassword: {}", username, email, password);
    }

    public void loginUser(String userName, String password) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("userName", userName);
        loginData.put("password", password);

        RestAssured.baseURI = Config.getBaseUrl();
        given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(getLoginPath())
                .then()
                .statusCode(200);
        log.info("User logged in:\n\tUsername: {}\n\tPassword: {}", userName, password);
    }

    public void createCharacter(String characterName, String accessToken, String userId) {
        Map<String, String> characterData = new HashMap<>();
        characterData.put("characterName", characterName);

        RestAssured.baseURI = Config.getBaseUrl();
        given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(characterData)
                .cookie("accesstokenid", accessToken)
                .cookie("userid", userId)
                .when()
                .post(getCharacterPath())
                .then()
                .statusCode(200);
        log.info("User character created:\n\tCharacter name: {}", characterName);
    }

    public void logoutUser(String userName, String accessToken, String userId) {
        RestAssured.baseURI = Config.getBaseUrl();
        given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .cookie("domain", accessToken)
                .cookie("userid", userId)
                .when()
                .post(getLogoutPath())
                .then()
                .statusCode(200);
        log.info("User logged out: {}", userName);

    }

    private static String getRegistrationPath() {
        return Config.isLocalEnvironmentUsed() ? REGISTRATION_PATH_DEV : REGISTRATION_PATH_PROD;
    }

    private static String getLoginPath() {
        return Config.isLocalEnvironmentUsed() ? LOGIN_PATH_DEV : LOGIN_PATH_PROD;
    }

    private static String getCharacterPath() {
        return Config.isLocalEnvironmentUsed() ? CHARACTER_PATH_DEV : CHARACTER_PATH_PROD;
    }

    private static String getLogoutPath() {
        return Config.isLocalEnvironmentUsed() ? LOGOUT_PATH_DEV : LOGOUT_PATH_PROD;
    }
}
