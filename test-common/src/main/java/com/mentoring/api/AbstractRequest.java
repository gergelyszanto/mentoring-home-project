package com.mentoring.api;

import com.mentoring.allure.AllureAttachmentHandler;
import com.mentoring.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRequest {

    private static final String RESPONSE_BODY = "Response body";
    private static final String RESPONSE_STATUS_CODE = "Response status code";
    private static final String ERROR_CODE = "Error code";
    private static final String ERROR_RESPONSE = "Error response";

    public Response sendPostRequest(Cookies cookies, Object requestBody, String path, int expectedStatusCode) {
        RestAssured.baseURI = Config.getBaseUrl();
        AllureAttachmentHandler attachmentHandler = new AllureAttachmentHandler();

        RequestSpecification requestSpecification = RequestBuilder.builder()
            .cookies(cookies)
            .requestBody(requestBody)
            .build()
            .createRequest(attachmentHandler, Method.POST, path);

        Response response = requestSpecification
                .when()
                .post(path)
                .then()
                .extract()
                .response();

        return validateResponse(attachmentHandler, response, expectedStatusCode);
    }

    public Response sendPostRequest(String path, int expectedStatusCode) {
        return sendPostRequest(new Cookies(), null, path, expectedStatusCode);
    }

    public Response sendPostRequest(Cookies cookies, String path, int expectedStatusCode) {
        return sendPostRequest(cookies, null, path, expectedStatusCode);
    }

    public Response sendPostRequest(Object request, String path, int expectedStatusCode) {
        return sendPostRequest(null, request, path, expectedStatusCode);
    }

    public Response sendGetRequest(String path, Cookies cookies, int expectedStatusCode) {
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

    public Response sendDeleteRequest(String path, Cookies cookies, int expectedStatusCode) {
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
            log.error("Request failed. Response is:\n{}", response.prettyPrint());
            attachment.attachText(ERROR_CODE, String.valueOf(response.getStatusCode()));
            attachment.attachText(ERROR_RESPONSE, response.prettyPrint());
            throw e;
        }
        log.info("Got code: {}, Response:\n{}", response.getStatusCode(), response.getBody().prettyPrint());
        attachment.attachText(RESPONSE_STATUS_CODE, String.valueOf(response.getStatusCode()));
        if (!response.getBody().prettyPrint().isEmpty()) {
            attachment.attachJson(RESPONSE_BODY, response.getBody().prettyPrint());
        }
        return response;
    }
}
