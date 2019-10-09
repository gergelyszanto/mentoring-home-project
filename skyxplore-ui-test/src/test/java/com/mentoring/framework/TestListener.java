package com.mentoring.framework;

import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestListener extends BasicTest implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}", type = "text/plain")
    private String saveTextLog(String message) {
        return message;
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.warn("Test '{}' failed.", getTestMethodName(iTestResult));
        Object testClass = iTestResult.getInstance();
        WebDriver driver = ((BasicTest)testClass).driver;
        if (driver != null) {
            log.info("Screenshot captured for test case: {}", getTestMethodName(iTestResult));
            saveScreenshot(driver);
            saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot is taken.");
        } else {
            saveTextLog(getTestMethodName(iTestResult) + " failed.");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}
