package com.test.retrospective.exceptions;


/**
 * Custom exception class for indicating that an attempt was made to create a duplicate retrospective.
 */
public class DuplicateRetrospectiveException extends RuntimeException {
    public DuplicateRetrospectiveException(String message) {
        super(message);
    }
}
