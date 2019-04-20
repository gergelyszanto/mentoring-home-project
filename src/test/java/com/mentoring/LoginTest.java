package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.util.Messages;
import com.mentoring.util.CommonAssertions;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.pageobject.PageFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.framework.utils.InitData.registerUser;

@Slf4j
public class LoginTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String WRONG_PASSWORD = "test1234!";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = "smoke")
    public void successfulLogin() {
        String validUserName = User.generateRandomUsername();
        String validEmail = User.generateRandomEmail();

        registerUser(validUserName, VALID_PASSWORD, validEmail);

        new PageFactory().getMainPage(driver)
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
        String validEmail = User.generateRandomEmail();

        registerUser(validUserName, VALID_PASSWORD, validEmail);

        new PageFactory().getMainPage(driver)
                .fillLoginForm(User.generateRandomUsername(), VALID_PASSWORD)
                .clickLoginButton();

        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.BAD_CREDENTIALS);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void wrongPassword() {
        String validUserName = User.generateRandomUsername();
        String validEmail = User.generateRandomEmail();

        registerUser(validUserName, VALID_PASSWORD, validEmail);

        new PageFactory().getMainPage(driver)
                .fillLoginForm(validUserName, WRONG_PASSWORD)
                .clickLoginButton();

        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.BAD_CREDENTIALS);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyUsername() {
        String validUserName = User.generateRandomUsername();

        new PageFactory().getMainPage(driver)
                .fillLoginForm(validUserName, "")
                .clickLoginButton();

        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.EMPTY_CREDENTIALS);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyPassword() {
        new PageFactory().getMainPage(driver)
                .fillLoginForm("", VALID_PASSWORD)
                .clickLoginButton();

        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.EMPTY_CREDENTIALS);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    public void emptyFields() {
        new PageFactory().getMainPage(driver)
                .fillLoginForm("", "")
                .clickLoginButton();

        NotificationContainer notificationContainer = new PageFactory().getNotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.EMPTY_CREDENTIALS);
        softAssertion.assertAll();
    }
}
