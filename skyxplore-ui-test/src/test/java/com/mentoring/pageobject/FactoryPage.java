package com.mentoring.pageobject;

import com.mentoring.config.Config;
import org.openqa.selenium.WebDriver;

public class FactoryPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    FactoryPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    FactoryPage waitUntilPageLoads() {
        return this;
    }
}