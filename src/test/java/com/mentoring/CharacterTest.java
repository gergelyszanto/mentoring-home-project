package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.PageFactory;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.framework.utils.InitData.registerUser;
import static com.mentoring.model.Features.CHARACTER;

public class CharacterTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void createReadUpdateDeleteCharacter() {
        String validUserName = User.generateRandomUsername();
        String validEmail = User.generateRandomEmail();
        String validCharacterName = User.generateRandomCharacterName();
        String updatedCharacterName = validCharacterName + "a";

        // register a random user
        registerUser(validUserName, VALID_PASSWORD, validEmail);

        // login with this user
        new PageFactory().getMainPage(driver)
                .fillLoginForm(validUserName, VALID_PASSWORD)
                .clickLoginButton();

        CharacterSelectionPage characterSelectionPage = new PageFactory().getCharacterSelectionPage(driver);

        // create new character
        characterSelectionPage.createNewCharacter(validCharacterName);

        softAssertion.assertThat(characterSelectionPage.getCharacterNamesText().contains(validCharacterName))
                .as("New character should be on the list.")
                .isTrue();

        // choose the character from the list and push the rename button
        characterSelectionPage.clickForRenameCharacterButtonInList();

        // rename the character
        characterSelectionPage.renameCharacter(updatedCharacterName);

        softAssertion.assertThat(characterSelectionPage.getCharacterNamesText().contains(updatedCharacterName))
                .as("Updated character should be on the list.")
                .isTrue();

        // delete the character
        characterSelectionPage.clickForDeleteCharacterButtonInList();

        // OK on confirmation dialog box
        characterSelectionPage.acceptAlert();

        softAssertion.assertThat(characterSelectionPage.getCharacterNamesText().contains(updatedCharacterName))
                .as("Deleted character should not be on the list.")
                .isFalse();

        softAssertion.assertAll();
    }
}
