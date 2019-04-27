package com.mentoring.pageobject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CharacterSelectionPageProd extends Page implements CharacterSelectionPage {

    private static final String PATH = "characterselect";

    @FindBy(css = "[onclick='authService.logout()']")
    private WebElement logoutButton;

    @FindBy(id = "newcharactername")
    private WebElement createCharacterName;

    @FindBy(id = "invalid_newcharactername")
    private WebElement invalidCreateCharacterName;

    @FindBy(id = "newcharacterbutton")
    private WebElement createCharacterButton;

    @FindBy(css = "tbody#characters .cursorpointer")
    private List<WebElement> characterNames;

    @FindBy(css = "tbody#characters .textaligncenter:nth-child(2)")
    private List<WebElement> characterButtons;

    @FindBy(id = "renamecharacterid")
    private WebElement renameCharacterLabel;

    @FindBy(id = "renamecharacterbutton")
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
    public MainPage logout() {
        log.info("Log out...");
        waitUntilClickable(logoutButton).click();
        return new MainPageProd(driver);
    }

    // ************* create new character *************

    private void enterCharacterName(String characterName) {
        log.info("Creating new character: '{}'", characterName);
        type(createCharacterName, characterName);
    }

    private void clickCreateCharacterButton() {
        log.info("Clicking on Create character button...");
        waitUntilClickable(createCharacterButton).click();
    }

    @Override
    public void createNewCharacter(String characterName) {
        enterCharacterName(characterName);
        clickCreateCharacterButton();
    }

    // ************* rename character *************

    private void enterRenamedCharacter(String newCharacterName) {
        log.info("Entering the new name of the existing character: '{}'", newCharacterName);
        type(renameCharacterLabel, newCharacterName);
    }

    private void clickRenameCharacterButton() {
        log.info("Clicking on Rename character button...");
        waitUntilClickable(renameCharacterButton).click();
    }

    public void renameCharacter(String newCharacterName) {
        enterCharacterName(newCharacterName);
        clickRenameCharacterButton();
    }

    // ************* methods for the character list *************

    public List<String> getCharacterNamesText() {
        waitForMilliSec(1000);
        return characterNames.stream()
                .map(webElement -> webElement.getText())
                .collect(Collectors.toList());
    }

    public void clickForRenameCharacterButtonInList() {
        log.info("Clicking on Rename character button in the list...");
        waitUntilClickable(characterButtons.get(0)).click();
    }

    public void clickForDeleteCharacterButtonInList() {
        log.info("Clicking on Delete character button in the list...");
        waitUntilClickable(characterButtons.get(1)).click();
    }
}
