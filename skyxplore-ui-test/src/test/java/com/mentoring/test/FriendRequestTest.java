package com.mentoring.test;

import com.mentoring.database.Database;
import com.mentoring.database.DbSelects;
import com.mentoring.framework.BasicTest;
import com.mentoring.generator.User;
import com.mentoring.messages.Messages;
import com.mentoring.pageobject.CommunityPage;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.test.assertionsteps.CommonAssertions;
import com.mentoring.utilities.UserUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
    private String accessTokenIdForUserA;
    private String accessTokenIdForUserB;
    private String userIdForUserA;
    private String userIdForUserB;

    @BeforeClass(alwaysRun = true)
    private void connectToDB() {
        database = new Database();
    }

    @AfterClass(alwaysRun = true)
    private void disconnectFromDb() {
        if (database != null) {
            database.disconnectFromDb();
        } else {
            log.warn("Db disconnection skipped. There is no database connection to disconnect.");
        }
    }

    @BeforeMethod(alwaysRun = true)
    private void setup() throws SQLException {
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

    @Test(groups = SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @Feature(FRIEND_REQUEST)
    public void sendAndReceiveFriendRequest() {
        CommunityPage communityPage = new IndexPage(driver)
                .login(userA)
                .selectCharacter(characterNameA)
                .openCommunityPage()
                .clickAddFriendButton()
                .enterFriendCharacterName(characterNameB)
                .clickInviteCharacter(characterNameB);

        NotificationContainer notificationContainer = new NotificationContainer(driver);

        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.SENT_FRIEND_REQUEST);

        communityPage
                .logout()
                .login(userB)
                .selectCharacter(characterNameB)
                .openCommunityPage()
                .clickFriendRequestsButton()
                .clickReceivedFriendRequestApproveButton(characterNameA);

        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer, Messages.APPROVED_FRIEND_REQUEST);

        softAssertion.assertAll();
    }

}
