package com.mentoring.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends Page {

    @FindBy(id = "reg-username")
    private WebElement usernameRegistration;

    public MainPage(WebDriver driver) {
        super(driver, "");
    }

    private void enterUsername(String username) {
        type(usernameRegistration, username);
    }

    @Override
    public void waitUntilPageLoads() {
    }
}
