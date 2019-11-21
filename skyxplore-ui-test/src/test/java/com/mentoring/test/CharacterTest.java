package com.mentoring.test;

import com.mentoring.framework.BasicTest;
import com.mentoring.utilities.UserUtils;
import com.mentoring.generator.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.IndexPage;
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

    @Test(groups = {SMOKE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void createReadUpdateDeleteCharacter() {
        String validCharacterName = UserUtils.generateRandomCharacterName();
        String updatedCharacterName = validCharacterName + "a";

        User user = new User();
        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(validCharacterName);
        softAssertion.assertThat(characterSelectionPage.getCharacterNamesText().contains(validCharacterName))
                .as("New character should be on the list.")
                .isTrue();

        characterSelectionPage
                .clickRenameCharacter(validCharacterName)
                .renameCharacter(updatedCharacterName);
        softAssertion.assertThat(characterSelectionPage.getCharacterNamesText().contains(updatedCharacterName))
                .as("Updated character should be on the list.")
                .isTrue();

        characterSelectionPage.deleteCharacter(updatedCharacterName);
        softAssertion.assertThat(characterSelectionPage.getCharacterNamesText().contains(updatedCharacterName))
                .as("Deleted character should not be on the list.")
                .isFalse();

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void tooShortCharacterName() {
        User user = new User();

        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user)
                .enterCharacterName(TOO_SHORT_CHARACTER_NAME);
        assertInvalidUserName(characterSelectionPage);
        assertCreateCharacterButtonDisabled(characterSelectionPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void tooLongCharacterName() {
        User user = new User();

        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user)
                .enterCharacterName(TOO_LONG_CHARACTER_NAME);
        assertInvalidUserName(characterSelectionPage);
        assertCreateCharacterButtonDisabled(characterSelectionPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void alreadyRegisteredCharacter() {
        String validCharacterName = UserUtils.generateRandomCharacterName();

        User user = new User();
        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(validCharacterName);

        characterSelectionPage.enterCharacterName(validCharacterName);
        assertInvalidUserName(characterSelectionPage);
        assertCreateCharacterButtonDisabled(characterSelectionPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void renameCharacterToTheSameName() {
        String validCharacterName = UserUtils.generateRandomCharacterName();

        User user = new User();
        CharacterSelectionPage characterSelectionPage = new IndexPage(driver).login(user);
        characterSelectionPage.
                createNewCharacter(validCharacterName)
                .clickRenameCharacter(validCharacterName);
        characterSelectionPage.enterRenamedCharacter(validCharacterName);

        assertInvalidRenameCharacterName(characterSelectionPage);
        assertRenameCharacterButtonDisabled(characterSelectionPage);

        softAssertion.assertAll();
    }

    @Test(groups = {NEGATIVE, REGRESSION})
    @Severity(SeverityLevel.BLOCKER)
    @Feature(CHARACTER)
    public void renameCharacterToAnExistingName() {
        String validCharacterName = UserUtils.generateRandomCharacterName();
        String validCharacterName2 = UserUtils.generateRandomCharacterName();
        User user = new User();

        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(validCharacterName)
                .createNewCharacter(validCharacterName2)
                .clickRenameCharacter(validCharacterName);

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
