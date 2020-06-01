package com.mentoring.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mentoring.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

@Slf4j
public abstract class AbstractRequest {

    Response sendPostRequest(Object request, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        log.info("Sending POST request to \"{}\" endpoint.", path);
        log.info("Request body:\n{}", convertPoJoToJson(request));

        Response response = given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
        try {
            response.then().statusCode(expectedStatusCode);
        } catch (AssertionError e) {
            log.error("Request failed. Response is:\n{}", response.prettyPrint());
            throw e;
        }
        log.info("Got code: {}, Response:\n{}", response.getStatusCode(), response.getBody().prettyPrint());
        return response;
    }

    Response sendGetRequest(String path, Cookies cookies, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        log.info("Sending GET request to \"{}\" endpoint.", path);
        return given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .get(path)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    private String convertPoJoToJson(Object jsonObject) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            log.error("Failed to process. Object is not JSON.");
        }
        return "";
    }
}
