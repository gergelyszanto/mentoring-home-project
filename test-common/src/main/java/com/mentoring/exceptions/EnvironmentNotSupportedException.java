package com.mentoring.exceptions;

import org.testng.SkipException;

public class EnvironmentNotSupportedException extends SkipException {

    public EnvironmentNotSupportedException(String skipMessage) {
        super(skipMessage);
    }
}