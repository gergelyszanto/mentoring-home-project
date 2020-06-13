package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FactoryPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    @FindBy(xpath = "//div[contains(text(),'Bővítő')]")
    private WebElement expansionItem;

    @FindBy(xpath = "//div[contains(@title,'CEX-01')]//button[contains(text(),'Gyártás indítása')]")
    private WebElement cex01ManufacturingButton;

    @FindBy(xpath = "//div[@class='queue-element-title']/span[contains(text(),'CEX-01 Csatlakozó bővítő')]")
    private WebElement cex01ManufacturingLineItem;

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

    public void clickExpansionItem() {
        waitUntilClickable(expansionItem).click();
    }

    public FactoryPage clickCex01ManufacturingButton() {
        waitUntilClickable(cex01ManufacturingButton).click();
        return this;
    }

    @Step("Asserting if expansion item under manufacturing is visible.")
    public boolean isExpansionItemUnderManufacturingVisible() {
        return isElementDisplayed(cex01ManufacturingLineItem);
    }

}