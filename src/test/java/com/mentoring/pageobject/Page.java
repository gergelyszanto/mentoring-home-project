package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public abstract class Page {

    private final String url;
    private WebDriver driver;

    Page(WebDriver driver, String path) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        url = Config.BASE_URL + path;
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

    public abstract void waitUntilPageLoads();

}
