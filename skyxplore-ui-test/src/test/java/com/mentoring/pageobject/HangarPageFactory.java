package com.mentoring.pageobject;

import org.openqa.selenium.WebDriver;

public class HangarPageFactory {

    public HangarPage getHangarPage(WebDriver driver) {
        if (System.getenv("CHANGED_HANGAR") != null) {
            return new HangarPageNew(driver);
        } else {
            return new HangarPageNormal(driver);
        }
    }
}
