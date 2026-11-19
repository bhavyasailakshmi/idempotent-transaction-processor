package com.example.transactionprocessor.exception;

/**
 * Exception thrown when a transaction
 * fails business validation checks.
 *
 * Examples:
 * - Missing transactionId
 * - Missing accountId
 * - Negative amount
 * - Invalid currency
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
