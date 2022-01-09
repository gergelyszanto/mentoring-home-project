package com.mentoring.test;

import com.mentoring.framework.BaseUiTest;
import com.mentoring.database.Database;
import com.mentoring.database.DbUpdates;
import com.mentoring.utilities.UserUtils;
import com.mentoring.generator.User;
import com.mentoring.pageobject.AccountPage;
import com.mentoring.pageobject.CharacterSelectionPage;
import com.mentoring.pageobject.CommunityPage;
import com.mentoring.pageobject.EquipmentPage;
import com.mentoring.pageobject.FactoryPage;
import com.mentoring.pageobject.GamePage;
import com.mentoring.pageobject.HangarPageNormal;
import com.mentoring.pageobject.IndexPage;
import com.mentoring.pageobject.LobbyPage;
import com.mentoring.pageobject.LobbyQueuePage;
import com.mentoring.pageobject.OverviewPage;
import com.mentoring.pageobject.ShopPage;
import com.mentoring.test.assertionsteps.UrlAssertions;
import com.mentoring.config.Config;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.mentoring.model.Features.PAGE_REDIRECTS;
import static org.assertj.core.api.Assertions.assertThat;

public class PageRedirectRulesTest extends BaseUiTest {

    private static final String GOOGLE = "https://www.google.com";
    private Database database;

    @DataProvider(name = "pageUrls")
    public static Object[][] pageUrls() {
        return new Object[][]{
                {AccountPage.getPageUrl()}, {CharacterSelectionPage.getPageUrl()}, {CommunityPage.getPageUrl()},
                {EquipmentPage.getPageUrl()}, {FactoryPage.getPageUrl()}, {GamePage.getPageUrl()}, {HangarPageNormal.getPageUrl()},
                {LobbyPage.getPageUrl()}, {LobbyQueuePage.getPageUrl()}, {OverviewPage.getPageUrl()}, {ShopPage.getPageUrl()},
                {IndexPage.getPageUrl()}
        };
    }

    @BeforeClass(alwaysRun = true)
    private void setupDatabase() {
        if (Config.isLocalEnvironmentUsed()) {
            database = new Database();
        }
    }

    @AfterClass(alwaysRun = true)
    private void closeDatabase() {
        if (Config.isLocalEnvironmentUsed()) {
            database.disconnectFromDb();
        }
    }

    @Description("User shouldn't be able to reach pages without login.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.BLOCKER)
    @Test(groups = {REGRESSION, REDIRECT_RULES}, dataProvider = "pageUrls")
    public void unauthenticatedUserRedirectedToIndexPage(String pageUrl) {
        IndexPage indexPage = new IndexPage(driver).waitUntilPageLoads();
        indexPage.openUrl(pageUrl);
        UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), indexPage.getUrl());
    }

    @Description("Authenticated user without character selected should be able to reach only the account page from url. " +
            "Other pages redirects the user back to the character page.")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {REGRESSION, REDIRECT_RULES}, dataProvider = "pageUrls")
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
    @Test(groups = {REGRESSION, REDIRECT_RULES}, dataProvider = "pageUrls")
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
    @Test(groups = {REGRESSION, REDIRECT_RULES}, dataProvider = "pageUrls")
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
    @Test(groups = {REGRESSION, REDIRECT_RULES}, dataProvider = "pageUrls")
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
        } else if (pageUrl.equals(HangarPageNormal.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), HangarPageNormal.getPageUrl());
        } else if (pageUrl.equals(ShopPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), ShopPage.getPageUrl());
        } else if (pageUrl.equals(CharacterSelectionPage.getPageUrl())) {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), CharacterSelectionPage.getPageUrl());
        } else {
            UrlAssertions.assertCurrentUrlMatchExpectedUrl(driver.getCurrentUrl(), overviewPage.getUrl());
        }
    }

    @Issue("2")
    @Description("User should be redirected to the index page after leaving the application " +
            "form overview page and opening the application root url with expired access token")
    @Feature(PAGE_REDIRECTS)
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {REGRESSION, REDIRECT_RULES})
    public void userLeavesApplicationOnOverviewPageAndComesBackWithExpiredSession() {
        skipTestIfNotLocalEnvironmentUsed();
        User user = new User();
        String characterName = UserUtils.generateRandomCharacterName();
        OverviewPage overviewPage = new IndexPage(driver)
                .login(user)
                .createNewCharacter(characterName)
                .selectCharacter(characterName);

        overviewPage.openUrl(GOOGLE);

        DbUpdates.setLastAccessValueByEmailAddress(database, user.getEmail(), 0);
        driver.get(Config.getApplicationUrl());
        IndexPage indexPage = new IndexPage(driver);
        assertThat(indexPage.isIndexPageLoaded())
                .as("Index page should have loaded and login button should be visible.")
                .isTrue();
    }
}