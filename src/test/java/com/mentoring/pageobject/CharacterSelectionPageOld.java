package com.mentoring.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CharacterSelectionPageOld extends Page implements CharacterSelectionPage {

    private static final String PATH = "characterselect";

    @FindBy(css = "[onclick='authService.logout()']")
    private WebElement logoutButton;

    CharacterSelectionPageOld(WebDriver driver) {
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
