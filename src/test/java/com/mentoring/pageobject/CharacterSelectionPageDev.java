package com.mentoring.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CharacterSelectionPageDev extends Page implements CharacterSelectionPage {

    private static final String PATH = "characterselect";

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    CharacterSelectionPageDev(WebDriver driver) {
        super(driver, PATH);
    }

    @Override
    public boolean isLogOutButtonVisible() {
        return isElementDisplayed(logoutButton);
    }

    @Override
    public void waitUntilPageLoads() {

    }
}
