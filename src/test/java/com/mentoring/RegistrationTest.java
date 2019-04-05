package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.Config;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.MainPage;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegistrationTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String EXISTING_EMAIL = "teszt1@test.com";
    private static final String INVALID_EMAIL = "Test@test";
    private static final String TOO_SHORT_USERNAME = "A";
    private static final String TOO_SHORT_PASSWORD = "1";
    private static final String TOO_LONG_USERNAME = "TooLongUserNameToTestTheValidationOnRegistrationForm";
    private static final String TOO_LONG_PASSWORD = "TooLongPasswordToTestTheValidationOnRegistrationForm";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
        driver.get(Config.BASE_URL);
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

    // FIXME: modify this solution to a globally working one
    // prerequisite:
    // a.) query an existing e-mail address from db: "SELECT email FROM USER LIMIT 1"
    // b.) successfulRegistration: save the given e-mail address to a global variable -> test case depends on another one -> WRONG
    // c.) we assume that an exact e-mail address is in the database and we use that one
    @Test(groups = "smoke")
    public void alreadyRegisteredEmail() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, EXISTING_EMAIL);
        softAssertion.assertThat(mainPage.invalidEmailAddress())
                .as("X icon (validation error) should be visible at e-mail address.")
                .isTrue();
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyRegistration() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm("", "", "");
        softAssertion.assertThat(mainPage.invalidUserName())
                .as("X icon (validation error) should be visible at username.")
                .isTrue();
        softAssertion.assertThat(mainPage.invalidPassword())
                .as("X icon (validation error) should be visible at password.")
                .isTrue();
        softAssertion.assertThat(mainPage.invalidEmailAddress())
                .as("X icon (validation error) should be visible at e-mail address.")
                .isTrue();
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void wrongEmailAddress() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, INVALID_EMAIL);
        softAssertion.assertThat(mainPage.invalidEmailAddress())
                .as("X icon (validation error) should be visible at e-mail address.")
                .isTrue();
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void tooShortFields() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(TOO_SHORT_USERNAME, TOO_SHORT_PASSWORD, User.generateRandomEmail());
        softAssertion.assertThat(mainPage.invalidUserName())
                .as("X icon (validation error) should be visible at username.")
                .isTrue();
        softAssertion.assertThat(mainPage.invalidPassword())
                .as("X icon (validation error) should be visible at password.")
                .isTrue();
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
        softAssertion.assertAll();
    }


    @Test(groups = "smoke")
    public void tooLongFields() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(TOO_LONG_USERNAME, TOO_LONG_PASSWORD, User.generateRandomEmail());
        softAssertion.assertThat(mainPage.invalidUserName())
                .as("X icon (validation error) should be visible at username.")
                .isTrue();
        softAssertion.assertThat(mainPage.invalidPassword())
                .as("X icon (validation error) should be visible at password.")
                .isTrue();
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
        softAssertion.assertAll();
    }

}
