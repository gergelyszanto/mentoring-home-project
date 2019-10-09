package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HangarPage extends Page {

    private static final String PAGE_PATH = "/hangar";

    @FindBy(id = "select-game-mode")
    private WebElement gameModeTitle;

    @FindBy(id = "mode-arcade-title")
    private WebElement arcadeMode;

    HangarPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    @Override
    HangarPage waitUntilPageLoads() {
        waitUntilVisible(gameModeTitle);
        return this;
    }

    @Step("Selecting arcade mode.")
    public LobbyPage selectArcadeMode() {
        waitUntilClickable(arcadeMode).click();
        return new LobbyPage(driver).waitUntilPageLoads();
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }
}