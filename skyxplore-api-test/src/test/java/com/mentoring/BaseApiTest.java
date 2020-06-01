package com.mentoring;

import com.mentoring.utilities.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

@Listeners({TestListener.class})
@Slf4j
public class BaseApiTest {

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod(Method method) {
        log.info(LogUtils.createMessageWithBorder("Executing test case: " + method.getName(), "="));
    }

//    @AfterSuite(alwaysRun = true)
//    void setAllureEnvironment() {
//        allureEnvironmentWriter(
//                ImmutableMap.<String, String>builder()
//                        .put("URL", Config.getApplicationUrl())
//                        .build(), "build/reports/allure-results/");
//    }
}
