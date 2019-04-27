package com.mentoring;

import com.mentoring.framework.BasicTest;
import com.mentoring.model.User;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.PageFactory;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.mentoring.framework.utils.InitData.registerUser;

public class CharacterTest extends BasicTest {

    private static final String VALID_PASSWORD = "Test1234!";

    private SoftAssertions softAssertion;

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = "smoke")
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

        CharacterSelectionPage csp = new PageFactory().getCharacterSelectionPage(driver);

        // create new character
        csp.createNewCharacter(validCharacterName);

        softAssertion.assertThat(csp.getCharacterNamesText().stream().anyMatch(s -> s.equals(validCharacterName)))
                .as("New character should be on the list.")
                .isTrue();

        // choose the character from the list and push the rename button
        csp.clickForRenameCharacterButtonInList();

        // rename the character
        csp.renameCharacter(updatedCharacterName);

        softAssertion.assertThat(csp.getCharacterNamesText().stream().anyMatch(s -> s.equals(updatedCharacterName)))
                .as("Updated character should be on the list.")
                .isTrue();

        // delete the character
        new PageFactory().getCharacterSelectionPage(driver)
                .clickForDeleteCharacterButtonInList();

        // OK on confirmation dialog box
        driver.switchTo().alert().accept();

        softAssertion.assertThat(csp.getCharacterNamesText().stream().anyMatch(s -> s.equals(updatedCharacterName)))
                .as("Deleted character should not be on the list.")
                .isFalse();

        softAssertion.assertAll();
    }
}
