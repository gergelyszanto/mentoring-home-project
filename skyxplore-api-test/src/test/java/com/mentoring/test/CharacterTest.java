package com.mentoring.test;

import com.mentoring.endpoints.CharacterEndpoint;
import com.mentoring.framework.BaseApiTest;
import com.mentoring.generator.User;
import com.mentoring.utilities.UserUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class CharacterTest extends BaseApiTest {

    @Test(groups = SMOKE)
    public void characterCrud() {
        User user = new User();

        CharacterEndpoint characterEndpoint = new CharacterEndpoint();
        characterEndpoint.createCharacter(UserUtils.generateRandomCharacterName(), user.getAuthCookie(), 200);
        characterEndpoint.getAllCharacters(user.getAuthCookie(), 200);

        String characterId = characterEndpoint.getCharacterResponse().getCharacterId();
        Assertions.assertThat(characterEndpoint.getAllCharactersResponse())
                .as("All characters list should contain created character response.")
                .contains(characterEndpoint.getCharacterResponse());

        String updated_name = UserUtils.generateRandomCharacterName();
        characterEndpoint.updateCharacter(user.getAuthCookie(), characterId, updated_name, 200);

        characterEndpoint.getAllCharacters(user.getAuthCookie(), 200);
        Assertions.assertThat(characterEndpoint.getAllCharactersResponse().get(0).getCharacterName())
                .as("Character should be successfully updated.")
                .isEqualTo(updated_name);

        characterEndpoint.deleteCharacter(user.getAuthCookie(), characterId, 200);
        characterEndpoint.getAllCharacters(user.getAuthCookie(), 200);

        Assertions.assertThat(characterEndpoint.getAllCharactersResponse())
                .as("Character list should be empty after deletion.")
                .isEmpty();
    }
}
