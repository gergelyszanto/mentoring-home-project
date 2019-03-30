package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.Config;
import com.mentoring.pageobject.MainPage;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegistrationTest extends BasicTest {

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
        driver.get(Config.BASE_URL);
    }

    @Test(groups = "smoke")
    public void successfulRegistration() {
    }
}
