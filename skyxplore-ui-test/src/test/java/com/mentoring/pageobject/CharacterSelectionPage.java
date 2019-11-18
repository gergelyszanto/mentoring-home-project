package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class CharacterSelectionPage extends Page {

    private static final String CHARACTER_BY_NAME_SELECTOR_TEMPLATE = "//td[contains(text(),'%1$s')]";
    private static final String RENAME_CHARACTER_BY_NAME_SELECTOR_TEMPLATE =
            "//td[contains(text(),'%1$s')]/following-sibling::td/button[contains(text(),'Átnevezés')]";
    private static final String DELETE_CHARACTER_BY_NAME_SELECTOR_TEMPLATE =
            "//td[contains(text(),'%1$s')]/following-sibling::td/button[contains(text(),'Törlés')]";
    private static final String PAGE_PATH = "/characterselect";

    private static final String LOGGING_OUT = "Logging out.";

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "create-character-name")
    private WebElement createCharacterName;

    @FindBy(css = "#invalid-create-character-name[style='display: block;']")
    private WebElement invalidCreateCharacterName;

    @FindBy(id = "create-character-button")
    private WebElement createCharacterButton;

    @FindBy(css = "tbody#characters .character-name-cell")
    private List<WebElement> characterNames;

    @FindBy(id = "rename-character-label")
    private WebElement renameCharacterLabel;

    @FindBy(css = "#invalid-new-character-name[style='display: block;']")
    private WebElement invalidNewCharacterName;

    @FindBy(id = "rename-character-button")
    private WebElement renameCharacterButton;

    CharacterSelectionPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    CharacterSelectionPage waitUntilPageLoads() {
        waitUntilVisible(createCharacterButton);
        return this;
    }

    public boolean isLogOutButtonVisible() {
        return isElementDisplayed(logoutButton);
    }

    @Step(LOGGING_OUT)
    public IndexPage logout() {
        log.info(LOGGING_OUT);
        waitUntilClickable(logoutButton).click();
        return new IndexPage(driver);
    }

    // ************* create new character *************

    public CharacterSelectionPage enterCharacterName(String characterName) {
        log.info("Entering character name: '{}'", characterName);
        type(createCharacterName, characterName);
        return this;
    }

    private void clickCreateCharacterButton() {
        log.info("Clicking on Create character button...");
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
        try {
            log.debug("Waiting for character list not to be empty.");
            Awaitility.await().atMost(1, TimeUnit.SECONDS).until(() -> !characterNames.isEmpty());
        } catch (ConditionTimeoutException e) {
            log.debug("Character list remained empty.");
        }
        return characterNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Selecting the first character on the characters list.")
    public OverviewPage selectCharacter(String characterName) {
        waitUntilVisible(By.xpath(String.format(CHARACTER_BY_NAME_SELECTOR_TEMPLATE, characterName)))
                .click();
        return new OverviewPage(driver).waitUntilPageLoads();
    }

    @Step("Clicking on Rename character button for character: {characterName}.")
    public CharacterSelectionPage clickRenameCharacter(String characterName) {
        waitUntilVisible(By.xpath(String.format(RENAME_CHARACTER_BY_NAME_SELECTOR_TEMPLATE, characterName)))
                .click();
        return this;
    }

    @Step("Rename from: {characterNameOld} - and choose new character: {characterNameNew}.")
    public OverviewPage renameAndChooseCharacter(String characterNameOld, String characterNameNew) {
        clickRenameCharacter(characterNameOld);
        renameCharacter(characterNameNew);
        return selectCharacter(characterNameNew);
    }

    @Step("")
    public CharacterSelectionPage deleteCharacter(String characterName) {
        clickForDeleteCharacterButtonInList(characterName);
        acceptAlert();
        return this;
    }

    @Step("Deleting character: {characterName}.")
    public void clickForDeleteCharacterButtonInList(String characterName) {
        waitUntilVisible(By.xpath(String.format(DELETE_CHARACTER_BY_NAME_SELECTOR_TEMPLATE, characterName)))
                .click();
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