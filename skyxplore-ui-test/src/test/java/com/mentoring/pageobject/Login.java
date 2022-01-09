package com.mentoring.pageobject;

import com.mentoring.generator.User;
import org.openqa.selenium.WebDriver;

public class Login {
    ILoginStrategy loginStrategy;

    public void setLoginStrategy(ILoginStrategy loginStrategy) {
        this.loginStrategy = loginStrategy;
    }

    public CharacterSelectionPage login(WebDriver driver, IndexPage indexPage, User user) {
        loginStrategy.login(driver, indexPage, user);
        return new CharacterSelectionPage(driver);
    }
}
