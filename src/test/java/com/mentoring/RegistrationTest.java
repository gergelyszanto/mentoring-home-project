package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.MainPage;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegistrationTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String TOO_SHORT_USERNAME = "12";
    private static final String TOO_SHORT_PASSWORD = "12345";
    private static final String TOO_LONG_USERNAME = "LongUserNameToTestRegistration1";
    private static final String TOO_LONG_PASSWORD = "LongPasswordToTestRegistration1";
    private static final String X_ICON_MESSAGE = "X icon (validation error) should be visible at ";
    private static final String USER_NAME = User.generateRandomUsername();
    private static final String EMAIL = User.generateRandomEmail();

    private Assertions hardAssertion;
    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = "smoke")
    public void successfulRegistration() {
        CharacterSelectionPage characterSelectionPage = new MainPage(driver)
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, User.generateRandomEmail())
                .submitRegistration();
        softAssertion.assertThat(characterSelectionPage.isLogOutButtonVisible())
                .as("Logout button should be visible.")
                .isTrue();
        //TODO: Add notification assertion
        softAssertion.assertAll();
    }

    private void assertInvalidUserName(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isUserNameInvalid())
                .as(X_ICON_MESSAGE + "username.")
                .isTrue();
    }

    private void assertInvalidPassword(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isPasswordInvalid())
                .as(X_ICON_MESSAGE + "password.")
                .isTrue();
    }

    private void assertInvalidEmail(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isEmailAddressInvalid())
                .as(X_ICON_MESSAGE + "e-mail address.")
                .isTrue();
    }

    private void assertSubmitButtonEnabled(MainPage mainPage) {
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
    }


    @Test(groups = "smoke")
    public void alreadyRegisteredUserName() {
        String validUserName = User.generateRandomUsername();
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(validUserName, VALID_PASSWORD, User.generateRandomEmail())
                .submitRegistration()
                .logout()
                .fillRegistrationForm(validUserName, VALID_PASSWORD, User.generateRandomEmail());
        assertInvalidUserName(mainPage);
        assertSubmitButtonEnabled(mainPage);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void alreadyRegisteredEmail() {
        String validEmail = User.generateRandomEmail();
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, validEmail)
                .submitRegistration()
                .logout()
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, validEmail);
        assertInvalidEmail(mainPage);
        assertSubmitButtonEnabled(mainPage);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void insufficientRegistration() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(USER_NAME, VALID_PASSWORD, EMAIL);
        // empty username:
        mainPage.enterRegistrationUsername("");
        assertInvalidUserName(mainPage);
        assertSubmitButtonEnabled(mainPage);
        mainPage.enterRegistrationUsername(USER_NAME);
        // empty password:
        mainPage.enterRegistrationPassword("");
        assertInvalidPassword(mainPage);
        assertSubmitButtonEnabled(mainPage);
        mainPage.enterRegistrationPassword(VALID_PASSWORD);
        // empty email:
        mainPage.enterRegistrationEmailAddress("");
        assertInvalidEmail(mainPage);
        assertSubmitButtonEnabled(mainPage);

        softAssertion.assertAll();
    }

    @DataProvider(name = "wrongEmailAddressData")
    public static Object[][] wrongEmailAddressData() {
        return new Object[][]{{"@test.com"}, {"test@test@test.com"}, {"test t@test.com"}, {"test@tes t.com"}, {"test\t@test.com"}, {"test@.com"}};
    }

    @Test(groups = "smoke", dataProvider = "wrongEmailAddressData")
    public void wrongEmailAddress(String email) {
        String username = User.generateRandomUsername();
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(username, VALID_PASSWORD, email);
        hardAssertion.assertThat(mainPage.isEmailAddressInvalid())
                .as(X_ICON_MESSAGE + "username.")
                .isTrue();
        hardAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
    }

    @Test(groups = "smoke")
    public void tooShortFields() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(USER_NAME, VALID_PASSWORD, EMAIL);
        // short username:
        mainPage.enterRegistrationUsername(TOO_SHORT_USERNAME);
        assertInvalidUserName(mainPage);
        assertSubmitButtonEnabled(mainPage);
        mainPage.enterRegistrationUsername(USER_NAME);
        // short password:
        mainPage.enterRegistrationPassword(TOO_SHORT_PASSWORD);
        mainPage.enterRegistrationConfirmPassword(TOO_SHORT_PASSWORD);
        assertInvalidPassword(mainPage);
        assertSubmitButtonEnabled(mainPage);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void tooLongFields() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(USER_NAME, VALID_PASSWORD, EMAIL);
        // long username:
        mainPage.enterRegistrationUsername(TOO_LONG_USERNAME);
        assertInvalidUserName(mainPage);
        assertSubmitButtonEnabled(mainPage);
        mainPage.enterRegistrationUsername(USER_NAME);
        // long password:
        mainPage.enterRegistrationPassword(TOO_LONG_PASSWORD);
        mainPage.enterRegistrationConfirmPassword(TOO_LONG_PASSWORD);
        assertInvalidPassword(mainPage);
        assertSubmitButtonEnabled(mainPage);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void wrongConfirmPassword() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, VALID_PASSWORD + "a", User.generateRandomEmail());
        softAssertion.assertThat(mainPage.isConfirmPasswordInvalid())
                .as(X_ICON_MESSAGE + "confirm password.")
                .isTrue();
        softAssertion.assertAll();
    }
}
