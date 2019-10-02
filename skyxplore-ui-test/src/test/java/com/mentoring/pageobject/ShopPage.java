package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import org.openqa.selenium.WebDriver;

public class ShopPage extends Page {

    private static final String PAGE_PATH = "/shop";

    ShopPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    ShopPage waitUntilPageLoads() {
        return this;
    }
}