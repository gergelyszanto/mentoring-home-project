package com.mentoring.framework;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
class DriverProvider {

    private static final Browser BROWSER;

    static {
        BROWSER = Browser.parse(System.getenv("BROWSER"));
    }

    @Getter
    private static String browserVersion;

    private static final String GRID_HUB_URL = "TBD";

    static WebDriver getInstance() throws MalformedURLException {
        switch (BROWSER) {
            case REMOTE_CHROME:
                ChromeOptions capabilityRemoteChrome = new ChromeOptions();
                capabilityRemoteChrome.setAcceptInsecureCerts(true);
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(GRID_HUB_URL), capabilityRemoteChrome);
                remoteWebDriver.setFileDetector(new LocalFileDetector());
                browserVersion = remoteWebDriver.getCapabilities().getVersion();
                return remoteWebDriver;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                System.setProperty("webdriver.gecko.driver", "drivers/firefox/geckodriver.exe");
                FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
                browserVersion = firefoxDriver.getCapabilities().getVersion();
                return firefoxDriver;
            case CHROME:
            default:
                ChromeOptions capabilityChrome = new ChromeOptions();
                System.setProperty("webdriver.chrome.driver", "drivers/chrome/chromedriver.exe");
                ChromeDriver chromeDriver = new ChromeDriver(capabilityChrome);
                browserVersion = chromeDriver.getCapabilities().getVersion();
                return chromeDriver;
        }
    }
}