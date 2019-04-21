package com.mentoring.framework.utils;

import com.mentoring.framework.Config;;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Slf4j
public final class InitData {

    private static final String REGISTRATION_PATH_DEV = "/user";
    private static final String REGISTRATION_PATH_PROD = "/user/register";

    private InitData() {
    }

    @Step("Posting user registration...")
    public static void registerUser(String username, String password, String email) {
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
