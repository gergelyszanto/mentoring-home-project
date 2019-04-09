package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MainPageDev extends Page implements MainPage {

    @FindBy(id = "reg-username")
    private WebElement registrationUsername;

    @FindBy(id = "reg-password")
    private WebElement registrationPassword;

    @FindBy(id = "reg-confirm-password")
    private WebElement registrationConfirmPassword;

    @FindBy(id = "reg-email")
    private WebElement registrationEmailAddress;

    @FindBy(id = "registration-button")
    private WebElement submitRegistrationButton;

    public MainPageDev(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public MainPageDev fillRegistrationForm(String username, String password, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(password);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    public CharacterSelectionPageDev submitRegistration() {
        log.info("Submitting registration...");
        waitUntilClickable(submitRegistrationButton).click();
        return new CharacterSelectionPageDev(driver);
    }

    private void enterRegistrationUsername(String username) {
        log.info("Entering registration username: '{}'", username);
        type(registrationUsername, username);
    }

    private void enterRegistrationPassword(String password) {
        log.info("Entering registration password: '{}'", password);
        type(registrationPassword, password);
    }

    private void enterRegistrationConfirmPassword(String confirmPassword) {
        log.info("Entering registration confirm password: '{}'", confirmPassword);
        type(registrationConfirmPassword, confirmPassword);
    }

    private void enterRegistrationEmailAddress(String emailAddress) {
        log.info("Entering registration email address: '{}'", emailAddress);
        type(registrationEmailAddress, emailAddress);
    }

    @Override
    public void waitUntilPageLoads() {
    }
}
