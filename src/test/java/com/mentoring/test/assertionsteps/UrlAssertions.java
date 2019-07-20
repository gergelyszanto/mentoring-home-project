package com.mentoring.test.assertionsteps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public final class UrlAssertions {

    private UrlAssertions() {}

    @Step("Validating current URL: {actualUrl} matches expected URL: {expectedUrl}")
    public static void assertCurrentUrlMatchExpectedUrl(String actualUrl, String expectedUrl) {
        assertThat(actualUrl)
                .as("Current URL should match the expected.")
                .isEqualTo(expectedUrl);
    }
}
