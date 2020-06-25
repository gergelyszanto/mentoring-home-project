package com.mentoring.test;

import com.mentoring.config.Config;
import com.mentoring.database.Database;
import com.mentoring.database.DbSelects;
import com.mentoring.database.DbUpdates;
import com.mentoring.exceptions.EnvironmentNotSupportedException;
import com.mentoring.framework.BasicTest;
import com.mentoring.generator.User;
import com.mentoring.pageobject.EquipmentPage;
import com.mentoring.pageobject.FactoryPage;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.utilities.UserUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static com.mentoring.model.Features.EQUIPMENT;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EquipmentTest extends BasicTest {

    private static final String DEFAULT_QUANTITY = "1";

    private SoftAssertions softAssertion;
    private Database database;
    private String factoryId;

    @BeforeMethod(alwaysRun = true)
    private void setup() throws SQLException {
        connectToDB();
        softAssertion = new SoftAssertions();
    }

    @AfterMethod(alwaysRun = true)
    private void disconnectFromDB() {
        if(database != null) {
            database.disconnectFromDb();
        } else {
            log.warn("DB disconnection skipped. There is no database connection to disconnect.");
        }
    }

    private void connectToDB() {
        if(!Config.isLocalEnvironmentUsed()) {
            throw new EnvironmentNotSupportedException("Only local environment is supported for database connection. " +
                    "Skipping tests.");
        }
        database = new Database();
    }

    @Description("Create and set up an equipment.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(EQUIPMENT)
    @Test(groups = {REGRESSION})
    public void createAndSetUpAnEquipment() {
        String characterName = UserUtils.generateRandomCharacterName();
        User user = new User();

        FactoryPage factoryPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectCharacter(characterName)
                .openFactoryPage()
                .selectConnectorExtender()
                .clickCex01ProductionButton();
        assertThat(factoryPage.isExtenderItemInQueueVisible())
                .as("Extender item in Queue should be visible.")
                .isTrue();
        softAssertion.assertThat(factoryPage.getQuantityInQueue().equals(DEFAULT_QUANTITY))
                .as("Quantity in Queue should be correct.")
                .isTrue();
        softAssertion.assertThat(factoryPage.isQueueProcessBarVisible())
                .as("Queue process bar should be visible.")
                .isTrue();

        log.info("Updating DB: Set queue process end-time to finish the production.");
        factoryId = DbSelects.getFactoryId(database, user.getEmail(), characterName);
        DbUpdates.setProductionEndTimeByFactoryId(database, factoryId, "0");

        driver.navigate().refresh();
        assertThat(factoryPage.isQueueProcessBarVisible())
                .as("Queue process bar should be updated.")
                .isTrue();
        assertThat(factoryPage.isQueueDisappeared())
                .as("Queue should be disappeared.")
                .isTrue();

        EquipmentPage equipmentPage = new FactoryPage(driver)
                .openOverviewPage()
                .openEquipmentPage();
        assertThat(equipmentPage.isCex01StorageItemVisible())
                .as("CEX-01 item should be visible in Storage section")
                .isTrue();
        assertThat(equipmentPage.isBat01ShipItemVisible())
                .as("BAT-01 item should be visible in Ship section")
                .isTrue();

        equipmentPage.removeBat01ShipItem();
        assertThat(equipmentPage.isEmptyShipSlotVisible())
                .as("Empty slot should be visible in Ship section")
                .isTrue();
        softAssertion.assertThat(equipmentPage.isBat01StorageItemVisible())
                .as("BAT-01 item should be visible in Storage section")
                .isTrue();

        equipmentPage.moveCex01FromStorageToEmptyShipSlot();

        //TODO: 11. Verify the number of empty slots added matches the item description (element's title value)

        softAssertion.assertAll();
    }

}
