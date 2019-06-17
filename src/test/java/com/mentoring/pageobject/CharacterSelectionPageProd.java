package com.mentoring.pageobject;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CharacterSelectionPageProd extends Page implements CharacterSelectionPage {

    private static final String PATH = "characterselect";

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

    CharacterSelectionPageProd(WebDriver driver) {
        super(driver, PATH);
    }

    @Override
    public boolean isLogOutButtonVisible() {
        return isElementDisplayed(logoutButton);
    }

    @Override
    public void waitUntilPageLoads() {
    }

    @Override
    @Step(LOGGING_OUT)
    public MainPage logout() {
        log.info(LOGGING_OUT);
        waitUntilClickable(logoutButton).click();
        return new MainPageProd(driver);
    }

    // ************* create new character *************

    public void enterCharacterName(String characterName) {
        log.info("Creating new character: '{}'", characterName);
        type(createCharacterName, characterName);
    }

    private void clickCreateCharacterButton() {
        log.info("Clicking on Create character button...");
        waitUntilClickable(createCharacterButton).click();
    }

    @Step("Creating new character.")
    public void createNewCharacter(String characterName) {
        enterCharacterName(characterName);
        clickCreateCharacterButton();
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
                .map(webElement -> webElement.getText())
                .collect(Collectors.toList());
    }

    @Step("Clicking on Rename character button.")
    public void clickForRenameCharacterButtonInList() {
        if (characterButtons.size() == 0)
            waitUntilWebElementListVisible(characterButtons);
        log.info("Clicking on Rename character button in the list...");
        waitUntilClickable(characterButtons.get(0)).click();
    }

    @Step("Clicking on Delete character button.")
    public void clickForDeleteCharacterButtonInList() {
        log.info("Clicking on Delete character button in the list...");
        waitUntilClickable(characterButtons.get(1)).click();
    }

    @Override
    public boolean isCreateCharacterNameInvalid() {
        return isElementDisplayed(invalidCreateCharacterName);
    }

    @Override
    public boolean isCreateCharacterButtonEnabled() {
        return createCharacterButton.isEnabled();
    }

    @Override
    public boolean isRenameNewCharacterNameInvalid() {
        return isElementDisplayed(invalidNewCharacterName);
    }

    @Override
    public boolean isRenameCharacterButtonEnabled() {
        return renameCharacterButton.isEnabled();
    }
}
