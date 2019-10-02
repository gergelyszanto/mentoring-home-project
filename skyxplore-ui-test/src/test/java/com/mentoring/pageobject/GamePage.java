package com.mentoring.pageobject;

import com.mentoring.config.Config;
import org.openqa.selenium.WebDriver;

public class GamePage extends Page {

    //TODO: Add correct path once this part of the SUT is done
    private static final String PAGE_PATH = "/TBD";

    GamePage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    GamePage waitUntilPageLoads() {
        return this;
    }
}