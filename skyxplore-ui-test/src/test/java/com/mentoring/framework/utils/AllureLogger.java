package com.mentoring.framework.utils;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;

import static io.qameta.allure.Allure.getLifecycle;

@Slf4j
public class AllureLogger {

    private AllureLogger() {
    }

    /**
     * Uses the @Step annotation to log the given log message to Allure.
     *
     * @param message the message to log to the allure report
     */
    @Step("{message}")
    public static void logToAllure(String message) {
        log.debug("[Logging to Allure] " + message);
    }

}