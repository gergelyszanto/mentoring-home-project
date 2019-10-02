package com.mentoring.framework.utils;

@FunctionalInterface
public interface RetryStatement {
    boolean evaluate();
}
