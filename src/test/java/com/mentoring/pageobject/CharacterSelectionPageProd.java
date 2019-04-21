package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class CharacterSelectionPageProd extends Page implements CharacterSelectionPage {

    private static final String PATH = "characterselect";

    @FindBy(css = "[onclick='authService.logout()']")
    private WebElement logoutButton;

    CharacterSelectionPageProd(WebDriver driver) {
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
        log.info("Logging out...");
        waitUntilClickable(logoutButton).click();
        return new MainPageProd(driver);
    }
}
