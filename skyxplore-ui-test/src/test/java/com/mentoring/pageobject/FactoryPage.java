package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

@Slf4j
public class FactoryPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    @FindBy(id = "overview-button")
    private WebElement overviewButton;

    @FindBy(xpath = "//div[contains(text(),'Bővítő')]")
    private WebElement connectorExtenderButton;

    @FindBy(xpath = "//div[contains(@title,'CEX-01')]//button")
    private WebElement cex01ProductionButton;

    @FindBy(xpath = "//div[@class='queue-element-title']/span[contains(text(),'CEX-01 Csatlakozó bővítő')]")
    private WebElement cex01ProductionQueueItem;

    @FindBy(css = ".queue-element")
    private WebElement queueElement;

    @FindBy(xpath = "(//div[@id='queue']//span)[3]")
    private WebElement quantityInQueue;

    @FindBy(css = ".queue-process .process-bar-text")
    private WebElement queueProcessBar;

    FactoryPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    FactoryPage waitUntilPageLoads() {
        return this;
    }

    @Step("Opening overview page")
    public OverviewPage openOverviewPage() {
        waitUntilClickable(overviewButton).click();
        return new OverviewPage(driver).waitUntilPageLoads();
    }

    public FactoryPage selectConnectorExtender() {
        waitUntilClickable(connectorExtenderButton).click();
        return this;
    }

    public FactoryPage clickCex01ProductionButton() {
        waitUntilClickable(cex01ProductionButton).click();
        return this;
    }

    @Step("Checking if extender item in queue is visible.")
    public boolean isExtenderItemInQueueVisible() {
        return isElementDisplayed(cex01ProductionQueueItem);
    }

    @Step("Checking quantity in production queue.")
    public String getQuantityInQueue() {
        return quantityInQueue.getText();
    }

    @Step("Checking the queue process bar is visible.")
    public boolean isQueueProcessBarVisible() {
        try {
            log.debug("Waiting for production queue job (before production)");
            Awaitility.await().pollInterval(500, TimeUnit.MILLISECONDS)
                    .atMost(15, TimeUnit.SECONDS)
                    .until(() -> getText(queueProcessBar).contains("00:"));
        } catch(ConditionTimeoutException e) {
            log.warn("Queue process bar is not visible");
            return false;
        }
        return true;
    }

    @Step("Checking the queue is not visible anymore.")
    public boolean isQueueDisappeared() {
        try {
            log.debug("Waiting for production queue job (after production)");
            Awaitility.await().pollInterval(500, TimeUnit.MILLISECONDS)
                    .atMost(15, TimeUnit.SECONDS)
                    .until(() -> (!isElementDisplayedNoWait(queueElement)));
        } catch(ConditionTimeoutException e) {
            log.warn("Queue has not disappeared");
            return false;
        }
        return true;
    }
}