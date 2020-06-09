package com.mentoring.test;

import com.mentoring.endpoints.CharacterEndpoint;
import com.mentoring.endpoints.LoginEndpoint;
import com.mentoring.endpoints.ShipEndpoint;
import com.mentoring.framework.BaseApiTest;
import com.mentoring.generator.User;
import com.mentoring.utilities.UserUtils;
import io.restassured.http.Cookies;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class ShipTest extends BaseApiTest {

    @Test(groups = {SMOKE, REGRESSION})
    public void getShip() {
        User user = new User();

        Cookies authCookies = new LoginEndpoint()
                .sendLoginRequest(user.getUserName(), user.getPassword(), 200)
                .getDetailedCookies();

        CharacterEndpoint characterEndpoint = new CharacterEndpoint();
        characterEndpoint.createCharacter(UserUtils.generateRandomCharacterName(), authCookies, 200);

        Assertions.assertThat(characterEndpoint.getCharacterResponse().getCharacterId())
                .as("Response should contain character ID.")
                .isNotNull();

        String characterId = characterEndpoint.getCharacterResponse().getCharacterId();
        ShipEndpoint shipEndpoint = new ShipEndpoint();
        shipEndpoint.getShip(authCookies, characterId, 200);

        Assertions.assertThat(shipEndpoint.getResponseContent())
                .as("Ship response should not be empty.")
                .isNotNull();
    }
}
