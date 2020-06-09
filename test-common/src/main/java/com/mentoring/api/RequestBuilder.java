package com.mentoring.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mentoring.allure.AllureAttachmentHandler;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

@Slf4j
@Builder
public class RequestBuilder {

    private Cookies cookies;
    private Object requestBody;

    RequestSpecification createRequest(AllureAttachmentHandler attachmentHandler, Method method, String path) {
        log.info("Sending {} request to \"{}\" endpoint.",method.name() , path);
        attachmentHandler.attachText(method.name() + " request", method.name() + " " + path);


        RequestSpecification request = given()
            .urlEncodingEnabled(true)
            .contentType(ContentType.JSON);
        if (requestBody != null) {
            request.body(requestBody);
            log.info("Request body:\n{}", convertPoJoToJson(requestBody));
            attachmentHandler.attachJson("Request body", convertPoJoToJson(requestBody));
        } else {
            log.info("Request body in null.");
        }
        if (cookies != null) {
            request.cookies(cookies);
            attachmentHandler.attachText("Request cookies", cookies.toString());
        }
        attachmentHandler.attachText("Request header", request.get().getHeaders().toString());

        return request;
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
