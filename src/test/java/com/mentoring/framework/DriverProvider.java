package com.mentoring.framework;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
class DriverProvider {

    private static final String HUB_URL = "TBD";

    static WebDriver getInstance() throws MalformedURLException {
        switch (Config.BROWSER) {
            case REMOTE_CHROME:
                ChromeOptions capabilityRemoteChrome = new ChromeOptions();
                capabilityRemoteChrome.setAcceptInsecureCerts(true);
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(HUB_URL), capabilityRemoteChrome);
                remoteWebDriver.setFileDetector(new LocalFileDetector());
                return remoteWebDriver;
            case FIREFOX:
                System.setProperty("webdriver.gecko.driver", "drivers/firefox/geckodriver.exe");
                return new FirefoxDriver();
            case CHROME:
            default:
                System.setProperty("webdriver.chrome.driver", "drivers/chrome/chromedriver.exe");
                return new ChromeDriver();
        }
    }
}