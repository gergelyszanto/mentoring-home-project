package com.mentoring;

import com.google.common.collect.ImmutableMap;
import com.mentoring.config.Config;
import com.mentoring.utilities.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

@Listeners({TestListener.class})
@Slf4j
public class BaseApiTest {

    protected static final String SMOKE = "smoke";
    protected static final String REGRESSION = "regression";
    protected static final String NEGATIVE = "negative";

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod(Method method) {
        log.info(LogUtils.createMessageWithBorder("Executing test case: " + method.getName(), "="));
    }

    @AfterSuite(alwaysRun = true)
    void setAllureEnvironment() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("URL", Config.getApplicationUrl())
                        .build(), "build/reports/allure-results/");
    }
}
