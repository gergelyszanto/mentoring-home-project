package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class EquipmentPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    private static final By shipEmptySlotsBy = By.xpath("//div[contains(text(),'Üres')]");

    // Ship section
    @FindBy(css = "#ship div[title *= 'BAT-01']")
    private WebElement shipBat01Item;

    @FindBy(css = "#ship div[title *= 'CEX-01']")
    private WebElement shipCex01Item;

    @FindBy(xpath = "(//div[contains(text(),'Üres')])[1]")
    private WebElement shipEmptySlot;

    // Storage section
    @FindBy(xpath = "(//div[@class='slot equipment-list-element'])[1]")
    private WebElement storageCex01Item;

    @FindBy(xpath = "//div[@id='equipment-list']//span[contains(text(),'BAT-01')]")
    private WebElement storageBat01Item;

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

    @Step("Removing BAT-01 item from Ship section by clicking it.")
    public void removeShipBat01Item() {
        log.info("Removing BAT-01 item from Ship section by clicking it");
        waitUntilClickable(shipBat01Item).click();
    }

    @Step("Moving CEX-01 item from Storage section to Ship empty slot by dragging and dropping it")
    public void moveStorageCex01ItemToShipEmptySlot() {
        dragAndDrop(waitUntilVisible(storageCex01Item), waitUntilVisible(shipEmptySlot));
    }

    @Step("Getting the number of extra free slots from CEX-01 item description")
    public int getCex01ItemExtraFreeSlots() throws AssertionError {
        String attr = waitUntilVisible(shipCex01Item).getAttribute("title");

        Pattern pattern = Pattern.compile("(?<=Extra hely: )[0-9]+");
        Matcher matcher = pattern.matcher(attr);

        String slot = "";
        if(matcher.find()) {
            slot = matcher.group(0);
        } else {
            throw new AssertionError(
                    ("Number of extra free slots description is not found:\n" + attr));
        }
        return Integer.parseInt(slot);
    }

    @Step("Getting the number of empty slots in Ship section")
    public int getShipEmptySlots() {
        return driver.findElements(shipEmptySlotsBy).size();
    }

    @Step("Checking if BAT-01 item is visible in Ship section")
    public boolean isShipBat01ItemVisible() {
        return isElementDisplayed(shipBat01Item);
    }

    @Step("Checking if CEX-01 item is visible in Ship section")
    public boolean isShipCex01ItemVisible() {
        return isElementDisplayed(shipCex01Item);
    }

    @Step("Checking if empty slot is visible in Ship section")
    public boolean isShipEmptySlotVisible() {
        return isElementDisplayed(shipEmptySlot);
    }

    @Step("Checking if BAT-01 item is visible in Storage section")
    public boolean isStorageBat01ItemVisible() {
        return isElementDisplayed(storageBat01Item);
    }

    @Step("Checking if CEX-01 item is visible in Storage section")
    public boolean isStorageCex01ItemVisible() {
        return isElementDisplayed(storageCex01Item);
    }
}
