package com.mentoring.pageobject;

import java.util.List;

public interface CharacterSelectionPage {
    boolean isLogOutButtonVisible();

    MainPage logout();

    void enterCharacterName(String characterName);

    void createNewCharacter(String characterName);

    List<String> getCharacterNamesText();

    void clickForRenameCharacterButtonInList();

    void clickForDeleteCharacterButtonInList();

    void enterRenamedCharacter(String newCharacterName);

    void renameCharacter(String newCharacterName);

    void acceptAlert();

    boolean isCreateCharacterNameInvalid();

    boolean isCreateCharacterButtonEnabled();

    boolean isRenameNewCharacterNameInvalid();

    boolean isRenameCharacterButtonEnabled();
}
