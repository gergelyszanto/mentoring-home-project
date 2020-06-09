package com.mentoring.api;

import com.mentoring.allure.AllureAttachmentHandler;
import com.mentoring.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractRequest {

    private static final String RESPONSE_BODY = "Response body";
    private static final String RESPONSE_STATUS_CODE = "Response status code";
    private static final String ERROR_CODE = "Error code";
    private static final String ERROR_RESPONSE = "Error response";

    public Response sendPostRequest(Cookies cookies, String path, Object requestBody, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();

        RequestSpecification requestSpecification = RequestBuilder.builder()
            .cookies(cookies)
            .requestBody(requestBody)
            .build()
            .createRequest(attachmentHandler, Method.POST, path);

        Response response = requestSpecification
                .when()
                .post(path);

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendGetRequest(Cookies cookies, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();

        RequestSpecification requestSpecification = RequestBuilder.builder()
            .cookies(cookies)
            .build()
            .createRequest(attachmentHandler, Method.GET, path);

        Response response = requestSpecification
            .when()
            .get(path)
            .then()
            .extract()
            .response();
        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendDeleteRequest(Cookies cookies, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();

        RequestSpecification requestSpecification = RequestBuilder.builder()
            .cookies(cookies)
            .build()
            .createRequest(attachmentHandler, Method.DELETE, path);

        Response response = requestSpecification
            .when()
            .delete(path)
            .then()
            .extract()
            .response();
        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendPutRequest(Cookies cookies, Object requestBody, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();

        RequestSpecification requestSpecification = RequestBuilder.builder()
            .cookies(cookies)
            .requestBody(requestBody)
            .build()
            .createRequest(attachmentHandler, Method.PUT, path);

        Response response = requestSpecification
            .when()
            .put(path)
            .then()
            .extract()
            .response();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendPatchRequest(Cookies cookies, Object requestBody, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();

        RequestSpecification requestSpecification = RequestBuilder.builder()
            .cookies(cookies)
            .requestBody(requestBody)
            .build()
            .createRequest(attachmentHandler, Method.PATCH, path);

        Response response = requestSpecification
            .when()
            .patch(path)
            .then()
            .extract()
            .response();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    private Response validateResponse(AllureAttachmentHandler attachment, Response response, int expectedStatusCode) {
        try {
            response.then().statusCode(expectedStatusCode);
        } catch (AssertionError e) {
            log.error("Request failed. Response is:\n{}", response.asString());
            attachment.attachText(ERROR_CODE, String.valueOf(response.getStatusCode()));
            attachment.attachText(ERROR_RESPONSE, response.asString());
            throw e;
        }
        log.info("Response code: {}", response.getStatusCode());
        attachment.attachText(RESPONSE_STATUS_CODE, String.valueOf(response.getStatusCode()));
        if (!response.getBody().asString().isEmpty()) {
            log.info("Response body:\n{}", formatJson(response.getBody().asString()));
            attachment.attachJson(RESPONSE_BODY, formatJson(response.getBody().asString()));
        }
        return response;
    }

    public Cookies addCookie(Cookies authCookies, String cookieKey, String characterId) {
        if (authCookies != null) {
            Cookie characterCookie = new Cookie.Builder(cookieKey, characterId).build();
            List<Cookie> cookies = new ArrayList<>();
            for (int i = 0; i < authCookies.size(); i++) {
                cookies.add(authCookies.asList().get(i));
            }
            cookies.add(characterCookie);
            return new Cookies(cookies);
        } else {
            return new Cookies();
        }
    }

    private String formatJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            return json.toString(4);
        } catch (JSONException e) {
            try {
                JSONArray json = new JSONArray(jsonString);
                return json.toString(4);
            } catch (JSONException e2) {
                log.error("Failed to format string to JSON. Input string is not a JSON object. " +
                        "Returning unformatted string");
                return jsonString;
            }
        }
    }
}
