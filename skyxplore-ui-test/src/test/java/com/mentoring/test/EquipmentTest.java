package com.mentoring.test;

import com.mentoring.framework.BasicTest;
import com.mentoring.generator.User;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.OverviewPage;
import com.mentoring.utilities.UserUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.EQUIPMENT;

public class EquipmentTest extends BasicTest {

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Description("Create and set up an equipment.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(EQUIPMENT)
    @Test(groups = {REGRESSION})
    public void createAndSetUpAnEquipment() {
        String validCharacterName = UserUtils.generateRandomCharacterName();
        User user = new User();

        OverviewPage overviewPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(validCharacterName)
                .selectCharacter(validCharacterName);


        //softAssertion.assertAll();
    }
}
