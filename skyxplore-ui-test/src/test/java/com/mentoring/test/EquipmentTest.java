package com.mentoring.test;

import com.mentoring.framework.BasicTest;
import com.mentoring.generator.User;
import com.mentoring.pageobject.FactoryPage;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.utilities.UserUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.EQUIPMENT;
import static org.assertj.core.api.Assertions.assertThat;

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

        FactoryPage factoryPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(validCharacterName)
                .selectCharacter(validCharacterName)
                .openFactoryPage();

        factoryPage
                .clickExpansionItem();

        factoryPage.clickCex01ManufacturingButton();

        assertThat(factoryPage.isExpansionItemUnderManufacturingVisible())
                .as("Expansion item under manufacturing should be visible.")
                .isTrue();


        //TODO: Subtask-3: Production time of CEX-01 is updated and item is finished
        //TODO: Subtask-4: CEX-01 item is added to the ship

        // softAssertion.assertAll();
    }

}
