package com.mentoring.pageobject;

import com.mentoring.config.Config;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class LobbyQueuePage extends Page {

    private static final String PAGE_PATH = "/lobby-queue";
    private static final String PAGE_TITLE = "Játék keresése";

    LobbyQueuePage(WebDriver driver) {
        super(driver, PAGE_PATH);
    }

    public static String getPageUrl() {
        return Config.getApplicationUrl().concat(PAGE_PATH);
    }

    @Override
    LobbyQueuePage waitUntilPageLoads() {
        await().atMost(10, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> driver.getTitle().contains(PAGE_TITLE));
        return this;
    }
}