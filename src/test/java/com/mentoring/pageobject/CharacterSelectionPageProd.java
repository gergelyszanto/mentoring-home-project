package com.mentoring.pageobject;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class CharacterSelectionPageProd extends Page implements CharacterSelectionPage {

    private static final String PATH = "characterselect";

    private static final String LOGGING_OUT = "Logging out.";

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
    @Step(LOGGING_OUT)
    public MainPage logout() {
        log.info(LOGGING_OUT);
        waitUntilClickable(logoutButton).click();
        return new MainPageProd(driver);
    }
}
