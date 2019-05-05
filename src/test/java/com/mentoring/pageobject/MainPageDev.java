package com.mentoring.pageobject;

import com.mentoring.model.User;
import io.qameta.allure.Step;
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

    @FindBy (id = "login-username")
    private WebElement loginUsername;

    @FindBy (id = "login-password")
    private WebElement loginPassword;

    @FindBy (id = "login-button")
    private WebElement submitLoginButton;

    @FindBy(id = "invalid-username")
    private WebElement invalidUsername;

    @FindBy(id = "invalid-password")
    private WebElement invalidPassword;

    @FindBy(id = "invalid-confirm-password")
    private WebElement invalidConfirmPassword;

    @FindBy(id = "invalid-email")
    private WebElement invalidEmail;

    @FindBy(id = "registration-button")
    private WebElement submitRegistrationButton;

    public MainPageDev(WebDriver driver) {
        super(driver, "");
    }

    @Override
    public MainPageDev fillLoginForm(String username, String password) {
        enterLoginUsername(username);
        enterLoginPassword(password);
        return this;
    }

    @Override
    public CharacterSelectionPageDev login(User user) {
        fillLoginForm(user.getUserName(), user.getPassword());
        clickLoginButton();
        return new CharacterSelectionPageDev(driver);
    }

    @Step("Entering login username.")
    private void enterLoginUsername(String username) {
        log.info("Entering login username: '{}'", username);
        type(loginUsername, username);
    }

    @Step("Entering login password.")
    private void enterLoginPassword(String password) {
        log.info("Entering login password: '{}'", password);
        type(loginPassword, password);
    }

    @Step("Click login.")
    public void clickLoginButton() {
        log.info("Clicking on login button...");
        waitUntilClickable(submitLoginButton).click();
    }

    @Override
    public MainPageDev fillRegistrationForm(String username, String password, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(password);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    public MainPageDev fillRegistrationForm(String username, String password, String confirmPassword, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(confirmPassword);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    @Step("Submit registration.")
    public CharacterSelectionPageDev submitRegistration() {
        log.info("Submitting registration...");
        waitUntilClickable(submitRegistrationButton).click();
        return new CharacterSelectionPageDev(driver);
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
