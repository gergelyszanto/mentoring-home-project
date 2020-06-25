package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class EquipmentPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    // Ship section
    @FindBy(css = "#ship div[title *= 'BAT-01']")
    private WebElement bat01ShipItem;

    @FindBy(css = "#ship div[title *= 'CEX-01']")
    private WebElement cex01ShipItem;

    @FindBy(xpath = "//div[@class='slot empty-slot']")
    private WebElement emptyShipSlot;

    // Storage section
    @FindBy(xpath = "//div[@class='slot equipment-list-element']//span[contains(text(),'CEX-01')]")
    private WebElement cex01StorageItem;

    @FindBy(xpath = "//div[@id='equipment-list']//span[contains(text(),'BAT-01')]")
    private WebElement bat01StorageItem;

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

    public void removeBat01ShipItem() {
        log.info("Removing BAT-01 item from Ship section by clicking it");
        waitUntilClickable(bat01ShipItem).click();
    }

    @Step("Checking if BAT-01 item is visible in Ship section")
    public boolean isBat01ShipItemVisible() {
        return isElementDisplayed(bat01ShipItem);
    }

    @Step("Checking if CEX-01 item is visible in Ship section")
    public boolean isCex01ShipItemVisible() {
        return isElementDisplayed(cex01ShipItem);
    }

    @Step("Checking if empty slot is visible in Ship section")
    public boolean isEmptyShipSlotVisible() {
        return isElementDisplayed(emptyShipSlot);
    }

    @Step("Checking if BAT-01 item is visible in Storage section")
    public boolean isBat01StorageItemVisible() {
        return isElementDisplayed(bat01StorageItem);
    }

    @Step("Checking if CEX-01 item is visible in Storage section")
    public boolean isCex01StorageItemVisible() {
        return isElementDisplayed(cex01StorageItem);
    }

    @Step("Moving CEX-01 from Storage section to Ship empty slot")
    public void moveCex01FromStorageToEmptyShipSlot() {
        Actions action = new Actions(driver);

        action.dragAndDrop(cex01StorageItem, emptyShipSlot)
                .build()
                .perform();
    }

}