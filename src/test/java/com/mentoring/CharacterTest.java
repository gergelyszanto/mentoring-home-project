package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.utils.UserUtils;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.PageFactory;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.CHARACTER;

public class CharacterTest extends BasicTest {

    private static final String X_ICON_ASSERTION_MESSAGE = "X icon (validation error) should be visible at ";
    private static final String TOO_SHORT_CHARACTER_NAME = "ab";
    private static final String TOO_LONG_CHARACTER_NAME = "LongUserNameToTestNewCharacter1";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    /* **************** CRUD test **************** */

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void createReadUpdateDeleteCharacter() {
        String validCharacterName = UserUtils.generateRandomCharacterName();
        String updatedCharacterName = validCharacterName + "a";

        // register a random user and login with it
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getMainPage(driver)
                .login(user);

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

    /* **************** Negative cases **************** */

    /* ********* Create character ********* */

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void tooShortCharacterName() {
        // register a random user and login with it
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getMainPage(driver)
                .login(user);

        characterSelectionPage.enterCharacterName(TOO_SHORT_CHARACTER_NAME);

        assertInvalidUserName(characterSelectionPage);
        assertCreateCharacterButtonDisabled(characterSelectionPage);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void tooLongCharacterName() {
        // register a random user and login with it
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getMainPage(driver)
                .login(user);

        characterSelectionPage.enterCharacterName(TOO_LONG_CHARACTER_NAME);

        assertInvalidUserName(characterSelectionPage);
        assertCreateCharacterButtonDisabled(characterSelectionPage);
        softAssertion.assertAll();
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void alreadyRegisteredCharacter() {
        String validCharacterName = UserUtils.generateRandomCharacterName();

        // register a random user and login with it
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getMainPage(driver)
                .login(user);

        // create new character
        characterSelectionPage.createNewCharacter(validCharacterName);

        // create new character with the same name
        characterSelectionPage.enterCharacterName(validCharacterName);

        assertInvalidUserName(characterSelectionPage);
        assertCreateCharacterButtonDisabled(characterSelectionPage);
        softAssertion.assertAll();
    }

    /* ********* Update character ********* */

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void renameCharacterToTheSameName() {
        String validCharacterName = UserUtils.generateRandomCharacterName();

        // register a random user and login with it
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getMainPage(driver)
                .login(user);

        // create new character
        characterSelectionPage.createNewCharacter(validCharacterName);

        // choose the character from the list and push the rename button
        characterSelectionPage.clickForRenameCharacterButtonInList();

        // rename the first character with the same name
        characterSelectionPage.enterRenamedCharacter(validCharacterName);

        assertInvalidRenameCharacterName(characterSelectionPage);
        assertRenameCharacterButtonDisabled(characterSelectionPage);
        softAssertion.assertAll();

    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void renameCharacterToAnExistingName() {
        String validCharacterName = UserUtils.generateRandomCharacterName();
        String validCharacterName2 = UserUtils.generateRandomCharacterName();

        // register a random user and login with it
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new PageFactory().getMainPage(driver)
                .login(user);

        // create new character
        characterSelectionPage.createNewCharacter(validCharacterName);

        // create new character2
        characterSelectionPage.createNewCharacter(validCharacterName2);

        // choose the first character from the list and push the rename button
        characterSelectionPage.clickForRenameCharacterButtonInList();

        // rename the first character with the second name
        characterSelectionPage.enterRenamedCharacter(validCharacterName2);

        assertInvalidRenameCharacterName(characterSelectionPage);
        assertRenameCharacterButtonDisabled(characterSelectionPage);
        softAssertion.assertAll();
    }

    /* **************** Assertions **************** */

    private void assertInvalidUserName(CharacterSelectionPage characterSelectionPage) {
        softAssertion.assertThat(characterSelectionPage.isCreateCharacterNameInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "create character name.")
                .isTrue();
    }

    private void assertCreateCharacterButtonDisabled(CharacterSelectionPage characterSelectionPage) {
        softAssertion.assertThat(characterSelectionPage.isCreateCharacterButtonEnabled())
                .as("Create Character button should be disabled.")
                .isFalse();
    }

    private void assertInvalidRenameCharacterName(CharacterSelectionPage characterSelectionPage) {
        softAssertion.assertThat(characterSelectionPage.isRenameNewCharacterNameInvalid())
                .as(X_ICON_ASSERTION_MESSAGE + "rename character name.")
                .isTrue();
    }

    private void assertRenameCharacterButtonDisabled(CharacterSelectionPage characterSelectionPage) {
        softAssertion.assertThat(characterSelectionPage.isRenameCharacterButtonEnabled())
                .as("Rename Character button should be disabled.")
                .isFalse();
    }


}
