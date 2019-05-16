package com.mentoring.model;

import com.mentoring.framework.Config;;
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
    private static final String REGISTRATION_PATH_DEV = "/user";
    private static final String REGISTRATION_PATH_PROD = "/user/register";

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
        if (!Config.isLocalEnvironmentUsed()) {
            regData.put("confirmPassword", password);
        }

        RestAssured.baseURI = Config.getApplicationUrl();
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

    private static String getRegistrationPath() {
        return Config.isLocalEnvironmentUsed() ? REGISTRATION_PATH_DEV : REGISTRATION_PATH_PROD;
    }
}
