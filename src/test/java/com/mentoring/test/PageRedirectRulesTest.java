package com.mentoring.test;

import com.mentoring.framework.BasicTest;
import com.mentoring.framework.utils.UserUtils;
import com.mentoring.model.User;
import com.mentoring.pageobject.AccountPage;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.CommunityPage;
import com.mentoring.pageobject.EquipmentPage;
import com.mentoring.pageobject.FactoryPage;
import com.mentoring.pageobject.GamePage;
import com.mentoring.pageobject.HangarPage;
import com.mentoring.pageobject.LobbyPage;
import com.mentoring.pageobject.LobbyQueuePage;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.OverviewPage;
import com.mentoring.pageobject.ShopPage;
import com.mentoring.test.assertionsteps.UrlAssertions;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.*;

public class PageRedirectRulesTest extends BasicTest {

    private SoftAssertions softAssertion;

    @DataProvider(name = "pageUrls")
    public static Object[][] pageUrls() {
        return new Object[][]{
                {AccountPage.getPageUrl()}, {CharacterSelectionPage.getPageUrl()}, {CommunityPage.getPageUrl()},
                {EquipmentPage.getPageUrl()}, {FactoryPage.getPageUrl()}, {GamePage.getPageUrl()}, {HangarPage.getPageUrl()},
                {LobbyPage.getPageUrl()}, {LobbyQueuePage.getPageUrl()}, {OverviewPage.getPageUrl()}, {ShopPage.getPageUrl()},
                {IndexPage.getPageUrl()}
        };
    }

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Description("User shouldn't be able to reach pages without login.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.BLOCKER)
    @Test(groups = "regression", dataProvider = "pageUrls")
    public void unauthenticatedUserRedirectedToIndexPage(String pageUrl) {
        IndexPage indexPage = new IndexPage(driver).waitUntilPageLoads();
        indexPage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), indexPage.getUrl());
    }

    @Description("Authenticated user without character selected should be able to reach only the account page from url. " +
            "Other pages redirects the user back to the character page.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserWithNoCharacterSelected(String pageUrl) {
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new IndexPage(driver)
                .login(user);
        characterSelectionPage.openUrl(pageUrl);
        if (pageUrl.equals(AccountPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), AccountPage.getPageUrl());
        } else {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), characterSelectionPage.getUrl());
        }
    }

    @Description("User should not be able to leave the lobby page by opening different page url-s.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserInTheLobby(String pageUrl) {
        String characterName = UserUtils.generateRandomCharacterName();
        User user = new User();
        LobbyPage lobbyPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectCharacter(characterName)
                .openHangarPage()
                .selectArcadeMode();
        lobbyPage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), lobbyPage.getUrl());
    }

    @Description("User shouldn't be able to leave matchmaking by opening different page url-s.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserInMatchmaking(String pageUrl) {
        User user = new User();
        String characterName = UserUtils.generateRandomCharacterName();
        LobbyQueuePage queuePage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectCharacter(characterName)
                .openHangarPage()
                .selectArcadeMode()
                .clickReadyButton()
                .openMatchmakingPage();
        queuePage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), queuePage.getUrl());
    }

    @Description("User should be able to open only certain pages by url when a character is selected.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserWithCharacterSelected(String pageUrl) {
        User user = new User();
        String characterName = UserUtils.generateRandomCharacterName();
        OverviewPage overviewPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectCharacter(characterName);
        overviewPage.openUrl(pageUrl);
        if (pageUrl.equals(CommunityPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), CommunityPage.getPageUrl());
        } else if (pageUrl.equals(EquipmentPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), EquipmentPage.getPageUrl());
        } else if (pageUrl.equals(FactoryPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), FactoryPage.getPageUrl());
        } else if (pageUrl.equals(HangarPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), HangarPage.getPageUrl());
        } else if (pageUrl.equals(ShopPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), ShopPage.getPageUrl());
        } else {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), overviewPage.getUrl());
        }
    }
}