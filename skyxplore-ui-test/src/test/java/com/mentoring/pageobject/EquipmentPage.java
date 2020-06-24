package com.mentoring.pageobject;

import com.mentoring.config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EquipmentPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    @FindBy(xpath = "(//div[contains(@title,'BAT-01')])[1]")
    private WebElement bat01BatteryConnectorItem;

    @FindBy(xpath = "//div[@class='slot equipment-list-element']//span")
    private WebElement cex01ExtenderConnectorItem;

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