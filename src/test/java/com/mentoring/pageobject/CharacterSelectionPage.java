package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
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

    public MainPage logout() {
        log.info("Log out...");
        waitUntilClickable(logoutButton).click();
        return new MainPage(driver);
    }
}
