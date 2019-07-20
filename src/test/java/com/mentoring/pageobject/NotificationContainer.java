package com.mentoring.pageobject;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NotificationContainer extends Page {

    @FindBy(id = "notificationcontainer")
    private WebElement notificationContainer;

    @FindBy(css = "#notificationcontainer div.button")
    private List<WebElement> buttons;

    public NotificationContainer(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public void waitUntilPageLoads() {
    }

    @Step("Getting list of toast notifications appeared.")
    public List<String> getNotificationMessages() {
        waitForMilliSec(1000);
        return buttons.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}

