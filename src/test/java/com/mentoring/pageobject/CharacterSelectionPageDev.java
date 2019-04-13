package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
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

    @Override
    public MainPage logout() {
        log.info("Log out...");
        waitUntilClickable(logoutButton).click();
        return new MainPageDev(driver);
    }
}
