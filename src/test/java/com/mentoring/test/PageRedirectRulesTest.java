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
import com.mentoring.pageobject.MainPage;
import com.mentoring.pageobject.OverviewPage;
import com.mentoring.pageobject.ShopPage;
import com.mentoring.test.assertionsteps.UrlAssertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PageRedirectRulesTest extends BasicTest {

    private SoftAssertions softAssertion;

    @DataProvider(name = "pageUrls")
    public static Object[][] pageUrls() {
        return new Object[][]{
                {AccountPage.getPageUrl()}, {CharacterSelectionPage.getPageUrl()}, {CommunityPage.getPageUrl()},
                {EquipmentPage.getPageUrl()}, {FactoryPage.getPageUrl()}, {GamePage.getPageUrl()}, {HangarPage.getPageUrl()},
                {LobbyPage.getPageUrl()}, {LobbyQueuePage.getPageUrl()}, {OverviewPage.getPageUrl()}, {ShopPage.getPageUrl()},
                {MainPage.getPageUrl()}
        };
    }

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        softAssertion = new SoftAssertions();
    }

    @Test(groups = "regression", dataProvider = "pageUrls")
    public void unauthenticatedUserRedirectedToIndexPage(String pageUrl) {
        MainPage mainPage = new MainPage(driver).waitUntilPageLoads();
        mainPage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), mainPage.getUrl());
    }

    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserWithNoCharacterSelected(String pageUrl) {
        User user = new User();
        CharacterSelectionPage characterSelectionPage = new MainPage(driver)
                .login(user);
        characterSelectionPage.openUrl(pageUrl);
        if (pageUrl.equals(AccountPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), AccountPage.getPageUrl());
        } else {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), characterSelectionPage.getUrl());
        }
    }

    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserInTheLobby(String pageUrl) {
        String characterName = UserUtils.generateRandomCharacterName();
        User user = new User();
        LobbyPage lobbyPage = new MainPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectFirstCharacter(characterName)
                .openHangarPage()
                .selectArcadeMode();
        lobbyPage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), lobbyPage.getUrl());
    }

    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserInMatchmaking(String pageUrl) {
        User user = new User();
        String characterName = UserUtils.generateRandomCharacterName();
        LobbyQueuePage queuePage = new MainPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectFirstCharacter(characterName)
                .openHangarPage()
                .selectArcadeMode()
                .clickReadyButton()
                .openMatchmakingPage();
        queuePage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), queuePage.getUrl());
    }

    @Test(groups = "regression", dataProvider = "pageUrls")
    public void pageRedirectsForAuthenticatedUserWithCharacterSelected(String pageUrl) {
        User user = new User();
        String characterName = UserUtils.generateRandomCharacterName();
        OverviewPage overviewPage = new MainPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectFirstCharacter(characterName);
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