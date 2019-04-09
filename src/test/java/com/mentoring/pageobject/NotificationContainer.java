package com.mentoring.pageobject;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class NotificationContainer extends Page {

    @FindBy(id = "notificationcontainer")
    private WebElement notificationContainer;

/*
    @FindAll( {
            @FindBy(css = "#notificationcontainer div.button")
    })
    */
    @FindBy(css = "#notificationcontainer div.button")
    private List<WebElement> buttons;

    public NotificationContainer(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public void waitUntilPageLoads() {
    }

    public boolean isButtonExist() {
        // TODO: implement
        return false;
    }
}

