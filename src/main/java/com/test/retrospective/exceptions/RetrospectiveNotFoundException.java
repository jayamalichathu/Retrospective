package com.test.retrospective.exceptions;

/**
 * Custom exception class for indicating that a requested retrospective was not found.
 */
public class RetrospectiveNotFoundException extends RuntimeException {
    public RetrospectiveNotFoundException(String message) {
        super(message);
    }
}
