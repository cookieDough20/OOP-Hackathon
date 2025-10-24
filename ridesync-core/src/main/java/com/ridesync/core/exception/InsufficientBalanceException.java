package com.ridesync.core.exception;

/**
 * Exception thrown when rider has insufficient balance for the ride
 */
public class InsufficientBalanceException extends RuntimeException {
    
    private final String riderId;
    private final double required;
    private final double available;
    
    public InsufficientBalanceException(String riderId, double required, double available) {
        super(String.format("Insufficient balance for rider %s. Required: %.2f, Available: %.2f", 
            riderId, required, available));
        this.riderId = riderId;
        this.required = required;
        this.available = available;
    }
    
    public String getRiderId() {
        return riderId;
    }
    
    public double getRequired() {
        return required;
    }
    
    public double getAvailable() {
        return available;
    }
}
