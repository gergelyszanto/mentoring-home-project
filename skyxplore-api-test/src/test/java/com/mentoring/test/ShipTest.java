package com.mentoring.test;

import com.mentoring.endpoints.CharacterEndpoint;
import com.mentoring.endpoints.ShipEndpoint;
import com.mentoring.framework.BaseApiTest;
import com.mentoring.generator.User;
import com.mentoring.utilities.UserUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class ShipTest extends BaseApiTest {

    @Test(groups = {SMOKE, REGRESSION})
    public void getShip() {
        User user = new User();

        CharacterEndpoint characterEndpoint = new CharacterEndpoint();
        characterEndpoint.createCharacter(UserUtils.generateRandomCharacterName(),
                user.getAuthCookie(),
                200);

        Assertions.assertThat(characterEndpoint.getCharacterResponse().getCharacterId())
                .as("Response should contain character ID.")
                .isNotNull();

        String characterId = characterEndpoint.getCharacterResponse().getCharacterId();
        ShipEndpoint shipEndpoint = new ShipEndpoint();
        shipEndpoint.getShip(user.getAuthCookie(), characterId, 200);

        Assertions.assertThat(shipEndpoint.getResponseContent())
                .as("Ship response should not be empty.")
                .isNotNull();
    }
}
