package com.mentoring.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
                WebDriverManager.chromedriver().setup();
                ChromeOptions capabilityRemoteChrome = new ChromeOptions();
                capabilityRemoteChrome.setAcceptInsecureCerts(true);
                WebDriver remoteWebDriver = new RemoteWebDriver(new URL(GRID_HUB_URL), capabilityRemoteChrome);
                browserVersion = ((ChromeDriver) remoteWebDriver).getCapabilities().getVersion();
                return remoteWebDriver;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setAcceptInsecureCerts(true);
                WebDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
                browserVersion = ((FirefoxDriver) firefoxDriver).getCapabilities().getVersion();
                return firefoxDriver;
            case CHROME:
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions capabilityChrome = new ChromeOptions();
                capabilityChrome.setAcceptInsecureCerts(true);
                WebDriver chromeDriver = new ChromeDriver(capabilityChrome);
                browserVersion = ((ChromeDriver) chromeDriver).getCapabilities().getVersion();
                return chromeDriver;
        }
    }
}