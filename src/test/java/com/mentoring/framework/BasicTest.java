package com.mentoring.framework;

import com.google.common.collect.ImmutableMap;
import com.mentoring.framework.utils.CookieUtils;
import com.mentoring.framework.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BasicTest {

    private static final short PAGE_LOAD_TIMEOUT = 60;
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod(Method method) {
        log.info(LogUtils.createMessageWithBorder("Executing test case: " + method.getName(), "="));
        setupBrowser();
    }

    private void setupBrowser() {
        try {
            initializeDriver();
            log.info("URL is: {}", Config.APPLICATION_URL);
            driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
            driver.get(Config.APPLICATION_URL);
            resizeBrowser(driver);
            CookieUtils.setTestCookies(driver);
        } catch (Exception e) {
            log.error("Error at setup: ", e);
        }
    }

    private void resizeBrowser(WebDriver driver) {
        driver.manage().window().maximize();
    }

    private void initializeDriver() {
        try {
            LocalDriverManager.setWebDriver(DriverProvider.getInstance());
            driver = LocalDriverManager.getDriver();
            if (driver == null) {
                log.error("driver object is null !!!");
                throw new AssertionError("driver object is null");
            }
        } catch (WebDriverException exception) {
            log.error("Unknown error at driver initialization", exception);
            throw new AssertionError("Exception Caught during initializeDriver");
        } catch (MalformedURLException exception) {
            log.error("Error at driver initialization...", exception);
            throw new AssertionError("Exception Caught during initializeDriver");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void postMethodActions(Method method, ITestResult testResult) {
        LocalDriverManager.getDriver().quit();
        LocalDriverManager.setWebDriver(null);
        driver = null;
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (LocalDriverManager.getDriver() != null) {
            LocalDriverManager.getDriver().quit();
            LocalDriverManager.setWebDriver(null);
            driver = null;
        }
    }

    @AfterSuite(alwaysRun = true)
    void setAllureEnvironment() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Browser", System.getenv("BROWSER"))
                        .put("Browser.Version", DriverProvider.getBrowserVersion())
                        .put("URL", Config.getApplicationUrl())
                        .build(), "build/reports/allure-results/");
    }
}