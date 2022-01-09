package com.mentoring.api;

import com.mentoring.allure.AllureAttachmentHandler;
import com.mentoring.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractRequest {

    private static final String RESPONSE_BODY = "Response";
    private static final String ERROR_RESPONSE = "Error response";

    public Response sendPostRequest(Cookies cookies, String path, Object requestBody, int expectedStatusCode) {
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();
        RestAssured.baseURI = Config.getBaseUrl();

        Response response = new RequestBuilder(attachmentHandler, Method.POST, path)
            .cookies(cookies)
            .requestBody(requestBody)
            .build();

            return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendPostRequest(String path, Object requestBody, int expectedStatusCode) {
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();
        RestAssured.baseURI = Config.getBaseUrl();

        Response response = new RequestBuilder(attachmentHandler, Method.POST, path)
                .requestBody(requestBody)
                .build();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendGetRequest(Cookies cookies, String path, int expectedStatusCode) {
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();
        RestAssured.baseURI = Config.getBaseUrl();

        Response response = new RequestBuilder(attachmentHandler, Method.GET, path)
            .cookies(cookies)
            .build();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendDeleteRequest(Cookies cookies, String path, int expectedStatusCode) {
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();
        RestAssured.baseURI = Config.getBaseUrl();

        Response response = new RequestBuilder(attachmentHandler, Method.DELETE, path)
            .cookies(cookies)
            .build();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendPutRequest(Cookies cookies, String path, Object requestBody, int expectedStatusCode) {
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();
        RestAssured.baseURI = Config.getBaseUrl();

        Response response = new RequestBuilder(attachmentHandler, Method.PUT, path)
            .cookies(cookies)
            .requestBody(requestBody)
            .build();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendPatchRequest(Cookies cookies, String path, Object requestBody, int expectedStatusCode) {
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();
        RestAssured.baseURI = Config.getBaseUrl();

        Response response = new RequestBuilder(attachmentHandler, Method.PATCH, path)
            .cookies(cookies)
            .requestBody(requestBody)
            .build();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    private Response validateResponse(AllureAttachmentHandler attachment, Response response, int expectedStatusCode) {
        try {
            response.then().statusCode(expectedStatusCode);
        } catch (AssertionError e) {
            log.error("Request failed. Response is:\n{}", response.asString());
            attachment.attachText(ERROR_RESPONSE, response.asString());
            throw e;
        }
        log.info("Response body:\n{}", formatJson(response.getBody().asString()));
        log.info("Response code: {}", response.getStatusCode());
        attachment.attachJson(RESPONSE_BODY.concat("\t").concat(String.valueOf(response.getStatusCode())),
            formatJson(response.getBody().asString()));
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
        if (jsonString != null && !jsonString.isEmpty()) {
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
        } else {
            return "";
        }
    }
}
