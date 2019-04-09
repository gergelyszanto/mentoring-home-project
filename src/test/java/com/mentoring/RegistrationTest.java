package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.MainPage;
import org.assertj.core.api.BooleanAssert;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

public class RegistrationTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String TOO_SHORT_USERNAME = "12";
    private static final String TOO_SHORT_PASSWORD = "12345";
    private static final String TOO_LONG_USERNAME = "LongUserNameToTestRegistration1";
    private static final String TOO_LONG_PASSWORD = "LongPasswordToTestRegistration1";

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

    private BooleanAssert assertInvalidUserName(MainPage mainPage) {
        return softAssertion.assertThat(mainPage.invalidUserName())
                .as("X icon (validation error) should be visible at username.")
                .isTrue();
    }

    private BooleanAssert assertInvalidPassword(MainPage mainPage) {
        return softAssertion.assertThat(mainPage.invalidPassword())
                .as("X icon (validation error) should be visible at password.")
                .isTrue();
    }

    private BooleanAssert assertInvalidEmail(MainPage mainPage) {
        return softAssertion.assertThat(mainPage.invalidEmailAddress())
                .as("X icon (validation error) should be visible at e-mail address.")
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
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
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
        softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void insufficientRegistration() {
        String[] userNameArray = {"", User.generateRandomUsername(), User.generateRandomUsername()};
        String[] passwordArray = {VALID_PASSWORD, "", VALID_PASSWORD};
        String[] emailArray = {User.generateRandomEmail(), User.generateRandomEmail(), ""};
        for (int i = 0; i < userNameArray.length; i++) {
            MainPage mainPage = new MainPage(driver)
                    .fillRegistrationForm(userNameArray[i], passwordArray[i], emailArray[i]);
            switch (i) {
                case 0:
                    assertInvalidUserName(mainPage);
                    break;
                case 1:
                    assertInvalidPassword(mainPage);
                    break;
                case 2:
                    assertInvalidEmail(mainPage);
                    break;
            }
            softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                    .as("Submit button should be disabled.")
                    .isTrue();
            softAssertion.assertAll();
        }
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
        Assert.assertTrue(mainPage.invalidEmailAddress(), "X icon (validation error) should be visible at e-mail address.");
        Assert.assertFalse(mainPage.isSubmitButtonEnabled(), "Submit button should be disabled.");
    }

    @Test(groups = "smoke")
    public void tooShortFields() {
        String[] userNameArray = {TOO_SHORT_USERNAME, User.generateRandomUsername()};
        String[] passwordArray = {VALID_PASSWORD, TOO_SHORT_PASSWORD};
        for (int i = 0; i < userNameArray.length; i++) {
            MainPage mainPage = new MainPage(driver)
                    .fillRegistrationForm(userNameArray[i], passwordArray[i], User.generateRandomEmail());
            switch (i) {
                case 0:
                    assertInvalidUserName(mainPage);
                    break;
                case 1:
                    assertInvalidPassword(mainPage);
                    break;
            }
            softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                    .as("Submit button should be disabled.")
                    .isTrue();
            softAssertion.assertAll();
        }
    }

    @Test(groups = "smoke")
    public void tooLongFields() {
        String[] userNameArray = {TOO_LONG_USERNAME, User.generateRandomUsername()};
        String[] passwordArray = {VALID_PASSWORD, TOO_LONG_PASSWORD};
        for (int i = 0; i < userNameArray.length; i++) {
            MainPage mainPage = new MainPage(driver)
                    .fillRegistrationForm(userNameArray[i], passwordArray[i], User.generateRandomEmail());
            switch (i) {
                case 0:
                    assertInvalidUserName(mainPage);
                    break;
                case 1:
                    assertInvalidPassword(mainPage);
                    break;
            }
            softAssertion.assertThat(!mainPage.isSubmitButtonEnabled())
                    .as("Submit button should be disabled.")
                    .isTrue();
            softAssertion.assertAll();
        }
    }

    @Test(groups = "smoke")
    public void wrongConfirmPassword() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(User.generateRandomUsername(), VALID_PASSWORD, VALID_PASSWORD+"a", User.generateRandomEmail());
        softAssertion.assertThat(mainPage.invalidConfirmPassword())
                .as("X icon (validation error) should be visible at confirm password.")
                .isTrue();
        softAssertion.assertAll();
    }
}
