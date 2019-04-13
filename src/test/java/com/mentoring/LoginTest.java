package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.Messages;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.MainPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.pageobject.PageFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Slf4j
public class LoginTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String WRONG_PASSWORD = "test1234!";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    private MainPage registrationAndLogout(String username, String password) {
        String validEmail = User.generateRandomEmail();
        MainPage mainPage = new PageFactory().getMainPage(driver)
                .fillRegistrationForm(username, password, validEmail)
                .submitRegistration()
                .logout();
        return mainPage;
    }

    @Test(groups = "smoke")
    public void successfulLogin() {
        String validUserName = User.generateRandomUsername();
        registrationAndLogout(validUserName, VALID_PASSWORD)
                .fillLoginForm(validUserName, VALID_PASSWORD)
                .clickLoginButton();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getCharacterSelectionPage(driver);
        softAssertion.assertThat(characterSelectionPage.isLogOutButtonVisible())
                .as("Logout button should be visible.")
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void wrongUserName() {
        String validUserName = User.generateRandomUsername();
        registrationAndLogout(validUserName, VALID_PASSWORD)
                .fillLoginForm(User.generateRandomUsername(), VALID_PASSWORD)
                .clickLoginButton();
        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        softAssertion.assertThat(notificationContainer.isAButtonLabelEqualsTo(Messages.BAD_CREDENTIALS))
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void wrongPassword() {
        String validUserName = User.generateRandomUsername();
        registrationAndLogout(validUserName, VALID_PASSWORD)
                .fillLoginForm(validUserName, WRONG_PASSWORD)
                .clickLoginButton();
        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        softAssertion.assertThat(notificationContainer.isAButtonLabelEqualsTo(Messages.BAD_CREDENTIALS))
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyUsername() {
        String validUserName = User.generateRandomUsername();
        new PageFactory().getMainPage(driver)
                .fillLoginForm(validUserName, "")
                .clickLoginButton();
        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        softAssertion.assertThat(notificationContainer.isAButtonLabelEqualsTo(Messages.EMPTY_CREDENTIALS))
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyPassword() {
        new PageFactory().getMainPage(driver)
                .fillLoginForm("", VALID_PASSWORD)
                .clickLoginButton();
        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        softAssertion.assertThat(notificationContainer.isAButtonLabelEqualsTo(Messages.EMPTY_CREDENTIALS))
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .isTrue();
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyFields() {
        new PageFactory().getMainPage(driver)
                .fillLoginForm("", "")
                .clickLoginButton();
        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        softAssertion.assertThat(notificationContainer.isAButtonLabelEqualsTo(Messages.EMPTY_CREDENTIALS))
                .as(Messages.NOTIFICATION_BUTTON_ASSERTION_MESSAGE)
                .isTrue();
        softAssertion.assertAll();
    }

}
