package com.mentoring.pageobject;

import com.mentoring.generator.User;
import org.openqa.selenium.WebDriver;

public class LoginFrontendAuthStrategy implements ILoginStrategy {

    public CharacterSelectionPage login(WebDriver driver, IndexPage indexPage, User user) {
        indexPage.fillLoginForm(user.getUserName(), user.getPassword());
        indexPage.clickLoginButton();
        return new CharacterSelectionPage(driver);
    }
}
