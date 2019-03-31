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
}
