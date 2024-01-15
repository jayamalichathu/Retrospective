package com.test.retrospective.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to centralize and handle custom exceptions thrown within the application.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DuplicateRetrospectiveException and returns a conflict response.
     * @param exception DuplicateRetrospectiveException instance.
     * @return ResponseEntity with the error message and HTTP status code.
     */
    @ExceptionHandler(DuplicateRetrospectiveException.class)
    public ResponseEntity<String> handleDuplicateRetrospective(DuplicateRetrospectiveException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }


    /**
     * Handles InvalidRetrospectiveException and returns a bad request response.
     * @param exception InvalidRetrospectiveException instance.
     * @return ResponseEntity with the error message and HTTP status code.
     */
    @ExceptionHandler(InvalidRetrospectiveException.class)
    public ResponseEntity<String> handleInvalidRetrospective(InvalidRetrospectiveException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles RetrospectiveNotFoundException and returns a not found response.
     * @param exception RetrospectiveNotFoundException instance.
     * @return ResponseEntity with the error message and HTTP status code.
     */
    @ExceptionHandler(RetrospectiveNotFoundException.class)
    public ResponseEntity<String> handleNotFoundRetrospective(RetrospectiveNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles FeedbackNotFoundException and returns a not found response.
     * @param exception FeedbackNotFoundException instance.
     * @return ResponseEntity with the error message and HTTP status code.
     */
    @ExceptionHandler(FeedbackNotFoundException.class)
    public ResponseEntity<String> handleFeedbackNotFoundException(FeedbackNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
