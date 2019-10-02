package com.mentoring.framework.utils;

import com.mentoring.framework.exceptions.ConditionNotMetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitHelper {

    private static String RETRY_MESSAGE = "Condition not fulfilled. Tried {} times out of {}.";
    private static String WAITING_FAILED_MESSAGE = "Waiting failed.";

    public static void waitUntilConditionFulfilled(RetryStatement statement, int maxNumberOfRetries, int pollingInMilliSecs, String logMessage) {
        log.info(logMessage);
        for (int i = 1; i <= maxNumberOfRetries; i++) {
            if (statement.evaluate()) {
                break;
            } else if (!statement.evaluate() && i == maxNumberOfRetries) {
                throw new ConditionNotMetException("Condition could not be fulfilled.");
            } else {
                try {
                    log.info(RETRY_MESSAGE, i, maxNumberOfRetries);
                    Thread.sleep(pollingInMilliSecs);
                } catch (InterruptedException e) {
                    log.error(WAITING_FAILED_MESSAGE, e);
                }
            }
        }
    }
}