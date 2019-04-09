package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MainPageProd extends Page implements MainPage {

    @FindBy(id = "registration_username")
    private WebElement registrationUsername;

    @FindBy(id = "registration_password")
    private WebElement registrationPassword;

    @FindBy(id = "registration_confirm_password")
    private WebElement registrationConfirmPassword;

    @FindBy(id = "registration_email")
    private WebElement registrationEmailAddress;

    @FindBy(id = "registration_button")
    private WebElement submitRegistrationButton;

    MainPageProd(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public MainPageProd fillRegistrationForm(String username, String password, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(password);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    public CharacterSelectionPageProd submitRegistration() {
        log.info("Submitting registration...");
        waitUntilClickable(submitRegistrationButton).click();
        return new CharacterSelectionPageProd(driver);
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