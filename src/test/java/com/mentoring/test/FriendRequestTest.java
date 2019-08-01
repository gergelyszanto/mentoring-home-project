package com.mentoring.test;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.database.Database;
import com.mentoring.framework.database.DbSelects;
import com.mentoring.framework.utils.UserUtils;
import com.mentoring.model.User;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.*;

import java.sql.SQLException;

import static com.mentoring.model.Features.FRIEND_REQUEST;

@Slf4j
public class FriendRequestTest extends BasicTest {

    private SoftAssertions softAssertion;
    private Database database;
    private User userA;
    private User userB;
    private String characterNameA;
    private String characterNameB;
    String accessTokenIdForUserA;
    String accessTokenIdForUserB;
    String userIdForUserA;
    String userIdForUserB;

    @BeforeClass(alwaysRun = true)
    private void setup() {
        database = new Database();
    }

    @AfterClass(alwaysRun = true)
    private void close() {
        database.disconnectFromDb();
    }

    @BeforeMethod(alwaysRun = true)
    private void initMethod() throws SQLException {
        softAssertion = new SoftAssertions();

        // create userA, login and create a character for it with RestAPI
        userA = new User();
        userA.loginUser(userA.getUserName(), userA.getPassword());
        accessTokenIdForUserA = DbSelects.getAccessTokenIdByEmailAddress(database, userA.getEmail());
        userIdForUserA = DbSelects.getUserIdByEmailAddress(database, userA.getEmail());
        characterNameA = UserUtils.generateRandomCharacterName();
        userA.createCharacter(characterNameA, accessTokenIdForUserA, userIdForUserA);

        // create userB, login and create a character for it with RestAPI
        userB = new User();
        userB.loginUser(userB.getUserName(), userA.getPassword());
        accessTokenIdForUserB = DbSelects.getAccessTokenIdByEmailAddress(database, userB.getEmail());
        userIdForUserB = DbSelects.getUserIdByEmailAddress(database, userB.getEmail());
        characterNameB = UserUtils.generateRandomCharacterName();
        userB.createCharacter(characterNameB, accessTokenIdForUserB, userIdForUserB);
    }

    @AfterMethod(alwaysRun = true)
    private void terminate() {
        userA.logoutUser(userA.getUserName(), accessTokenIdForUserA, userIdForUserA);
        userB.logoutUser(userB.getUserName(), accessTokenIdForUserB, userIdForUserB);
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Feature(FRIEND_REQUEST)
    public void sendAndReceiveFriendRequest() {
        log.info("usernameA = {}; characterNameA = {}; userNameB = {}; characterNameB = {}", userA.getUserName(), characterNameA, userB.getUserName(), characterNameB);

        // TODO:
        // - login with userA
        // - choose characterNameA
        // - create a page class for the overview page
        // - push the button "Közösség"
        // - create a page class for the community page
        // - push the button "Barát felvétele"
        // ...
    }

}
