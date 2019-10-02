package com.mentoring.pageobject;

import com.mentoring.framework.utils.AllureLogger;
import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Slf4j
public abstract class Page {

    private final String pagePath;
    private final String url;
    WebDriver driver;

    Page(WebDriver driver, String pagePath) {
        this.driver = driver;
        this.pagePath = pagePath;
        PageFactory.initElements(driver, this);
        url = Config.getApplicationUrl() + pagePath;
        logPageLoadToAllure();
    }

    /**
     * Waiter method to be customized for all pages. Should return the specific page always.
     * @return
     */
    abstract Page waitUntilPageLoads();

    public String getUrl() {
        return url;
    }

    private void logPageLoadToAllure() {
        try {
            AllureLogger.logToAllure("Loading page object: '" + getClass().getSimpleName() + "'...");
        } catch (Exception e) {
            log.warn("Error logging page load, but page loaded successfully", e);
        }
    }

    void type(WebElement input, String text) {
        waitUntilVisible(input).click();
        deleteFieldContent(input);
        input.sendKeys(text);
    }

    WebElement waitUntilElementEnabled(WebElement element) {
        try {
            await().atMost(3, TimeUnit.SECONDS)
                    .pollInterval(100, TimeUnit.MILLISECONDS)
                    .until(element::isEnabled);
        } catch (ConditionTimeoutException e) {
            log.warn("Waiting for '{}' to be enabled failed.", element);
        }
        waitForMilliSec(400); //Waiting for animation complete
        return element;
    }

    private void deleteFieldContent(WebElement input) {
        waitUntilVisible(input).sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
    }

    WebElement waitUntilVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Config.LOAD_WAIT);
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    WebElement waitUntilVisible(By element) {
        return new WebDriverWait(driver, Config.LOAD_WAIT)
                .until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    void waitUntilWebElementListVisible(List<WebElement> elements) {
        WebDriverWait wait = new WebDriverWait(driver, Config.LOAD_WAIT);
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    WebElement waitUntilClickable(WebElement element) {
        return new WebDriverWait(driver, Config.LOAD_WAIT).until(ExpectedConditions.elementToBeClickable(element));
    }

    String getText(WebElement element) {
        String elementText = "";
        try {
            elementText = waitUntilVisible(element).getText();
        } catch (NoSuchElementException | TimeoutException e) {
            log.error("Could not locate element", e);
        }
        return elementText;
    }

    boolean isElementDisplayed(WebElement element) {
        boolean elementDisplayed = false;
        try {
            waitUntilVisible(element);
            elementDisplayed = element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException exception) {
            log.info("Element '{}' didn't appear", element);
        }
        return elementDisplayed;
    }

    void waitForMilliSec(int milliSec) {
        try {
            Thread.sleep(milliSec);
        } catch (InterruptedException e) {
            log.error("Thread sleep failed.", e);
        }
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    @Step("Opening URL: {url}")
    public void openUrl(String url) {
        driver.get(url);
    }
}
