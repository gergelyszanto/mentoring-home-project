package com.mentoring.framework;

import com.google.common.collect.ImmutableMap;
import com.mentoring.exceptions.EnvironmentNotSupportedException;
import com.mentoring.framework.utils.CookieUtils;
import com.mentoring.utilities.LogUtils;
import com.mentoring.config.Config;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

@Listeners({TestListener.class})
@Slf4j
public abstract class BaseUiTest {

    protected static final String SMOKE = "smoke";
    protected static final String REGRESSION = "regression";
    protected static final String REDIRECT_RULES = "redirect-rules";
    protected static final String NEGATIVE = "negative";

    private static final short PAGE_LOAD_TIMEOUT = 60;
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod(Method method) {
        log.info(LogUtils.createMessageWithBorder("Executing test case: " + method.getName(), "="));
        setupBrowser();
    }

    @Step("Initializing driver and opening URL: {Config.getApplicationUrl()}")
    private void setupBrowser() {
        try {
            initializeDriver();
            log.info("URL is: {}", Config.getApplicationUrl());
            driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
            driver.get(Config.getApplicationUrl());
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
            driver = DriverProvider.getInstance();
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

    protected void skipTestIfNotLocalEnvironmentUsed() {
        if (!Config.isLocalEnvironmentUsed()) {
            throw new EnvironmentNotSupportedException("Only local environment is supported for this test. " +
                    "Skipping test...");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void postMethodActions(Method method, ITestResult testResult) {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
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