package com.mentoring.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

@Slf4j
public final class CookieUtils {

    private static final String VALIDATION_AND_ANIMATION_DELAY = "0";
    private static final String SEARCH_RESULT_DELAY = "search-result-timeout";
    private static final String VALIDATION_DELAY = "validation-timeout";

    private CookieUtils() {
    }

    public static void setTestCookies(WebDriver driver) {
        setCookie(driver, SEARCH_RESULT_DELAY, VALIDATION_AND_ANIMATION_DELAY);
        setCookie(driver, VALIDATION_DELAY, VALIDATION_AND_ANIMATION_DELAY);
    }

    private static void setCookie(WebDriver driver, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
        log.info("Cookie set with name: '{}', and value: '{}'", name, value);
    }
}
