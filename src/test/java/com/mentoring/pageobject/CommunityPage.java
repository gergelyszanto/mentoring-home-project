package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import org.openqa.selenium.WebDriver;

public class CommunityPage extends Page {

    private static final String PAGE_PATH = "/community";

    CommunityPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    CommunityPage waitUntilPageLoads() {
        return this;
    }
}