package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class NotificationContainerProd extends Page implements NotificationContainer {

    @FindBy(id = "notificationcontainer")
    private WebElement notificationContainer;

    @FindBy(css = "#notificationcontainer div.button")
    private List<WebElement> buttons;

    public NotificationContainerProd(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public void waitUntilPageLoads() {
    }

    public boolean isAButtonLabelEqualsTo(String label) {
        sleepForSeconds(1);
        for (WebElement button : buttons) {
            if (button.getText().equals(label)) {
                return true;
            }
        }
        return false;
    }
}

