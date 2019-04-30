package com.mentoring.framework.utils;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;

import static io.qameta.allure.Allure.getLifecycle;

@Slf4j
public class AllureLogger {

    //    private static final Logger logger = LogManager.getLogger();
    private static final ThreadLocal<Deque<String>> STEP_UUID_STACK =
            ThreadLocal.withInitial(ArrayDeque::new);

    private AllureLogger() {
        // hide default constructor for this util class
    }

    /**
     * Uses the @Step annotation to log the given log message to Allure.
     *
     * @param message the message to log to the allure report
     */
    @Step("{message}")
    public static void logToAllure(String message) {
        log.debug("Logged to Allure: " + message);
    }

//    public static void stepStart(String stepName) {
//        StepResult result = new StepResult().setName(stepName);
//        String uuid = UUID.randomUUID().toString();
//        getLifecycle().startStep(uuid, result);
//        STEP_UUID_STACK.get().addFirst(uuid);
//    }

    public static void stepFinish() {
        getLifecycle().stopStep(STEP_UUID_STACK.get().removeFirst());
    }
}