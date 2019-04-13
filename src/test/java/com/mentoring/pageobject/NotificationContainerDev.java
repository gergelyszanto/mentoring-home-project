package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

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

    public boolean isAButtonLabelEqualsTo(String label) {
        waitForMilliSec(1000);
        // TODO: remove after hotfix of LoginTest.wrongPassword()
        log.debug("debug1: button counter=" + buttons.size());
        for (WebElement button : buttons) {
            // TODO: remove after hotfix of LoginTest.wrongPassword()
            log.debug("debug2: button label=" + button.getText());
            if (button.getText().equals(label)) {
                return true;
            }
        }
        return false;
    }
}

