package com.test.retrospective.exceptions;

/**
 * Custom exception class for indicating that an attempt was made to create or process an invalid retrospective.
 */
public class InvalidRetrospectiveException extends RuntimeException {
    public InvalidRetrospectiveException(String message) {
        super(message);
    }
}
