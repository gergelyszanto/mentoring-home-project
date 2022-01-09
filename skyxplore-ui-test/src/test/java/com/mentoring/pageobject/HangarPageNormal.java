package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class HangarPageNormal extends Page implements HangarPage {

    private static final String PAGE_PATH = "/hangar";

    @FindBy(id = "select-game-mode")
    private WebElement gameModeTitle;

    @FindBy(id = "mode-arcade-title")
    private WebElement arcadeMode;

    HangarPageNormal(WebDriver driver) {
        super(driver, PAGE_PATH);
        log.info("Normal version of Hangar page has been loaded.");
    }

    @Override
    public HangarPageNormal waitUntilPageLoads() {
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