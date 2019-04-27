package com.mentoring.pageobject;

import java.util.List;

public interface CharacterSelectionPage {
    boolean isLogOutButtonVisible();

    MainPage logout();

    void createNewCharacter(String characterName);

    List<String> getCharacterNamesText();

    void clickForRenameCharacterButtonInList();

    void clickForDeleteCharacterButtonInList();

    void renameCharacter(String newCharacterName);

}
