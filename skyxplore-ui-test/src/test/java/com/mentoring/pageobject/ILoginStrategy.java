package com.mentoring.pageobject;

import com.mentoring.generator.User;
import org.openqa.selenium.WebDriver;

public interface ILoginStrategy {

    CharacterSelectionPage login(WebDriver driver, IndexPage indexPage, User user);
}
