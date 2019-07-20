package com.mentoring.pageobject;

import com.mentoring.model.User;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MainPage extends Page {

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

    public MainPage(WebDriver driver) {
        super(driver, "");
    }

    public MainPage fillLoginForm(String username, String password) {
        enterLoginUsername(username);
        enterLoginPassword(password);
        return this;
    }

    @Step("Performing login.")
    public CharacterSelectionPage login(User user) {
        fillLoginForm(user.getUserName(), user.getPassword());
        clickLoginButton();
        return new CharacterSelectionPage(driver);
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

    @Step("Filling registrastion form with username: {username}, password: {password} and email address: {emailAddress}")
    public MainPage fillRegistrationForm(String username, String password, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(password);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    @Step("Filling registrastion form with username: {username}, password: {password}, confirm password: {confirmPassword} and email address: {emailAddress}")
    public MainPage fillRegistrationForm(String username, String password, String confirmPassword, String emailAddress) {
        enterRegistrationUsername(username);
        enterRegistrationPassword(password);
        enterRegistrationConfirmPassword(confirmPassword);
        enterRegistrationEmailAddress(emailAddress);
        return this;
    }

    @Step("Submit registration.")
    public CharacterSelectionPage submitRegistration() {
        log.info("Submitting registration...");
        waitUntilClickable(submitRegistrationButton).click();
        return new CharacterSelectionPage(driver);
    }

    @Step("Entering registration username.")
    public MainPage enterRegistrationUsername(String username) {
        log.info("Entering registration username: '{}'", username);
        type(registrationUsername, username);
        return this;
    }

    @Step("Entering registration password.")
    public MainPage enterRegistrationPassword(String password) {
        log.info("Entering registration password: '{}'", password);
        type(registrationPassword, password);
        return this;
    }

    @Step("Entering registration confirm password.")
    public MainPage enterRegistrationConfirmPassword(String confirmPassword) {
        log.info("Entering registration confirm password: '{}'", confirmPassword);
        type(registrationConfirmPassword, confirmPassword);
        return this;
    }

    @Step("Entering registration email address.")
    public MainPage enterRegistrationEmailAddress(String emailAddress) {
        log.info("Entering registration email address: '{}'", emailAddress);
        type(registrationEmailAddress, emailAddress);
        return this;
    }

    public void waitUntilPageLoads() {
    }

    @Step("Asserting if user name is marked as invalid.")
    public boolean isUserNameInvalid() {
        return isElementDisplayed(invalidUsername);
    }

    @Step("Asserting if password is marked as invalid.")
    public boolean isPasswordInvalid() {
        return isElementDisplayed(invalidPassword);
    }

    @Step("Asserting if confirm password is marked as invalid.")
    public boolean isConfirmPasswordInvalid() {
        return isElementDisplayed(invalidConfirmPassword);
    }

    @Step("Asserting if email address is marked as invalid.")
    public boolean isEmailAddressInvalid() {
        return isElementDisplayed(invalidEmail);
    }

    @Step("Asserting submit button's state.")
    public boolean isSubmitButtonEnabled() {
        return submitRegistrationButton.isEnabled();
    }
}
