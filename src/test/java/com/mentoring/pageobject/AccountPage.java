package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import org.openqa.selenium.WebDriver;

public class AccountPage extends Page {

    private static final String PAGE_PATH = "/account";

    AccountPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    AccountPage waitUntilPageLoads() {
        return this;
    }
}