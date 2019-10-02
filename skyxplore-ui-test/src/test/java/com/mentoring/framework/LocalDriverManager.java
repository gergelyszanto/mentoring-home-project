package com.mentoring.framework;

import org.openqa.selenium.WebDriver;

class LocalDriverManager {

    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    /**
     * Returns the {@link WebDriver} instance previously added to the {@link ThreadLocal}
     *
     * @return {@link WebDriver}
     */
    static WebDriver getDriver() {
        return webDriver.get();
    }

    /**
     * Set a {@link WebDriver} to a {@link ThreadLocal} object
     *
     * @param driver the {@link WebDriver} instance to be added to {@link ThreadLocal}
     */
    static void setWebDriver(WebDriver driver) {
        webDriver.set(driver);
    }

}