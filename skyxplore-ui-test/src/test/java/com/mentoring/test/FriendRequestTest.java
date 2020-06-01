package com.mentoring.test;

import com.mentoring.config.Config;
import com.mentoring.database.Database;
import com.mentoring.database.DbSelects;
import com.mentoring.exceptions.EnvironmentNotSupportedException;
import com.mentoring.framework.BasicTest;
import com.mentoring.generator.User;
import com.mentoring.messages.Messages;
import com.mentoring.pageobject.CommunityPage;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.NotificationContainer;
import com.mentoring.test.assertionsteps.CommonAssertions;
import com.mentoring.utilities.UserUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static com.mentoring.model.Features.FRIEND_REQUEST;

@Slf4j
public class FriendRequestTest extends BasicTest {

    private static final String FRIEND_LIST_ASSERTION_MESSAGE = " should appear in friend list.";

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
    private String characterIdA;
    private String characterIdB;

    @AfterMethod(alwaysRun = true)
    private void disconnectFromDb() {
        if (database != null) {
            database.disconnectFromDb();
        } else {
            log.warn("Db disconnection skipped. There is no database connection to disconnect.");
        }
    }

    @BeforeMethod(alwaysRun = true)
    private void setup() throws SQLException {
        connectToDB();
        softAssertion = new SoftAssertions();

        log.debug("\n\n****userA:****");
        // create userA, login and create a character for it with RestAPI
        userA = new User();
        userA.loginUser(userA.getUserName(), userA.getPassword());
        accessTokenIdForUserA = DbSelects.getAccessTokenIdByEmailAddress(database, userA.getEmail());
        userIdForUserA = DbSelects.getUserIdByEmailAddress(database, userA.getEmail());
        characterNameA = UserUtils.generateRandomCharacterName();
        userA.createCharacter(characterNameA, accessTokenIdForUserA, userIdForUserA);
        characterIdA = DbSelects.getCharacterIdByCharacterName(database, characterNameA);

        log.debug("\n\n****userB:****");
        // create userB, login and create a character for it with RestAPI
        userB = new User();
        userB.loginUser(userB.getUserName(), userA.getPassword());
        accessTokenIdForUserB = DbSelects.getAccessTokenIdByEmailAddress(database, userB.getEmail());
        userIdForUserB = DbSelects.getUserIdByEmailAddress(database, userB.getEmail());
        characterNameB = UserUtils.generateRandomCharacterName();
        userB.createCharacter(characterNameB, accessTokenIdForUserB, userIdForUserB);
        characterIdB = DbSelects.getCharacterIdByCharacterName(database, characterNameB);
    }

    private void connectToDB() {
        if (!Config.isLocalEnvironmentUsed()) {
            throw new EnvironmentNotSupportedException("Only local environment is supported for database connection. " +
                    "Skipping tests.");
        }
        database = new Database();
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

        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer,
                Messages.SENT_FRIEND_REQUEST);

        communityPage
                .logout()
                .login(userB)
                .selectCharacter(characterNameB)
                .openCommunityPage()
                .clickFriendRequestsButton()
                .clickReceivedFriendRequestApproveButton(characterNameA);

        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer,
                Messages.APPROVED_FRIEND_REQUEST);

        softAssertion.assertAll();
    }

    @Description("Negative test case 1: " +
            "User A and user B exist. " +
            "User A has already sent friend request to user B's character on the backend. " +
            "User B logs in and refuses user A's request.")
    @Test(groups = SMOKE)
    @Severity(SeverityLevel.BLOCKER)
    @Feature(FRIEND_REQUEST)
    public void sendAndDeclineFriendRequest() {
        userA.sendFriendRequest(characterIdA, accessTokenIdForUserA, userIdForUserA, characterIdB);

        new IndexPage(driver)
                .login(userB)
                .selectCharacter(characterNameB)
                .openCommunityPage()
                .clickFriendRequestsButton()
                .clickReceivedFriendRequestDeclineButton(characterNameA);

        NotificationContainer notificationContainer = new NotificationContainer(driver);

        CommonAssertions.assertNotificationMessageIsCorrect(softAssertion, notificationContainer,
                Messages.DECLINED_FRIEND_REQUEST);

        softAssertion.assertAll();
    }

    @Description("Negative test case 2: " +
            "User B has a pending request from user A. " +
            "User B renames the character he got the request with, " +
            "then accepts the request.")
    @Test(groups = SMOKE)
    @Severity(SeverityLevel.NORMAL)
    @Feature(FRIEND_REQUEST)
    public void renameCharacterAndAcceptFriendRequest() {
        userA.sendFriendRequest(characterIdA, accessTokenIdForUserA, userIdForUserA, characterIdB);

        String characterNameBMod = UserUtils.generateRandomCharacterName();

        CommunityPage communityPage = new IndexPage(driver)
                .login(userB)
                .renameAndChooseCharacter(characterNameB, characterNameBMod)
                .openCommunityPage()
                .clickFriendRequestsButton()
                .clickReceivedFriendRequestApproveButton(characterNameA)
                .logout()
                .login(userA)
                .selectCharacter(characterNameA)
                .openCommunityPage();

        softAssertion.assertThat(communityPage
                .isCharacterInFriendList(characterNameBMod))
                .as(characterNameBMod + FRIEND_LIST_ASSERTION_MESSAGE)
                .isTrue();

        softAssertion.assertAll();
    }

    @Description("Negative test case 3: " +
            "User B has a pending request from user A. " +
            "Delete user B's character " +
            "and check the request at user A.")
    @Test(groups = SMOKE)
    @Severity(SeverityLevel.NORMAL)
    @Feature(FRIEND_REQUEST)
    public void sendFriendRequestAndDeleteRequestedCharacter() {
        userA.sendFriendRequest(characterIdA, accessTokenIdForUserA, userIdForUserA, characterIdB);

        CommunityPage communityPage = new IndexPage(driver)
                .login(userB)
                .deleteCharacter(characterNameB)
                .logout()
                .login(userA)
                .selectCharacter(characterNameA)
                .openCommunityPage();

        softAssertion.assertThat(communityPage
                .isCharacterInFriendList(characterNameB))
                .as(characterNameB + FRIEND_LIST_ASSERTION_MESSAGE)
                .isFalse();

        softAssertion.assertAll();
    }

    @Description("Negative test case 4: " +
            "User B has a pending request from user A. " +
            "Delete user A's character " +
            "and check the request at user B.")
    @Test(groups = SMOKE)
    @Severity(SeverityLevel.NORMAL)
    @Feature(FRIEND_REQUEST)
    public void sendFriendRequestAndDeleteSenderCharacter() {
        userA.sendFriendRequest(characterIdA, accessTokenIdForUserA, userIdForUserA, characterIdB);

        CommunityPage communityPage = new IndexPage(driver)
                .login(userA)
                .deleteCharacter(characterNameA)
                .logout()
                .login(userB)
                .selectCharacter(characterNameB)
                .openCommunityPage()
                .clickFriendRequestsButton();

        softAssertion.assertThat(communityPage.isFriendRequestListEmpty())
                .as("Friend request list should be empty.")
                .isTrue();

        softAssertion.assertAll();
    }

}
