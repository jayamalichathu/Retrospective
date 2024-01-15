package com.test.retrospective.exceptions;

/**
 * Custom exception class for indicating that feedback associated with a retrospective was not found.
 */
public class FeedbackNotFoundException extends RuntimeException {
    public FeedbackNotFoundException(String message) {
        super(message);
    }
}
