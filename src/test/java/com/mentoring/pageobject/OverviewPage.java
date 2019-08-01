package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OverviewPage extends Page {

    @FindBy(id = "hangar-button")
    private WebElement hangarButton;

    @FindBy(id = "ship-table-header")
    private WebElement shipTableHeader;

    private static final String PAGE_PATH = "/overview";

    OverviewPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    @Step("Opening hangar page.")
    public HangarPage openHangarPage() {
        waitUntilClickable(hangarButton).click();
        return new HangarPage(driver).waitUntilPageLoads();
    }

    @Override
    OverviewPage waitUntilPageLoads() {
        waitUntilVisible(shipTableHeader);
        return this;
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }
}
