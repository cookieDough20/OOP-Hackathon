package com.ridesync.core.exception;

/**
 * Custom exception thrown when no driver is available for ride allocation.
 * Part of the error handling strategy for the ride-sharing platform.
 */
public class NoDriverAvailableException extends RuntimeException {
    
    public NoDriverAvailableException(String message) {
        super(message);
    }
    
    public NoDriverAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
