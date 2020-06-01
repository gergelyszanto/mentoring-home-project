package com.mentoring.test;

import com.mentoring.BaseApiTest;
import com.mentoring.endpoints.Login;
import com.mentoring.endpoints.User;
import com.mentoring.utilities.UserUtils;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class UserTest extends BaseApiTest {

    private static final String VALID_PASSWORD = "Test123!";
    private static final String ACCESS_TOKEN_ID = "accesstokenid";
    private static final String USER_ID = "userid";

    @Test
    public void registerUserAndLogin() {
        String username = UserUtils.generateRandomUsername();
        String email = UserUtils.generateRandomEmail();

        new User().sendRegistrationRequest(email, username, VALID_PASSWORD, 200);
        Response response = new Login().sendLoginRequest(username, VALID_PASSWORD, 200);

        Assertions.assertThat(response.detailedCookies().size())
                .as("Cookie size should be greater than 0.")
                .isGreaterThan(0);
        Assertions.assertThat(response.detailedCookies().hasCookieWithName(ACCESS_TOKEN_ID))
                .as("Cookies: \"" + response.detailedCookies() +
                        "\" should contain \"" + ACCESS_TOKEN_ID + "\"")
                .isTrue();
        Assertions.assertThat(response.detailedCookies().hasCookieWithName("userid"))
                .as("Cookies: \"" + response.detailedCookies() + "\" should contain \"" + USER_ID + "\"")
                .isTrue();
    }
}
