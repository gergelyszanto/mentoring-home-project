package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

@Slf4j
public class FactoryPage extends Page {

    private static final String PAGE_PATH = "/equipment";

    @FindBy(xpath = "//div[contains(text(),'Bővítő')]")
    private WebElement connectorExtenderButton;

    @FindBy(xpath = "//div[contains(@title,'CEX-01')]//button")
    private WebElement cex01ProductionButton;

    @FindBy(xpath = "//div[@class='queue-element-title']/span[contains(text(),'CEX-01 Csatlakozó bővítő')]")
    private WebElement cex01ProductionQueueItem;

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

    public FactoryPage selectConnectorExtender() {
        waitUntilClickable(connectorExtenderButton).click();
        return this;
    }

    public FactoryPage clickCex01ProductionButton() {
        waitUntilClickable(cex01ProductionButton).click();
        return this;
    }

    @Step("Asserting if extender item in queue is visible.")
    public boolean isExtenderItemInQueueVisible() {
        return isElementDisplayed(cex01ProductionQueueItem);
    }

    @Step("Asserting quantity in production queue.")
    public String getQuantityInQueue() {
        return quantityInQueue.getText();
    }

    @Step("Check the production queue process started.")
    public boolean isQueueProcessStarted() {
        FluentWait wait = new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.textToBePresentInElement(queueProcessBar, "00"));

        return true;
    }

}