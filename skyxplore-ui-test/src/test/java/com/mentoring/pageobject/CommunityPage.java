package com.mentoring.pageobject;

import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.By.*;

@Slf4j
public class CommunityPage extends Page {

    private static final String FRIEND_CHARACTER_BY_NAME_SELECTOR = "//div[(text()='%1$s')]/following-sibling::button";
    private static final String APPROVE_RECEIVED_FRIEND_REQUEST_BY_NAME_SELECTOR = "//div[(text()='%1$s')]/following-sibling::div/button[text()='Elfogadás']";
    private static final String REFUSE_RECEIVED_FRIEND_REQUEST_BY_NAME_SELECTOR = "//div[(text()='%1$s')]/following-sibling::div/span/button[text()='Elutasítás']";
    private static final String CHARACTER_NAME_IN_FRIEND_LIST = "//div[@id='friend-list']/div/div[text()='%1$s']";

    @FindBy(id = "add-friend-button")
    private WebElement openAddFriendDialogButton;

    @FindBy(id = "friend-name")
    private WebElement characterSearchField;

    // Játékos nem található.
    @FindBy(id = "no-character-can-be-friend")
    private WebElement noCharacterCanBeFriend;

    @FindBy(id = "add-friend-search-result")
    private WebElement addFriendSearchResult;

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "sent-friend-requests-tab-button")
    private WebElement sentFriendRequestsButton;

    @FindBy(id = "friend-requests-tab-button")
    private WebElement friendRequestsButton;

    @FindBy(id = "no-friend-requests")
    private WebElement noFriendRequest;

    private static final String PAGE_PATH = "/community";

    CommunityPage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    CommunityPage waitUntilPageLoads() {
        return this;
    }

    @Step("Logging out")
    public IndexPage logout() {
        log.info("Logging out from the application.");
        waitUntilClickable(logoutButton).click();
        return new IndexPage(driver);
    }

    @Step("Click login.")
    public CommunityPage clickAddFriendButton() {
        log.info("Clicking on add friend button...");
        waitUntilClickable(openAddFriendDialogButton).click();
        return this;
    }

    @Step("Entering character name: {characterName}")
    public CommunityPage enterFriendCharacterName(String characterName) {
        log.info("Entering character name: '{}'", characterName);
        type(characterSearchField, characterName);
        waitUntilVisible(xpath(String.format(FRIEND_CHARACTER_BY_NAME_SELECTOR, characterName)));
        return this;
    }

    @Step("Clicking on Invite on a desired friend button for character: {characterName}.")
    public CommunityPage clickInviteCharacter(String characterName) {
        waitUntilVisible(xpath(String.format(FRIEND_CHARACTER_BY_NAME_SELECTOR, characterName)))
                .click();
        return this;
    }

    @Step("Clicking on sent friend requests button.")
    public CommunityPage clicksentFriendRequestsButton() {
        log.info("Clicking on sent friend requests button...");
        waitUntilClickable(sentFriendRequestsButton).click();
        return this;
    }

    @Step("Clicking on friend requests button.")
    public CommunityPage clickFriendRequestsButton() {
        log.info("Clicking on friend requests button...");
        waitUntilClickable(friendRequestsButton).click();
        return this;
    }

    @Step("Clicking on the received friend request approve button: {characterName}.")
    public CommunityPage clickReceivedFriendRequestApproveButton(String characterName) {
        waitUntilVisible(xpath(String.format(APPROVE_RECEIVED_FRIEND_REQUEST_BY_NAME_SELECTOR, characterName)))
                .click();
        return this;
    }

    @Step("Investigate friend list that {characterName} appears or not.")
    public boolean isCharacterInFriendList(String characterName) {
        return isElementDisplayed(xpath(String.format(CHARACTER_NAME_IN_FRIEND_LIST, characterName)));
    }

    @Step("Asserting if character name is marked as invalid.")
    public boolean isFriendRequestListEmpty() {
        return isElementDisplayed(noFriendRequest);
    }

    @Step("Clicking on the received friend request refuse button: {characterName}.")
    public CommunityPage clickReceivedFriendRequestDeclineButton(String characterName) {
        Actions action = new Actions(driver);
        WebElement we = waitUntilVisible(xpath(String.format(APPROVE_RECEIVED_FRIEND_REQUEST_BY_NAME_SELECTOR, characterName)));
        action
                .moveToElement(we)
                .moveToElement(driver.findElement(xpath(String.format(REFUSE_RECEIVED_FRIEND_REQUEST_BY_NAME_SELECTOR, characterName))))
                .click()
                .build()
                .perform();
        return this;
    }
}