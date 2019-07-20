package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.utils.UserUtils;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.MainPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.util.CommonAssertions;
import com.mentoring.util.Messages;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.REGISTRATION;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class RegistrationTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String TOO_SHORT_USERNAME = "12";
    private static final String TOO_SHORT_PASSWORD = "12345";
    private static final String TOO_LONG_USERNAME = "LongUserNameToTestRegistration1";
    private static final String TOO_LONG_PASSWORD = "LongPasswordToTestRegistration1";
    private static final String X_ICON_ASSERTION_MESSAGE = "X icon (validation error) should be visible at ";
    private static final String USER_NAME = UserUtils.generateRandomUsername();
    private static final String EMAIL = UserUtils.generateRandomEmail();

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(REGISTRATION)
    public void successfulRegistration() {
        CharacterSelectionPage characterSelectionPage = new MainPage(driver)
                .fillRegistrationForm(UserUtils.generateRandomUsername(), VALID_PASSWORD, UserUtils.generateRandomEmail())
                .submitRegistration();
        softAssertion.assertThat(characterSelectionPage.isLogOutButtonVisible())
                .as("Logout button should be visible.")
                .isTrue();

        NotificationContainer notificationContainer = new NotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(
                softAssertion,
                notificationContainer,
                Messages.REGISTRATION_SUCCESSFUL
        );

        softAssertion.assertAll();
    }

    private void assertInvalidUserName(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isUserNameInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "username.")
                .isTrue();
    }

    private void assertInvalidPassword(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isPasswordInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "password.")
                .isTrue();
    }

    private void assertInvalidEmail(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isEmailAddressInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "e-mail address.")
                .isTrue();
    }

    private void assertSubmitButtonDisabled(MainPage mainPage) {
        softAssertion.assertThat(mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isFalse();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(REGISTRATION)
    public void alreadyRegisteredUserName() {
        String validUserName = UserUtils.generateRandomUsername();
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(validUserName, VALID_PASSWORD, UserUtils.generateRandomEmail())
                .submitRegistration()
                .logout()
                .fillRegistrationForm(validUserName, VALID_PASSWORD, UserUtils.generateRandomEmail());
        assertInvalidUserName(mainPage);
        assertSubmitButtonDisabled(mainPage);

        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(REGISTRATION)
    public void alreadyRegisteredEmail() {
        String validEmail = UserUtils.generateRandomEmail();
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(UserUtils.generateRandomUsername(), VALID_PASSWORD, validEmail)
                .submitRegistration()
                .logout()
                .fillRegistrationForm(UserUtils.generateRandomUsername(), VALID_PASSWORD, validEmail);
        assertInvalidEmail(mainPage);
        assertSubmitButtonDisabled(mainPage);

        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Feature(REGISTRATION)
    public void insufficientRegistration() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm("", VALID_PASSWORD, EMAIL);
        assertInvalidUserName(mainPage);
        assertSubmitButtonDisabled(mainPage);

        mainPage.enterRegistrationUsername(USER_NAME)
                .enterRegistrationPassword("");
        assertInvalidPassword(mainPage);
        assertSubmitButtonDisabled(mainPage);

        mainPage.enterRegistrationPassword(VALID_PASSWORD)
                .enterRegistrationEmailAddress("");
        assertInvalidEmail(mainPage);
        assertSubmitButtonDisabled(mainPage);

        softAssertion.assertAll();
    }

    @DataProvider(name = "wrongEmailAddressData")
    public static Object[][] wrongEmailAddressData() {
        return new Object[][]{{"@test.com"}, {"test@test@test.com"}, {"test t@test.com"}, {"test@tes t.com"}, {"test\t@test.com"}, {"test@.com"}};
    }

    @Test(groups = "smoke", dataProvider = "wrongEmailAddressData")
    @Severity(SeverityLevel.CRITICAL)
    @Feature(REGISTRATION)
    public void wrongEmailAddress(String email) {
        String username = UserUtils.generateRandomUsername();
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(username, VALID_PASSWORD, email);
        assertThat(mainPage.isEmailAddressInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "username.")
                .isTrue();
        assertThat(!mainPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.NORMAL)
    @Feature(REGISTRATION)
    public void tooShortFields() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(TOO_SHORT_USERNAME, VALID_PASSWORD, EMAIL);
        assertInvalidUserName(mainPage);
        assertSubmitButtonDisabled(mainPage);

        mainPage.enterRegistrationUsername(USER_NAME)
                .enterRegistrationPassword(TOO_SHORT_PASSWORD)
                .enterRegistrationConfirmPassword(TOO_SHORT_PASSWORD);
        assertInvalidPassword(mainPage);
        assertSubmitButtonDisabled(mainPage);

        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.NORMAL)
    @Feature(REGISTRATION)
    public void tooLongFields() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(TOO_LONG_USERNAME, VALID_PASSWORD, EMAIL);
        assertInvalidUserName(mainPage);
        assertSubmitButtonDisabled(mainPage);

        mainPage.enterRegistrationUsername(USER_NAME)
                .enterRegistrationPassword(TOO_LONG_PASSWORD)
                .enterRegistrationConfirmPassword(TOO_LONG_PASSWORD);
        assertInvalidPassword(mainPage);
        assertSubmitButtonDisabled(mainPage);

        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.NORMAL)
    @Feature(REGISTRATION)
    public void wrongConfirmPassword() {
        MainPage mainPage = new MainPage(driver)
                .fillRegistrationForm(
                        UserUtils.generateRandomUsername(),
                        VALID_PASSWORD,
                        VALID_PASSWORD + "a",
                        UserUtils.generateRandomEmail()
                );
        softAssertion.assertThat(mainPage.isConfirmPasswordInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "confirm password.")
                .isTrue();

        softAssertion.assertAll();
    }
}
