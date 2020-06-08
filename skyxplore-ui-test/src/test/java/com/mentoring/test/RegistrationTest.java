package com.mentoring.test;

import com.mentoring.framework.BaseUiTest;
import com.mentoring.utilities.UserUtils;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.test.assertionsteps.CommonAssertions;
import com.mentoring.messages.Messages;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.REGISTRATION;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RegistrationTest extends BaseUiTest {

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

    @Test(groups = {SMOKE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(REGISTRATION)
    public void successfulRegistration() {
        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
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

    private void assertInvalidUserName(IndexPage indexPage) {
        softAssertion.assertThat(indexPage.isUserNameInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "username.")
                .isTrue();
    }

    private void assertInvalidPassword(IndexPage indexPage) {
        softAssertion.assertThat(indexPage.isPasswordInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "password.")
                .isTrue();
    }

    private void assertInvalidEmail(IndexPage indexPage) {
        softAssertion.assertThat(indexPage.isEmailAddressInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "e-mail address.")
                .isTrue();
    }

    private void assertSubmitButtonDisabled(IndexPage indexPage) {
        softAssertion.assertThat(indexPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isFalse();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(REGISTRATION)
    public void alreadyRegisteredUserName() {
        String validUserName = UserUtils.generateRandomUsername();
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm(validUserName, VALID_PASSWORD, UserUtils.generateRandomEmail())
                .submitRegistration()
                .logout()
                .fillRegistrationForm(validUserName, VALID_PASSWORD, UserUtils.generateRandomEmail());
        assertInvalidUserName(indexPage);
        assertSubmitButtonDisabled(indexPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(REGISTRATION)
    public void alreadyRegisteredEmail() {
        String validEmail = UserUtils.generateRandomEmail();
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm(UserUtils.generateRandomUsername(), VALID_PASSWORD, validEmail)
                .submitRegistration()
                .logout()
                .fillRegistrationForm(UserUtils.generateRandomUsername(), VALID_PASSWORD, validEmail);
        assertInvalidEmail(indexPage);
        assertSubmitButtonDisabled(indexPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.CRITICAL)
    @Feature(REGISTRATION)
    public void insufficientRegistration() {
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm("", VALID_PASSWORD, EMAIL);
        assertInvalidUserName(indexPage);
        assertSubmitButtonDisabled(indexPage);

        indexPage.enterRegistrationUsername(USER_NAME)
                .enterRegistrationPassword("");
        assertInvalidPassword(indexPage);
        assertSubmitButtonDisabled(indexPage);

        indexPage.enterRegistrationPassword(VALID_PASSWORD)
                .enterRegistrationEmailAddress("");
        assertInvalidEmail(indexPage);
        assertSubmitButtonDisabled(indexPage);

        softAssertion.assertAll();
    }

    @DataProvider(name = "wrongEmailAddressData")
    public static Object[][] wrongEmailAddressData() {
        return new Object[][]{{"@test.com"}, {"test@test@test.com"}, {"test t@test.com"}, {"test@tes t.com"}, {"test\t@test.com"}, {"test@.com"}};
    }

    @Test(groups = {NEGATIVE, REGRESSION}, dataProvider = "wrongEmailAddressData")
    @Severity(SeverityLevel.CRITICAL)
    @Feature(REGISTRATION)
    public void wrongEmailAddress(String email) {
        String username = UserUtils.generateRandomUsername();
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm(username, VALID_PASSWORD, email);
        assertThat(indexPage.isEmailAddressInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "username.")
                .isTrue();
        assertThat(!indexPage.isSubmitButtonEnabled())
                .as("Submit button should be disabled.")
                .isTrue();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.NORMAL)
    @Feature(REGISTRATION)
    public void tooShortFields() {
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm(TOO_SHORT_USERNAME, VALID_PASSWORD, EMAIL);
        assertInvalidUserName(indexPage);
        assertSubmitButtonDisabled(indexPage);

        indexPage.enterRegistrationUsername(USER_NAME)
                .enterRegistrationPassword(TOO_SHORT_PASSWORD)
                .enterRegistrationConfirmPassword(TOO_SHORT_PASSWORD);
        assertInvalidPassword(indexPage);
        assertSubmitButtonDisabled(indexPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.NORMAL)
    @Feature(REGISTRATION)
    public void tooLongFields() {
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm(TOO_LONG_USERNAME, VALID_PASSWORD, EMAIL);
        assertInvalidUserName(indexPage);
        assertSubmitButtonDisabled(indexPage);

        indexPage.enterRegistrationUsername(USER_NAME)
                .enterRegistrationPassword(TOO_LONG_PASSWORD)
                .enterRegistrationConfirmPassword(TOO_LONG_PASSWORD);
        assertInvalidPassword(indexPage);
        assertSubmitButtonDisabled(indexPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.NORMAL)
    @Feature(REGISTRATION)
    public void wrongConfirmPassword() {
        IndexPage indexPage = new IndexPage(driver)
                .fillRegistrationForm(
                        UserUtils.generateRandomUsername(),
                        VALID_PASSWORD,
                        VALID_PASSWORD + "a",
                        UserUtils.generateRandomEmail()
                );
        softAssertion.assertThat(indexPage.isConfirmPasswordInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "confirm password.")
                .isTrue();

        softAssertion.assertAll();
    }
}
