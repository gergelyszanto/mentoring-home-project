package com.mentoring.framework;

import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestListener extends BaseApiTest implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Test Log", type = "text/plain")
    public String stopCatch() {
        String result = MultithreadedConsoleOutputCatcher.getContent();
        MultithreadedConsoleOutputCatcher.stopCatch();
        return result;
    }

    @Attachment(value = "{0}", type = "text/plain")
    private String saveTextLog(String message) {
        return message;
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.warn("Test '{}' failed.", getTestMethodName(iTestResult));
        saveTextLog(getTestMethodName(iTestResult) + " failed.");
        stopCatch();
    }

    @Override
    public void onTestStart(ITestResult result) {
        MultithreadedConsoleOutputCatcher.startCatch ();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        stopCatch();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        stopCatch();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        stopCatch();
    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}
