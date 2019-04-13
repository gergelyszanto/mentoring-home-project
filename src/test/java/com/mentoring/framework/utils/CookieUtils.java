package com.mentoring.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

@Slf4j
public final class CookieUtils {

    private static final String VALIDATION_AND_ANIMATION_DELAY = "0";

    private CookieUtils() {
    }

    public static void setTestCookies(WebDriver driver) {
        setCookie(driver, "search-result-timeout", VALIDATION_AND_ANIMATION_DELAY);
        setCookie(driver, "validation-timeout", VALIDATION_AND_ANIMATION_DELAY);
    }

    private static void setCookie(WebDriver driver, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
        log.info("Cookie set with name: '{}', and value: '{}'", name, value);
    }
}
