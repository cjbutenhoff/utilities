package com.wp.utils;

/**
 * Application specific exception
 * 
 */
public class TargetingFrameworkException extends Exception {

    private static final long serialVersionUID = 1L;

    public TargetingFrameworkException() {
        super();
    }

    public TargetingFrameworkException(String message, Throwable e) {
        super(message, e);
    }

    public TargetingFrameworkException(String message) {
        super(message);
    }

    public TargetingFrameworkException(Throwable e) {
        super(e);
    }
}
