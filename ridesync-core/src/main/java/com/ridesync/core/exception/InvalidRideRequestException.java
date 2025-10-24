package com.ridesync.core.exception;

/**
 * Exception thrown when a ride request is invalid.
 */
public class InvalidRideRequestException extends RuntimeException {
    
    public InvalidRideRequestException(String message) {
        super(message);
    }
}
