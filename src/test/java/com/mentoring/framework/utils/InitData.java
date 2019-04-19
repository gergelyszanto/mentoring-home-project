package com.mentoring.framework.utils;

import com.mentoring.framework.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Slf4j
public final class InitData {

    private InitData() {
    }

    public static void registerUser(String username, String password, String email) {
        Map<String,String> regData = new HashMap<>();
        regData.put("username", username);
        regData.put("password", password);
        regData.put("email", email);

        RestAssured.baseURI = Config.getApplicationUrl();
        given()
            .urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(regData)
            .when()
            .post("/user")
            .then()
            .statusCode(200);
    }
}
