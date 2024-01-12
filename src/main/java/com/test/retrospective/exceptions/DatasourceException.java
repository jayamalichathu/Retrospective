package com.test.retrospective.exceptions;

public class DatasourceException extends RuntimeException {
    public DatasourceException(String message, Exception exception) {
        super(message);
    }
}
