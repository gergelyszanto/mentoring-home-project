package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import org.openqa.selenium.WebDriver;

public class EquipmentPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    EquipmentPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    EquipmentPage waitUntilPageLoads() {
        return this;
    }
}