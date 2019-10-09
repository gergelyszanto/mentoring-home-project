package com.mentoring.utilities;

@FunctionalInterface
public interface RetryStatement {
    boolean evaluate();
}
