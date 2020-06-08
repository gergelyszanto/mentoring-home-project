package com.mentoring.test;

import com.mentoring.framework.BaseApiTest;
import com.mentoring.endpoints.LoginEndpoint;
import com.mentoring.endpoints.UserEndpoint;
import com.mentoring.utilities.UserUtils;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class UserTest extends BaseApiTest {

    private static final String VALID_PASSWORD = "Test123!";
    private static final String ACCESS_TOKEN_ID = "accesstokenid";
    private static final String USER_ID = "userid";

    @Test(groups = {SMOKE, REGRESSION})
    public void registerUserAndLogin() {
        String username = UserUtils.generateRandomUsername();
        String email = UserUtils.generateRandomEmail();

        new UserEndpoint().sendRegistrationRequest(email, username, VALID_PASSWORD, 200);
        Response loginResponse = new LoginEndpoint().sendLoginRequest(username, VALID_PASSWORD, 200);

        Assertions.assertThat(loginResponse.detailedCookies().size())
                .as("Cookie size should be greater than 0.")
                .isGreaterThan(0);
        Assertions.assertThat(loginResponse.detailedCookies().hasCookieWithName(ACCESS_TOKEN_ID))
                .as("Cookies: \"" + loginResponse.detailedCookies() +
                        "\" should contain \"" + ACCESS_TOKEN_ID + "\"")
                .isTrue();
        Assertions.assertThat(loginResponse.detailedCookies().hasCookieWithName("userid"))
                .as("Cookies: \"" + loginResponse.detailedCookies() + "\" should contain \"" + USER_ID + "\"")
                .isTrue();
    }
}
