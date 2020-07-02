package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OverviewPage extends Page {

    @FindBy(id = "equipment-button")
    private WebElement equipmentButton;

    @FindBy(id = "hangar-button")
    private WebElement hangarButton;

    @FindBy(id = "ship-table-header")
    private WebElement shipTableHeader;

    @FindBy(id = "community-button")
    private WebElement communityButton;

    @FindBy(id = "factory-button")
    private WebElement factoryButton;

    private static final String PAGE_PATH = "/overview";

    OverviewPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    @Step("Opening equipment page.")
    public EquipmentPage openEquipmentPage() {
        waitUntilClickable(equipmentButton).click();
        return new EquipmentPage(driver).waitUntilPageLoads();
    }

    @Step("Opening hangar page.")
    public HangarPage openHangarPage() {
        waitUntilClickable(hangarButton).click();
        return new HangarPage(driver).waitUntilPageLoads();
    }

    @Step("Opening community page.")
    public CommunityPage openCommunityPage() {
        waitUntilClickable(communityButton).click();
        return new CommunityPage(driver).waitUntilPageLoads();
    }

    @Step("Opening factory page.")
    public FactoryPage openFactoryPage() {
        waitUntilClickable(factoryButton).click();
        return new FactoryPage(driver).waitUntilPageLoads();
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
