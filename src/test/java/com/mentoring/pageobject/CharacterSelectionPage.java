package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.*;

@Slf4j
public class CharacterSelectionPage extends Page {

    private static final String PAGE_PATH = "/characterselect";

    private static final String LOGGING_OUT = "Logging out.";

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "create-character-name")
    private WebElement createCharacterName;

    @FindBy(id = "invalid-create-character-name")
    private WebElement invalidCreateCharacterName;

    @FindBy(id = "create-character-button")
    private WebElement createCharacterButton;

    @FindBy(css = "tbody#characters .character-name-cell")
    private List<WebElement> characterNames;

    @FindBy(css = "tbody#characters .character-operations button")
    private List<WebElement> characterButtons;

    @FindBy(id = "rename-character-label")
    private WebElement renameCharacterLabel;

    @FindBy(id = "invalid-new-character-name")
    private WebElement invalidNewCharacterName;

    @FindBy(id = "rename-character-button")
    private WebElement renameCharacterButton;

    @FindBy(css = "#characters tr:nth-of-type(1) .character-name-cell")
    private WebElement firstCharacterOnTheList;

    CharacterSelectionPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    @Override
    CharacterSelectionPage waitUntilPageLoads() {
        waitUntilVisible(createCharacterButton);
        return this;
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    public boolean isLogOutButtonVisible() {
        return isElementDisplayed(logoutButton);
    }

    @Step(LOGGING_OUT)
    public MainPage logout() {
        log.info(LOGGING_OUT);
        waitUntilClickable(logoutButton).click();
        return new MainPage(driver);
    }

    // ************* create new character *************

    public CharacterSelectionPage enterCharacterName(String characterName) {
        log.info("Entering character name: '{}'", characterName);
        type(createCharacterName, characterName);
        return this;
    }

    private void clickCreateCharacterButton() {
        log.info("Clicking on Create character button...");
        await().atMost(3, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(this::isCreateCharacterButtonEnabled);
        waitUntilClickable(createCharacterButton).click();
    }

    @Step("Creating new character.")
    public CharacterSelectionPage createNewCharacter(String characterName) {
        enterCharacterName(characterName);
        clickCreateCharacterButton();
        return this;
    }

    // ************* rename character *************

    public void enterRenamedCharacter(String newCharacterName) {
        log.info("Entering the new name of the existing character: '{}'", newCharacterName);
        type(renameCharacterLabel, newCharacterName);
    }

    private void clickRenameCharacterButton() {
        log.info("Clicking on Rename character button...");
        waitUntilClickable(renameCharacterButton).click();
    }

    @Step("Renaming the character name.")
    public void renameCharacter(String newCharacterName) {
        enterRenamedCharacter(newCharacterName);
        clickRenameCharacterButton();
    }

    // ************* methods for the character list *************

    public List<String> getCharacterNamesText() {
        waitForMilliSec(1000);
        return characterNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Selecting the first character on the characters list.")
    public OverviewPage selectFirstCharacter() {
        waitUntilVisible(firstCharacterOnTheList).click();
        return new OverviewPage(driver).waitUntilPageLoads();
    }

    @Step("Clicking on Rename character button.")
    public CharacterSelectionPage clickForRenameCharacterButtonInList() {
        if (characterButtons.size() == 0) {
            waitUntilWebElementListVisible(characterButtons);
        }
        log.info("Clicking on Rename character button in the list...");
        waitUntilClickable(characterButtons.get(0)).click();
        return this;
    }

    @Step("Clicking on Delete character button.")
    public void clickForDeleteCharacterButtonInList() {
        log.info("Clicking on Delete character button in the list...");
        waitUntilClickable(characterButtons.get(1)).click();
    }

    @Step("Asserting if character name is marked as invalid.")
    public boolean isCreateCharacterNameInvalid() {
        return isElementDisplayed(invalidCreateCharacterName);
    }

    @Step("Asserting create character button state.")
    public boolean isCreateCharacterButtonEnabled() {
        return createCharacterButton.isEnabled();
    }

    @Step("Asserting if character name is marked as invalid.")
    public boolean isRenameNewCharacterNameInvalid() {
        return isElementDisplayed(invalidNewCharacterName);
    }

    @Step("Asserting rename character button state.")
    public boolean isRenameCharacterButtonEnabled() {
        return renameCharacterButton.isEnabled();
    }
}