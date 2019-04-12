package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MainPageProd extends Page implements MainPage {

    @FindBy (id = "login-username")
    private WebElement loginUsername;

    @FindBy (id = "login-password")
    private WebElement loginPassword;

    @FindBy (id = "login-button")
    private WebElement submitLoginButton;

    @FindBy(id = "registration_username")
    private WebElement registrationUsername;

    @FindBy(id = "invalid-username")
    private WebElement invalidUsername;

    @FindBy(id = "registration_password")
    private WebElement registrationPassword;

    @FindBy(id = "invalid-password")
    private WebElement invalidPassword;

    @FindBy(id = "registration_confirm_password")
    private WebElement registrationConfirmPassword;

    @FindBy(id = "invalid-confirm-password")
    private WebElement invalidConfirmPassword;

    @FindBy(id = "registration_email")
    private WebElement registrationEmailAddress;

    @FindBy(id = "invalid-email")
    private WebElement invalidEmail;

    @FindBy(id = "registration_button")
    private WebElement submitRegistrationButton;

    MainPageProd(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public MainPageProd fillLoginForm(String username, String password) {
        enterLoginUsername(username);
        enterLoginPassword(password);
        return this;
    }

    public void enterLoginUsername(String username) {
        log.info("Entering login username: '{}'", username);
        type(loginUsername, username);
    }

    public void enterLoginPassword(String password) {
        log.info("Entering login password: '{}'", password);
        type(loginPassword, password);
    }

    public void submitLogin() {
        log.info("Submitting login...");
        waitUntilClickable(submitLoginButton).click();
    }

    @Override
    public MainPageProd fillRegistrationForm(String username, String password, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(password);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    @Override
    public MainPageProd fillRegistrationForm(String username, String password, String confirmPassword, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(confirmPassword);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    @Override
    public CharacterSelectionPageProd submitRegistration() {
        log.info("Submitting registration...");
        waitUntilClickable(submitRegistrationButton).click();
        return new CharacterSelectionPageProd(driver);
    }

    @Override
    public void enterRegistrationUsername(String username) {
        log.info("Entering registration username: '{}'", username);
        type(registrationUsername, username);
    }

    @Override
    public void enterRegistrationPassword(String password) {
        log.info("Entering registration password: '{}'", password);
        type(registrationPassword, password);
    }

    @Override
    public void enterRegistrationConfirmPassword(String confirmPassword) {
        log.info("Entering registration confirm password: '{}'", confirmPassword);
        type(registrationConfirmPassword, confirmPassword);
    }

    @Override
    public void enterRegistrationEmailAddress(String emailAddress) {
        log.info("Entering registration email address: '{}'", emailAddress);
        type(registrationEmailAddress, emailAddress);
    }

    @Override
    public void waitUntilPageLoads() {
    }

    @Override
    public boolean isUserNameInvalid() {
        return isElementDisplayed(invalidUsername);
    }

    @Override
    public boolean isPasswordInvalid() {
        return isElementDisplayed(invalidPassword);
    }

    @Override
    public boolean isConfirmPasswordInvalid() {
        return isElementDisplayed(invalidConfirmPassword);
    }

    @Override
    public boolean isEmailAddressInvalid() {
        return isElementDisplayed(invalidEmail);
    }

    @Override
    public boolean isSubmitButtonEnabled() {
        return submitRegistrationButton.isEnabled();
    }

}