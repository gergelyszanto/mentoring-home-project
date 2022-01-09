package com.mentoring.pageobject;

import com.mentoring.generator.User;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class LoginAccessTokenStrategy implements ILoginStrategy {

    public CharacterSelectionPage login(WebDriver driver, IndexPage indexPage, User user) {
        Cookie cookie1 = new Cookie( "userid", user.getUserId());
        Cookie cookie2 = new Cookie( "accesstokenid", user.getAccessToken());
        driver.manage().addCookie(cookie1);
        driver.manage().addCookie(cookie2);
        CharacterSelectionPage characterSelectionPage = new CharacterSelectionPage(driver);
        driver.get(characterSelectionPage.getUrl());
        return characterSelectionPage;
    }
}
