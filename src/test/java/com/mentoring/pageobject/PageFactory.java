package com.mentoring.pageobject;

import com.mentoring.framework.Config;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    public MainPage getMainPage(WebDriver driver) {
        if (System.getenv(Config.ENVIRONMENT).equals(Config.SKY_XPLORE_LOCALHOST)) {
            return new MainPageNew(driver);
        } else {
            return new MainPageOld(driver);
        }
    }

    public CharacterSelectionPage getCharacterSelectionPage(WebDriver driver) {
        if (System.getenv(Config.ENVIRONMENT).equals(Config.SKY_XPLORE_LOCALHOST)) {
            return new CharacterSelectionPageNew(driver);
        } else {
            return new CharacterSelectionPageOld(driver);
        }
    }
}
