package com.mentoring.pageobject;

import com.mentoring.config.Config;
import com.mentoring.framework.utils.AllureLogger;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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

    private void deleteFieldContent(WebElement input) {
        waitUntilVisible(input).sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
    }

    WebElement waitUntilVisible(WebElement element) {
        return waitUntilVisible(element, Config.LOAD_WAIT);
    }

    WebElement waitUntilVisible(WebElement element, int waitSec) {
        WebDriverWait wait = new WebDriverWait(driver, waitSec);
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

    void dragAndDrop(WebElement elementFrom, WebElement elementTo) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("function createEvent(typeOfEvent) {\n" + "var event =document.createEvent(\"CustomEvent\");\n"
                + "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n" + "data: {},\n"
                + "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
                + "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n" + "return event;\n"
                + "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
                + "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
                + "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
                + "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n"
                + "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
                + "var dragStartEvent =createEvent('dragstart');\n" + "dispatchEvent(element, dragStartEvent);\n"
                + "var dropEvent = createEvent('drop');\n"
                + "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
                + "var dragEndEvent = createEvent('dragend');\n"
                + "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
                + "var source = arguments[0];\n" + "var destination = arguments[1];\n"
                + "simulateHTML5DragAndDrop(source,destination);", elementFrom, elementTo);
    }

    String getText(WebElement element) {
        return getText(element, Config.LOAD_WAIT);
    }

    String getTextNoWait(WebElement element) {
        return getText(element, 0);
    }

    private String getText(WebElement element, int waitSec) {
        String elementText = "";
        try {
            elementText = waitUntilVisible(element, waitSec).getText();
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

    boolean isElementDisplayedNoWait(WebElement element) {
        boolean elementDisplayed = false;
        try {
            elementDisplayed = element.isDisplayed();
        } catch (NoSuchElementException exception) {
            log.info("Element '{}' didn't appear", element);
        }
        return elementDisplayed;
    }

    boolean isElementDisplayed(By element) {
        boolean elementDisplayed = false;
        try {
            elementDisplayed = waitUntilVisible(element).isDisplayed();
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
