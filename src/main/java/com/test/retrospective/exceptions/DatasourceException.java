package com.test.retrospective.exceptions;

/**
 * Custom exception class for handling errors related to datasource operations.
 */
public class DatasourceException extends RuntimeException {
    public DatasourceException(String message, Exception exception) {
        super(message, exception);
    }
}
