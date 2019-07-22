package com.mentoring.framework.exceptions;

public class ConditionNotMetException extends AssertionError {

    public ConditionNotMetException(String skipMessage) {
        super(skipMessage);
    }
}
