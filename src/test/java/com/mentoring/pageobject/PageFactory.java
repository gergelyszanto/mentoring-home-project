package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    public MainPage getMainPage(WebDriver driver) {
        return Config.isLocalEnvironmentUsed() ? new MainPageDev(driver) : new MainPageProd(driver);
    }

    public CharacterSelectionPage getCharacterSelectionPage(WebDriver driver) {
        return Config.isLocalEnvironmentUsed() ? new CharacterSelectionPageDev(driver) : new CharacterSelectionPageProd(driver);
    }
}
