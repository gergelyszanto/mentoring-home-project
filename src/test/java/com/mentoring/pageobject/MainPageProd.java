package com.mentoring.pageobject;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MainPageProd extends Page implements MainPage {

    @FindBy (id = "login_username")
    private WebElement loginUsername;

    @FindBy (id = "login_password")
    private WebElement loginPassword;

    @FindBy (id = "login_button")
    private WebElement submitLoginButton;

    @FindBy(id = "registration_username")
    private WebElement registrationUsername;

    @FindBy(id = "invalid_username")
    private WebElement invalidUsername;

    @FindBy(id = "registration_password")
    private WebElement registrationPassword;

    @FindBy(id = "invalid_password")
    private WebElement invalidPassword;

    @FindBy(id = "registration_confirm_password")
    private WebElement registrationConfirmPassword;

    @FindBy(id = "invalid_confirm_password")
    private WebElement invalidConfirmPassword;

    @FindBy(id = "registration_email")
    private WebElement registrationEmailAddress;

    @FindBy(id = "invalid_email")
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

    @Step("Entering login username.")
    public void enterLoginUsername(String username) {
        log.info("Entering login username: '{}'", username);
        type(loginUsername, username);
    }

    @Step("Entering login password.")
    public void enterLoginPassword(String password) {
        log.info("Entering login password: '{}'", password);
        type(loginPassword, password);
    }

    @Step("Click login.")
    public void clickLoginButton() {
        log.info("Clicking on login button...");
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
    @Step("Submit registration.")
    public CharacterSelectionPageProd submitRegistration() {
        log.info("Submitting registration...");
        waitUntilClickable(submitRegistrationButton).click();
        return new CharacterSelectionPageProd(driver);
    }

    @Step("Entering registration username.")
    public void enterRegistrationUsername(String username) {
        log.info("Entering registration username: '{}'", username);
        type(registrationUsername, username);
    }

    @Step("Entering registration password.")
    public void enterRegistrationPassword(String password) {
        log.info("Entering registration password: '{}'", password);
        type(registrationPassword, password);
    }

    @Step("Entering registration confirm password.")
    public void enterRegistrationConfirmPassword(String confirmPassword) {
        log.info("Entering registration confirm password: '{}'", confirmPassword);
        type(registrationConfirmPassword, confirmPassword);
    }

    @Step("Entering registration email address.")
    public void enterRegistrationEmailAddress(String emailAddress) {
        log.info("Entering registration email address: '{}'", emailAddress);
        type(registrationEmailAddress, emailAddress);
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

    @Override
    public void waitUntilPageLoads() {
    }
}