package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public abstract class Page {

    WebDriver driver;
    private final String url;

    Page(WebDriver driver, String path) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        url = Config.APPLICATION_URL + path;
    }

    void type(WebElement input, String text) {
        waitUntilVisible(input).click();
        deleteFieldContent(input);
        input.sendKeys(text);
    }

    private void deleteFieldContent(WebElement input) {
        waitUntilVisible(input).sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
    }

    private WebElement waitUntilVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Config.LOAD_WAIT);
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
        return wait.until(ExpectedConditions.visibilityOf(element));
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

    public abstract void waitUntilPageLoads();

    void sleepForSeconds(int sec) {
        try {
            Thread.sleep(1000 * sec);
        } catch (InterruptedException e) {
            log.error(e.getStackTrace().toString());
        }
    }

}
