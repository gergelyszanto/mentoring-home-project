package com.mentoring.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mentoring.allure.AllureAttachmentHandler;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;


@Slf4j
public class RequestBuilder {

    private final AllureAttachmentHandler attachmentHandler;
    private final Method method;
    private final String path;
    private Cookies cookies;
    private Object requestBody;

    public RequestBuilder(AllureAttachmentHandler attachmentHandler, Method method, String path) {
        this.attachmentHandler = attachmentHandler;
        this.method = method;
        this.path = path;
    }

    public RequestBuilder cookies(Cookies cookies) {
        this.cookies = cookies;
        return this;
    }

    public RequestBuilder requestBody(Object requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    Response build() {
        log.info("Sending {} request to \"{}\" endpoint.", method.name(), path);

        RequestSpecification request = given()
                .urlEncodingEnabled(true)
                .contentType(ContentType.JSON);
        attachmentHandler.attachJson(method.name().concat(" Request (").concat(path).concat(")"),
                convertPoJoToJson(requestBody));
        if (requestBody != null) {
            request.body(requestBody);
            log.info("Request body:\n{}", convertPoJoToJson(requestBody));
        } else {
            log.info("Request body in null.");
        }
        if (cookies != null) {
            request.cookies(cookies);
            attachmentHandler.attachText("Request cookies", cookies.toString());
        }
        attachmentHandler.attachText("Request header", request.get().getHeaders().toString());
        return request.when().request(method, path);
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
