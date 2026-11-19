package com.example.transactionprocessor.exception;

/**
 * Exception used to indicate a transient failure
 * that can be retried safely.
 *
 * Examples:
 * - Temporary network outage
 * - Downstream service unavailable
 * - Database connection timeout
 */
public class RetryableException extends RuntimeException {

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
