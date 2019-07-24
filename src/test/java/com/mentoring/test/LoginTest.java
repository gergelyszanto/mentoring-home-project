package com.mentoring.test;

import com.mentoring.framework.BasicTest;
import com.mentoring.model.User;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.util.Messages;
import com.mentoring.test.assertionsteps.CommonAssertions;
import com.mentoring.framework.utils.UserUtils;
import com.mentoring.pageobject.CharacterSelectionPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.LOGIN;

@Slf4j
public class LoginTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";
    private static final String WRONG_PASSWORD = "test1234!";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = {SMOKE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(LOGIN)
    public void successfulLogin() {
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user);
        softAssertion.assertThat(characterSelectionPage.isLogOutButtonVisible())
                .as("Logout button should be visible.")
                .isTrue();

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(LOGIN)
    public void wrongUserName() {
        new IndexPage(driver)
                .fillLoginForm(UserUtils.generateRandomUsername(), VALID_PASSWORD)
                .clickLoginButton();

        NotificationContainer notificationContainer = new NotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.BAD_CREDENTIALS);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(LOGIN)
    public void wrongPassword() {
        User user = new User();
        new IndexPage(driver)
                .fillLoginForm(user.getUserName(), WRONG_PASSWORD)
                .clickLoginButton();

        NotificationContainer notificationContainer = new NotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.BAD_CREDENTIALS);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(LOGIN)
    public void emptyPassword() {
        User user = new User();
        new IndexPage(driver)
                .fillLoginForm(user.getUserName(), "")
                .clickLoginButton();

        NotificationContainer notificationContainer = new NotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.EMPTY_CREDENTIALS);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(LOGIN)
    public void emptyUsername() {
        new IndexPage(driver)
                .fillLoginForm("", VALID_PASSWORD)
                .clickLoginButton();

        NotificationContainer notificationContainer = new NotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.EMPTY_CREDENTIALS);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.NORMAL)
    @Feature(LOGIN)
    public void emptyFields() {
        new IndexPage(driver)
                .fillLoginForm("", "")
                .clickLoginButton();

        NotificationContainer notificationContainer = new NotificationContainer(driver);
        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.EMPTY_CREDENTIALS);

        softAssertion.assertAll();
    }
}
