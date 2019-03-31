package com.mentoring.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CharacterSelectionPage extends Page {

    private static final String PATH = "characterselect";

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    CharacterSelectionPage(WebDriver driver) {
        super(driver, PATH);
    }

    public boolean isLogOutButtonVisible() {
        return isElementDisplayed(logoutButton);
    }

    @Override
    public void waitUntilPageLoads() {

    }
}
