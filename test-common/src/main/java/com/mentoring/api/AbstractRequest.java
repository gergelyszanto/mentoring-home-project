package com.mentoring.api;

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

    public Response sendPostRequest(Cookies cookies, Object request, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        log.info("Sending POST request to \"{}\" endpoint.", path);
        log.info("Request body:\n{}", convertPoJoToJson(request));

        Response response = given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(request)
                .cookies(cookies)
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

    public Response sendPostRequest(String path, int expectedStatusCode) {
        return sendPostRequest(new Cookies(), "{}", path, expectedStatusCode);
    }

    public Response sendPostRequest(Cookies cookies, String path, int expectedStatusCode) {
        return sendPostRequest(cookies, "{}", path, expectedStatusCode);
    }

    public Response sendPostRequest(Object request, String path, int expectedStatusCode) {
        return sendPostRequest(new Cookies(), request, path, expectedStatusCode);
    }

    public Response sendGetRequest(String path, Cookies cookies, int expectedStatusCode) {
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

    public Response sendDeleteRequest(String path, Cookies cookies, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        log.info("Sending DELETE request to \"{}\" endpoint.", path);
        return given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .delete(path)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response sendPutRequest(Object request, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        log.info("Sending PUT request to \"{}\" endpoint.", path);
        log.info("Request body:\n{}", convertPoJoToJson(request));

        Response response = given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put(path)
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

    public Response sendPatchRequest(Object request, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        log.info("Sending PATCH request to \"{}\" endpoint.", path);
        log.info("Request body:\n{}", convertPoJoToJson(request));

        Response response = given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(path)
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
