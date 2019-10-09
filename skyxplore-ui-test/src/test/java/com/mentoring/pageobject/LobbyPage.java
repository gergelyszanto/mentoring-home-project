package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LobbyPage extends Page {

    private static final String PAGE_PATH = "/lobby-page";

    @FindBy(id = "members-container")
    private WebElement membersContainer;

    @FindBy(id = "start-queueing")
    private WebElement startMatchmakingButton;

    @FindBy(css = "[id*='ready']")
    private WebElement readyButton;

    LobbyPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Step("Clicking the player ready button.")
    public LobbyPage clickReadyButton() {
        waitUntilClickable(readyButton).click();
        return this;
    }

    @Step("Opening the matchmaking page.")
    public LobbyQueuePage openMatchmakingPage() {
        waitUntilClickable(startMatchmakingButton).click();
        return new LobbyQueuePage(driver).waitUntilPageLoads();
    }

    @Override
    LobbyPage waitUntilPageLoads() {
        waitUntilVisible(membersContainer);
        return this;
    }
}