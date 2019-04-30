package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NotificationContainerDev extends Page implements NotificationContainer {

    @FindBy(id = "notificationcontainer")
    private WebElement notificationContainer;

    @FindBy(css = "#notificationcontainer div.button")
    private List<WebElement> buttons;

    public NotificationContainerDev(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public void waitUntilPageLoads() {
    }

    public List<String> getNotificationMessages() {
        waitForMilliSec(1000);
        return buttons.stream()
                .map(webElement -> webElement.getText())
                .collect(Collectors.toList());
    }
}

